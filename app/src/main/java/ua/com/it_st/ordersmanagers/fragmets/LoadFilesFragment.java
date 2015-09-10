package ua.com.it_st.ordersmanagers.fragmets;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import com.loopj.android.http.RequestParams;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.utils.AsyncHttpClientUtil;
import ua.com.it_st.ordersmanagers.utils.ConstantsUtil;
import ua.com.it_st.ordersmanagers.utils.ErrorInfo;
import ua.com.it_st.ordersmanagers.utils.FileSizeLine;
import ua.com.it_st.ordersmanagers.utils.SQLiteOpenHelperUtil;

/* Класс предназначен для принимаема данных (файлы в формате csv) с сервера
   Файлы:
   ref_price.csv - цены
   ref_goodsbystores.csv - остатки по товарам
   ref_pricecategories.csv - типы цен
   ref_clients.csv  - клиенты и адресса
   ref_goods.csv - товары
   ref_firms.csv - организации
   ref_stores.csv - склады
  */

public class LoadFilesFragment extends FilesFragment {

    private final String TEG = LoadFilesFragment.class.getSimpleName();
    private double mProgress;
    private double mProgressDiscrete;
    private int nOSeek;
    private int AcountNameFile;
    private Map<String, String> lTableNameInsert;
    private Map<String, String> lTableName;

    public LoadFilesFragment() {
        super();
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.load_files_button:
                /* удаляем все записи из таблиц */
                SQLiteOpenHelperUtil.onDeleteValueTables(getDb());
                /*загружаем файлы с сервера*/
                dowloadFilesOfServer();
                break;
            case R.id.load_files_image_button_n:
                /*переходим в журанл заказаов*/
                final onEventListener someEventListener = (onEventListener) getActivity();
                someEventListener.onOpenFragmentClass(OrderListFragment.class);
                break;
            default:
                break;
        }
    }

    /* получаем количество всех строк в файлах до загрузки всех файлов */
    private void getSizeLine(RequestParams params, String wayCatalog, String idServer, String loginServer, String passwordServer) {

        params.put(getString(R.string.SizeFileCatalog), wayCatalog);

        FileSizeLine fileSizeLine = new FileSizeLine(idServer, params, loginServer, passwordServer);
        fileSizeLine.execute();
        try {
            fileSizeLine.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (ConstantsUtil.sizeFileLine > 0) {
            getProgressPieView().setMax(ConstantsUtil.sizeFileLine);
        }

    }

    /* загружаем файлы с сервера*/
    private void dowloadFilesOfServer() {

         /*получаем команды для записи новой строки в таблице*/
        lTableNameInsert = getFileNameInsert();
        /*получаем заголовки таблиц*/
        lTableName = getFileNameHeader();

       /* список файлов для загрузки */
        String[] nameFile = getResources().getStringArray(R.array.name_file_data);
        /*определяем кол-во файлов*/
        AcountNameFile = nameFile.length;
        /*обнуляем все значения перед загрузкой*/
        nullableValues();
        nOSeek = getnOSeek();
        mProgress = getProgress();
        mProgressDiscrete = getProgressDiscrete();
        //Log
        ErrorInfo.setmLogLine("Начало загрузки");
         /*подключаемся к серверу*/
        Object[] connectData = connectServer();
        /*Проверка*/
        if (connectData == null) {
            //Log
            ErrorInfo.setmLogLine("Не все установлены параметры для загрузки!");
            return;
        }
        /*подключились к базе или нет*/
        boolean lConnect = (boolean) connectData[0];
        //
        if (!lConnect) {
            //Log
            ErrorInfo.setmLogLine(getString(R.string.action_conect_base), true, TEG + getString(R.string.error_login_password_inet));
            return;
        }
      /*получаем параметры подключения*/
        AsyncHttpClientUtil utilAsyncHttpClient = (AsyncHttpClientUtil) connectData[1];
        String loginServer = (String) connectData[2];
        String passwordServer = (String) connectData[3];
        String wayCatalog = (String) connectData[4];
        String idServer = (String) connectData[5];
        //
        RequestParams params = new RequestParams();

        /* получаем количество всех строк в файлах до загрузки всех файлов */
        getSizeLine(params, wayCatalog, idServer, loginServer, passwordServer);

            /* начинаем транзакцию */
        getDb().beginTransaction();
        for (String i : nameFile) {
            params = new RequestParams();
            params.put(getString(R.string.NameCatalog), wayCatalog);
            params.put(getString(R.string.name_file), i);
            params.put(getString(R.string.SizeFileCatalog), "");

            //Log
            ErrorInfo.setmLogLine(getString(R.string.action_download_file), i);

            try {
                /* загружаем файл */
                utilAsyncHttpClient.getDownloadFiles(params, i);
            } catch (Exception e) {
                e.printStackTrace();
                //Log
                ErrorInfo.setmLogLine(getString(R.string.action_download_file), i, true, TEG + ": " + e.toString());
            }
        }
            /* заканчиваем транзакцию */
        getDb().setTransactionSuccessful();
        getDb().endTransaction();

    }

    public void onInsertTable(final File file,
                              final String fileName
    ) throws IOException {

        DownloadAsyncFile downloadAsyncFile = new DownloadAsyncFile(file,
                lTableNameInsert.get(fileName),
                lTableName.get(fileName)
        );
        downloadAsyncFile.execute();
    }

    public class DownloadAsyncFile extends AsyncTask<String, Integer, String> {

        private final String TEG = LoadFilesFragment.class.getSimpleName();
        private String mCvsSplitBy = ",\"";
        private String mNameTable;
        Handler handlerTotalLinesFile = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                /*выставляем макс и минимум прогресса*/
                int totalLinesFileSeekBar = bundle.getInt("totalLinesFile");
                getDiscreteSeekBar().setMax(totalLinesFileSeekBar);
                /**/
                getLoadFiles().setText("Загрузка " + mNameTable + "...");
                getDiscreteSeekBar().setMin(0);
            }
        };
        private String mLineInsert;
        private File mFile;
        private int limitInsert = 100;
        private int totalLinesFile;
        private int stotalLinesFile;

        public DownloadAsyncFile(final File file, final String lineInsert,
                                 final String nameTable) {
            mLineInsert = lineInsert;
            mNameTable = nameTable;
            mFile = file;
        }

        @Override
        protected String doInBackground(final String... files) {

            try {

                final BufferedReader input = new BufferedReader(new FileReader(mFile));
                /*вычисляем к-во строк в файле*/
                totalLinesFile = getCountFileLines(mFile);
                stotalLinesFile = totalLinesFile;
                mProgressDiscrete = 0;
                /*отсылаем сообщения прогрессу*/
                final Message msgTotalLinesFile = handlerTotalLinesFile.obtainMessage();
                final Bundle bundleTotalLinesFile = new Bundle();
                bundleTotalLinesFile.putInt("totalLinesFile", totalLinesFile);
                msgTotalLinesFile.setData(bundleTotalLinesFile);
                handlerTotalLinesFile.sendMessage(msgTotalLinesFile);

                /*создаем строку команды для базы данных в базу*/
                StringBuilder sql = new StringBuilder();

                int n = 0;/*счетчик*/
                int nDSeek = 0;/*счетчик*/
                /**/
                String line;
                input.readLine();
                 /* Считываем по одной строке */
                while ((line = input.readLine()) != null) {
                    /*получаем массив из строчке*/
                    String[] country = line.split(mCvsSplitBy);
                    String[] pCountry = new String[country.length];
                    /*начинаем формировать значения для insert*/
                    sql.append("(");

                    for (byte s = 0; s < country.length; s++) {
                        /*делаем замену символа*/
                        pCountry[s] = country[s].replace('\"', ' ');
                        pCountry[s] = pCountry[s].replace('\'', ' ');
                        /*убираем пробелы в строке*/
                        pCountry[s] = pCountry[s].trim();
                        /*добавляем значение из  файла*/
                        sql.append("'");
                        sql.append(pCountry[s]);
                        sql.append("'");
                        if (s + 1 != country.length) {
                            sql.append(",");
                        }
                    }
                    sql.append(")");
                   /*счетчик*/
                    n++;
                    publishProgress(++nDSeek, ++nOSeek);

                    /*делаем добавления пачки строк в базу при выполнеии условий*/
                    if (n == limitInsert || n == totalLinesFile) {
                        sql.append("");
                        getDb().execSQL(mLineInsert + sql);
                        totalLinesFile = totalLinesFile - n;
                        n = 0;
                        sql = new StringBuilder();
                    } else {
                        sql.append(",");
                    }
                }

                input.close();

            } catch (IOException e) {
                e.printStackTrace();
                //Log
                ErrorInfo.setmLogLine("Загрузка в таблицу ", mNameTable, true, TEG + e.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(final Integer... values) {
            super.onProgressUpdate(values);
            /* Линия прогресс */
            getDiscreteSeekBar().setProgress(values[0]);
            mProgressDiscrete += (100 / (double) stotalLinesFile);
            getTextProgress().setText(String.valueOf((int) mProgressDiscrete) + "%");
            /* Круг прогресс */
            getProgressPieView().setProgress(values[1]);
            mProgress += (100 / (double) ConstantsUtil.sizeFileLine);
            getProgressPieView().setText(String.valueOf((int) mProgress) + "%");
            //
        }

        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);

             /*счетчик файлов*/
            ConstantsUtil.nPieViewProgress++;
             /*если загрузился последний файл*/
            if (ConstantsUtil.nPieViewProgress == AcountNameFile) {
                getButtonOrderList().setVisibility(View.VISIBLE);
               /*считаем разницу между то что пришли данные из 1с и то что реально загрузилось*/
                int d = ConstantsUtil.sizeFileLine - nOSeek;
                /* Круг прогресс */
                getProgressPieView().setProgress(nOSeek + d);
                mProgress += (100 / (double) ConstantsUtil.sizeFileLine) * d;
                getProgressPieView().setText(String.valueOf((int) mProgress) + "%");
                /**/
                getLoadFiles().setText("Загрузка завершена!");
                getTextProgress().setText((int) mProgress + "%");
            }
            //Log
            ErrorInfo.setmLogLine("Загрузка в таблицу завершена", mNameTable);
        }
    }
}

