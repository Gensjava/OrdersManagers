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

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.utils.AsyncHttpClientUtil;
import ua.com.it_st.ordersmanagers.utils.ConnectServer;
import ua.com.it_st.ordersmanagers.utils.ConstantsUtil;
import ua.com.it_st.ordersmanagers.utils.Dialogs;
import ua.com.it_st.ordersmanagers.utils.InfoUtil;
import ua.com.it_st.ordersmanagers.utils.SQLiteOpenHelperUtil;
import ua.com.it_st.ordersmanagers.utils.WorkFiles;
import ua.com.it_st.ordersmanagers.utils.WorkSharedPreferences;

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

public class FilesLoadFragment extends FilesFragment {

    private final String TEG = FilesLoadFragment.class.getSimpleName();
    private double mProgress;
    private double mProgressDiscrete;
    private int nOSeek;
    private int AcountNameFile;
    private Map<String, String> lTableNameInsert;
    private Map<String, String> lTableName;

    public FilesLoadFragment() {
        super();
    }

    @Override
    public void onClick(final View view) {

        switch (view.getId()) {
            case R.id.load_files_button:
                /*загружаем файлы с сервера*/
                /*даем пользователю выбрать загружать новые данные или нет*/
                Dialogs.showYesNoDialog(getString(R.string.question_updata), getString(R.string.updata), (MainActivity) getActivity());
                break;
            case R.id.load_files_image_button_n:
                /*переходим в журанл заказаов*/
                onEventListener someEventListener = (onEventListener) getActivity();
                someEventListener.onOpenFragmentClass(OrderListFragment.class);
                break;
            case R.id.load_files_imageView:
                /*переходим в список ощибок*/
                someEventListener = (onEventListener) getActivity();
                someEventListener.onOpenFragmentClass(InfoFragment.class);
                break;
            default:
                break;
        }
    }

    /* загружаем файлы с сервера*/
    public void dowloadFilesOfServer() {

       /* список шаблонов пути к серверу  */
        String[] templateWay = getResources().getStringArray(R.array.template_way);
        /**/
        ConstantsUtil.sizeFileLine = 0;
        //класс работает с настройками программы
        WorkSharedPreferences lWorkSharedPreferences = new WorkSharedPreferences(mSettings, getActivity());

        /* получаем количество всех строк в файлах до загрузки всех файлов */
        WorkFiles.getSizeLine(
                lWorkSharedPreferences.getWayCatalog(),
                lWorkSharedPreferences.getIdServer() + templateWay[0],
                lWorkSharedPreferences.getMloginServer(),
                lWorkSharedPreferences.getPasswordServer(),
                (MainActivity) getActivity(),
                getImageViewInfo());

        /*если количество = 0 тогда возврат нет  смысла продолжать*/
        if (ConstantsUtil.sizeFileLine > 0) {
            getProgressPieView().setMax(ConstantsUtil.sizeFileLine);
        } else {
            InfoUtil.showErrorAlertDialog(getString(R.string.eror_conect), getString(R.string.updata), getActivity());
            getUi_bar().setVisibility(View.INVISIBLE);
            return;
        }
        //Log
        InfoUtil.setmLogLine(getString(R.string.start_load));
        getLoadFiles().setText(getString(R.string.start_load));
         /*подключаемся к серверу*/
        ConnectServer сonnectServer = new ConnectServer(getActivity(), (byte) 0);
        /*подключились к базе или нет*/
        boolean lConnect = сonnectServer.isMlConnect();
        //
        if (!lConnect) {
            //Log
            InfoUtil.setmLogLine(getString(R.string.action_conect_base), true, TEG + getString(R.string.error_login_password_inet));
            InfoUtil.getFleshImage(R.mipmap.ic_info_red, R.anim.scale_image, getImageViewInfo(), (MainActivity) getActivity());
            getUi_bar().setVisibility(View.INVISIBLE);
            return;
        }
        /* удаляем все записи из таблиц */
        SQLiteOpenHelperUtil.onDeleteValueTables(getDb());
        /*обнуляем все значения перед загрузкой*/
        nullableValues();
         /*получаем команды для записи новой строки в таблице*/
        lTableNameInsert = WorkFiles.getFileNameInsert();
        /*получаем заголовки таблиц*/
        lTableName = WorkFiles.getFileNameHeader();

       /* список файлов для загрузки */
        String[] nameFile = getResources().getStringArray(R.array.name_file_data);
        /*определяем кол-во файлов*/
        AcountNameFile = nameFile.length;

        nOSeek = getnOSeek();
        mProgress = getProgress();
        mProgressDiscrete = getProgressDiscrete();

        /*получаем параметры подключения*/
        AsyncHttpClientUtil utilAsyncHttpClient = сonnectServer.getAsyncHttpClientUtil();
        RequestParams params;
        
        /* начинаем транзакцию */
        getDb().beginTransaction();
        for (String i : nameFile) {

            params = new RequestParams();
            params.put(getString(R.string.NameCatalog), lWorkSharedPreferences.getWayCatalog());
            params.put(getString(R.string.name_file), i);
            params.put(getString(R.string.SizeFileCatalog), "");

            //Log
            InfoUtil.setmLogLine(getString(R.string.action_download_file), i);

            try {
                /* загружаем файл */
                utilAsyncHttpClient.getDownloadFiles(params, i);
            } catch (Exception e) {
                e.printStackTrace();
                //Log
                InfoUtil.setmLogLine(getString(R.string.action_download_file), i, true, TEG + ": " + e.toString());
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
        try {
            downloadAsyncFile.execute();
        } catch (Exception e) {
            //Log
            InfoUtil.setmLogLine(getString(R.string.load_info_base), true, TEG + ": " + fileName + "  " + e.toString());
        }
    }

    public class DownloadAsyncFile extends AsyncTask<String, Integer, String> {

        private final String TEG = FilesLoadFragment.class.getSimpleName();
        private String mCvsSplitBy = ",\"";
        private String mNameTable;
        Handler handlerTotalLinesFile = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                /*выставляем макс и минимум прогресса*/
                int totalLinesFileSeekBar = bundle.getInt(getString(R.string.totalLinesFile));
                getDiscreteSeekBar().setMax(totalLinesFileSeekBar);
                /**/
                getLoadFiles().setText(getString(R.string.load) + mNameTable + "...");
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
                totalLinesFile = WorkFiles.getCountFileLines(mFile);
                stotalLinesFile = totalLinesFile;
                mProgressDiscrete = 0;
                /*отсылаем сообщения прогрессу*/
                final Message msgTotalLinesFile = handlerTotalLinesFile.obtainMessage();
                final Bundle bundleTotalLinesFile = new Bundle();
                bundleTotalLinesFile.putInt(getString(R.string.totalLinesFile), totalLinesFile);
                msgTotalLinesFile.setData(bundleTotalLinesFile);
                handlerTotalLinesFile.sendMessage(msgTotalLinesFile);

                /*создаем строку команды для базы данных в базу*/
                StringBuilder sql = new StringBuilder();

                int n = 0;/*счетчик*/
                int nDSeek = 0;/*счетчик*/
                /**/
                String line = null;
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
                InfoUtil.setmLogLine(getString(R.string.load_table), mNameTable, true, TEG + e.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(final Integer... values) {
            super.onProgressUpdate(values);
            try {

            /* Линия прогресс */
                getDiscreteSeekBar().setProgress(values[0]);
                mProgressDiscrete += (100 / (double) stotalLinesFile);
                getTextProgress().setText(String.valueOf((int) mProgressDiscrete) + "%");
            /* Круг прогресс */

                getProgressPieView().setProgress(values[1]);
                mProgress += (100 / (double) ConstantsUtil.sizeFileLine);
                getProgressPieView().setText(String.valueOf((int) mProgress) + "%");
            } catch (Exception e) {
                InfoUtil.setmLogLine(getString(R.string.load_table), mNameTable, true, TEG + e.toString());
            }
        }
        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);
            //Log
            InfoUtil.setmLogLine(getString(R.string.load_table), mNameTable + getString(R.string.load_end_1));
             /*счетчик файлов*/
            ConstantsUtil.nPieViewProgress++;

             /*если загрузился последний файл*/
            if (ConstantsUtil.nPieViewProgress == AcountNameFile) {

               /*считаем разницу между то что пришли данные из 1с и то что реально загрузилось*/
                int d = ConstantsUtil.sizeFileLine - nOSeek;
                /* Круг прогресс */
                getProgressPieView().setProgress(nOSeek + d);
                mProgress += (100 / (double) ConstantsUtil.sizeFileLine) * d;
                getProgressPieView().setText(String.valueOf((int) mProgress) + "%");
                /**/
                getLoadFiles().setText(R.string.load_end_2);
                //Log
                InfoUtil.setmLogLine(getString(R.string.load_end_2));
                getTextProgress().setText((int) mProgress + "%");

                /*мигаем иконкой для вывода лога*/
                if (InfoUtil.isErrors) {
                    InfoUtil.getFleshImage(R.mipmap.ic_info_red, R.anim.scale_image, getImageViewInfo(), (MainActivity) getActivity());
                } else {
                    InfoUtil.getFleshImage(R.mipmap.ic_info_ok, R.anim.scale_image, getImageViewInfo(), (MainActivity) getActivity());
                }
                getButtonOrderList().setVisibility(View.VISIBLE);
                getUi_bar().setVisibility(View.INVISIBLE);
            }
        }
    }
}

