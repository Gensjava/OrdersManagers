package ua.com.it_st.ordersmanagers.fragmets;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.filippudak.ProgressPieView.ProgressPieView;
import com.loopj.android.http.RequestParams;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.apache.http.auth.AuthScope;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.models.OrderDoc;
import ua.com.it_st.ordersmanagers.sqlTables.TableCompanies;
import ua.com.it_st.ordersmanagers.sqlTables.TableCounteragents;
import ua.com.it_st.ordersmanagers.sqlTables.TableGoodsByStores;
import ua.com.it_st.ordersmanagers.sqlTables.TableOrders;
import ua.com.it_st.ordersmanagers.sqlTables.TableOrdersLines;
import ua.com.it_st.ordersmanagers.sqlTables.TablePrices;
import ua.com.it_st.ordersmanagers.sqlTables.TableProducts;
import ua.com.it_st.ordersmanagers.sqlTables.TableTypePrices;
import ua.com.it_st.ordersmanagers.sqlTables.TableTypeStores;
import ua.com.it_st.ordersmanagers.utils.AsyncHttpClientUtil;
import ua.com.it_st.ordersmanagers.utils.ConstantsUtil;
import ua.com.it_st.ordersmanagers.utils.ErrorInfo;
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

public class LoadFilesFragment extends Fragment implements View.OnClickListener {

    private final String TEG = LoadFilesFragment.class.getSimpleName();
    private SQLiteDatabase mDb;
    private SharedPreferences mSettings;
    private ProgressPieView mProgressPieView;
    private DiscreteSeekBar mDiscreteSeekBar;
    private ImageView mButtonOrderList;
    private TextView mTextProgress;
    private TextView mLoadFiles;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.load_files, container,
                false);

        final Button BHost = (Button) rootView.findViewById(R.id.load_files_button);
        BHost.setBackgroundResource(R.drawable.roundbutton);
        BHost.setOnClickListener(this);
        /* открываем подключение к БД */
        mDb = SQLiteOpenHelperUtil.getInstance().getDatabase();
        /*вызываем менеджера настроек*/
        mSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        /*Круглый общий прогресс загрузки*/
        mProgressPieView = (ProgressPieView) rootView.findViewById(R.id.load_files_progressPieView);
        mProgressPieView.setText("0%");
        mProgressPieView.setTextColor(getResources().getColor(R.color.main_sub_grey));
        mProgressPieView.setProgressColor(getResources().getColor(R.color.main_grey));
        mProgressPieView.setStrokeColor(getResources().getColor(R.color.main_grey));
        /*прогресс каждого файла*/
        mDiscreteSeekBar = (DiscreteSeekBar) rootView.findViewById(R.id.load_files_seekBar);
        /**/
        mButtonOrderList = (ImageView) rootView.findViewById(R.id.load_files_image_button_n);
        mButtonOrderList.setOnClickListener(this);
        /**/
        mTextProgress = (TextView) rootView.findViewById(R.id.load_files_text_progress);
            /**/
        mLoadFiles = (TextView) rootView.findViewById(R.id.load_files_text);

        return rootView;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.load_files_button:

                /* удаляем все записи из таблиц */
                onDeleteValueTables();
                /*загружаем файлы с сервера*/
                dowloadFilesOfServer();

                break;
            case R.id.load_files_image_button_n:
                final onEventListener someEventListener = (onEventListener) getActivity();
                someEventListener.onOpenFragmentClass(OrderListFragment.class);
                break;
            default:
                break;
        }
    }

    /* загружаем файлы с сервера*/
    private void dowloadFilesOfServer() {

       /* список файлов для загрузки */
        String[] nameFile = getResources().getStringArray(R.array.name_file_data);
        ConstantsUtil.nPieViewProgress = 0;
        ConstantsUtil.nPieViewdProgress = 0;
        mProgressPieView.setMax(7);
        mProgressPieView.setProgress(0);
        mProgressPieView.setText("0%");
        mButtonOrderList.setVisibility(View.INVISIBLE);

        /* текущий новый заказ */
        ConstantsUtil.mCurrentOrder = new OrderDoc();
        /* ТЧ заказа */
        ConstantsUtil.mCart = new LinkedHashSet<>();
       /*текущий номер заказа*/
        ConstantsUtil.sCurrentNumber = 0;
       /*режим заказа новый или не новый*/
        ConstantsUtil.modeNewOrder = true;

        //Log
        ErrorInfo.setmLogLine("Начало загрузки");

        /* подключаемся через HTTP к базе и загужаем данные */
        AsyncHttpClientUtil utilAsyncHttpClient = null;
        boolean lConnect;
        /*данные настроек*/
        String loginServer = mSettings.getString(getActivity().getString(R.string.login_server), null);
        String passwordServer = mSettings.getString(getActivity().getString(R.string.password_server), null);
        /*проверка*/
        if (loginServer == null) {
            ErrorInfo.Tost("Введите логин!", getActivity());
            return;
        }
        if (passwordServer == null) {
            ErrorInfo.Tost("Введите пароль!", getActivity());
            return;
        }
        /*режим сервера*/
        String modeServer = mSettings.getString(getActivity().getString(R.string.mode_server), null);
        /* ид сервер удаленны или локальный*/
        final String idServer;
        if (modeServer.equals(getString(R.string.remoteServer))) {
            idServer = mSettings.getString(getActivity().getString(R.string.id_remote), null);
        } else {
            idServer = mSettings.getString(getActivity().getString(R.string.id_local), null);
        }
        /*каталог на сервере пользователя*/
        String wayCatalog = mSettings.getString(getActivity().getString(R.string.way_catalog), null);

        try {
            utilAsyncHttpClient = new AsyncHttpClientUtil((MainActivity) getActivity(), idServer);
            utilAsyncHttpClient.setBasicAuth(loginServer, passwordServer, AuthScope.ANY);
            utilAsyncHttpClient.setMaxRetriesAndTimeout(3, 3);
            lConnect = true;
        } catch (Exception e) {
            e.printStackTrace();
            lConnect = false;
            //Log
            ErrorInfo.setmLogLine(getString(R.string.action_conect_base), true, TEG + ": " + e.toString());
        }

        /* начинаем загрузку */
        if (lConnect) {

            /* начинаем транзакцию */
            mDb.beginTransaction();
            for (String i : nameFile) {
                RequestParams params = new RequestParams();
                params.put(getString(R.string.NameCatalog), wayCatalog);
                params.put(getString(R.string.name_file), i);
                //params.put("login", "admin");
                //params.put("password", "123");
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
            mDb.setTransactionSuccessful();
            mDb.endTransaction();

        } else {
            //Log
            ErrorInfo.setmLogLine(getString(R.string.action_conect_base), true, TEG + getString(R.string.error_login_password_inet));
        }

    }

    public void onInsertTable(final File file,
                              final String fileName
    ) throws IOException {
        Map<String, String> lTableNameInsert = new HashMap<>();

        lTableNameInsert.put(TableCompanies.FILE_NAME, TableCompanies.INSERT_VALUES);
        lTableNameInsert.put(TableCounteragents.FILE_NAME, TableCounteragents.INSERT_VALUES);
        lTableNameInsert.put(TablePrices.FILE_NAME, TablePrices.INSERT_VALUES);
        lTableNameInsert.put(TableProducts.FILE_NAME, TableProducts.INSERT_VALUES);
        lTableNameInsert.put(TableTypePrices.FILE_NAME, TableTypePrices.INSERT_VALUES);
        lTableNameInsert.put(TableTypeStores.FILE_NAME, TableTypeStores.INSERT_VALUES);
        lTableNameInsert.put(TableGoodsByStores.FILE_NAME, TableGoodsByStores.INSERT_VALUES);

        Map<String, String> lTableName = new HashMap<>();

        lTableName.put(TableCompanies.FILE_NAME, TableCompanies.HEADER_NAME);
        lTableName.put(TableCounteragents.FILE_NAME, TableCounteragents.HEADER_NAME);
        lTableName.put(TablePrices.FILE_NAME, TablePrices.HEADER_NAME);
        lTableName.put(TableProducts.FILE_NAME, TableProducts.HEADER_NAME);
        lTableName.put(TableTypePrices.FILE_NAME, TableTypePrices.HEADER_NAME);
        lTableName.put(TableTypeStores.FILE_NAME, TableTypeStores.HEADER_NAME);
        lTableName.put(TableGoodsByStores.FILE_NAME, TableGoodsByStores.HEADER_NAME);

        DownloadAsyncFile downloadAsyncFile = new DownloadAsyncFile(file,
                lTableNameInsert.get(fileName),
                lTableName.get(fileName)
        );
        downloadAsyncFile.execute();

    }

    /* чистим таблицы */
    private void onDeleteValueTables() {

        TableCompanies.onDeleteValueTable(mDb);
        TableCounteragents.onDeleteValueTable(mDb);
        TablePrices.onDeleteValueTable(mDb);
        TableProducts.onDeleteValueTable(mDb);
        TableTypePrices.onDeleteValueTable(mDb);
        TableTypeStores.onDeleteValueTable(mDb);
        TableGoodsByStores.onDeleteValueTable(mDb);
        TableOrders.onDeleteValueTable(mDb);
        TableOrdersLines.onDeleteValueTable(mDb);

    }

    /* создаем класс - интефейс для открытия фрагментов */
    public interface onEventListener {
        void onOpenFragmentClass(Class<?> fClass);

    }

    public class DownloadAsyncFile extends AsyncTask<String, Integer, String> {

        private final String TEG = LoadFilesFragment.class.getSimpleName();

        Handler handlerTotalLinesFile = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                /*выставляем макс и минимум прогресса*/
                int totalLinesFileSeekBar = bundle.getInt("totalLinesFile");
                mDiscreteSeekBar.setMax(totalLinesFileSeekBar);
                mDiscreteSeekBar.setMin(0);
            }
        };
        private String mCvsSplitBy = ",\"";
        private String mNameTable;
        private String mLineInsert;
        private File mFile;
        private int limitInsert = 100;
        private int totalLinesFile;

        public DownloadAsyncFile(final File file, final String lineInsert,
                                 final String nameTable) {
            mLineInsert = lineInsert;
            mNameTable = nameTable;
            mFile = file;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(final String... files) {

            try {
                /* Считываем по одной строке */
                BufferedReader input = new BufferedReader(new FileReader(mFile));
                /*вычисляем к-во строк в файле*/
                totalLinesFile = getCountFileLines(mFile);

                /*отсылаем сообщения прогрессу*/
                Message msgTotalLinesFile = handlerTotalLinesFile.obtainMessage();
                Bundle bundleTotalLinesFile = new Bundle();
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
                    publishProgress(++nDSeek);

                    /*делаем добавления пачки строк в базу при выполнеии условий*/
                    if (n == limitInsert || n == totalLinesFile) {
                        sql.append("");
                        mDb.execSQL(mLineInsert + sql);
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

            mDiscreteSeekBar.setProgress(values[0]);
            mTextProgress.setText(String.valueOf(mDiscreteSeekBar.getProgress()));
            mLoadFiles.setText("Загрузка " + mNameTable + "...");

        }

        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);
            mProgressPieView.setProgress(++ConstantsUtil.nPieViewProgress);

            double sProgress = ConstantsUtil.nPieViewdProgress += 14.29;
            mProgressPieView.setText(String.valueOf((int) sProgress) + "%");

            if (ConstantsUtil.nPieViewProgress == 7) {
                mButtonOrderList.setVisibility(View.VISIBLE);
                mLoadFiles.setText("Загрузка завершена!");
            }
            //Log
            ErrorInfo.setmLogLine("Загрузка в таблицу завершена", mNameTable);
                                    /* заканчиваем транзакцию */

        }

        /*получаем кол-во строк в файле*/
        private int getCountFileLines(File mFile) {
            int n = 0;
            BufferedReader input = null;
            try {
                input = new BufferedReader(new FileReader(mFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                input.readLine();
                while ((input.readLine()) != null) {
                    n++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return n;
        }

    }


}

