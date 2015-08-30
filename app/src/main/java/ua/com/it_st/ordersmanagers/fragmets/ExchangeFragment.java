package ua.com.it_st.ordersmanagers.fragmets;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.filippudak.ProgressPieView.ProgressPieView;
import com.loopj.android.http.RequestParams;

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

public class ExchangeFragment extends Fragment implements View.OnClickListener {

    private final String TEG = ExchangeFragment.class.getSimpleName();
    private SQLiteDatabase mDb;
    private SharedPreferences mSettings;
    private ProgressPieView mProgressPieView;

    /*имена таблиц из имен файлов*/
    public static Map getListHashMapTableName() {

        Map<String, String> lTableName = new HashMap<String, String>();

        lTableName.put(TableCompanies.FILE_NAME, TableCompanies.TABLE_NAME);
        lTableName.put(TableCounteragents.FILE_NAME, TableCounteragents.TABLE_NAME);
        lTableName.put(TablePrices.FILE_NAME, TablePrices.TABLE_NAME);
        lTableName.put(TableProducts.FILE_NAME, TableProducts.TABLE_NAME);
        lTableName.put(TableTypePrices.FILE_NAME, TableTypePrices.TABLE_NAME);
        lTableName.put(TableTypeStores.FILE_NAME, TableTypeStores.TABLE_NAME);
        lTableName.put(TableGoodsByStores.FILE_NAME, TableGoodsByStores.TABLE_NAME);

        return lTableName;
    }

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
        /**/
        mProgressPieView = (ProgressPieView) rootView.findViewById(R.id.load_files_progressPieView);
        mProgressPieView.setMax(7);
        mProgressPieView.setText("0%");
        mProgressPieView.setTextColor(getResources().getColor(R.color.main_sub_grey));
        mProgressPieView.setProgressColor(getResources().getColor(R.color.main_grey));
        mProgressPieView.setStrokeColor(getResources().getColor(R.color.main_grey));
        // mProgressPieView.setProgress(ConstantsUtil.n);


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
            default:
                break;
        }
    }

    /* загружаем файлы с сервера*/
    private void dowloadFilesOfServer() {

       /* список файлов для загрузки */
        String[] nameFile = getResources().getStringArray(R.array.name_file_data);

        Map lTableNameInsert;
        lTableNameInsert = getListTableName();
        /**/
        Map lTableName;
        lTableName = getListHashMapTableName();

        /* текущий новый заказ */
        ConstantsUtil.mCurrentOrder = new OrderDoc();
        /* ТЧ заказа */
        ConstantsUtil.mCart = new LinkedHashSet<OrderDoc.OrderLines>();
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
            utilAsyncHttpClient.setBasicAuth(loginServer, passwordServer);
            lConnect = true;
        } catch (Exception e) {
            e.printStackTrace();
            lConnect = false;
            //Log
            ErrorInfo.setmLogLine(getString(R.string.action_conect_base), true, TEG + ": " + e.toString());
        }

        /* начинаем загрузку */
        if (lConnect) {
            int pr = 0;
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
                    utilAsyncHttpClient.getDownloadFiles(params, mDb, lTableNameInsert, lTableName, i, mProgressPieView);
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

    /* получаем список имени файла и команду для создания строки в таблицах базе данных*/
    public Map getListTableName() {

        Map<String, String> lTableName = new HashMap<String, String>();

        lTableName.put(TableCompanies.FILE_NAME, TableCompanies.INSERT_VALUES);
        lTableName.put(TableCounteragents.FILE_NAME, TableCounteragents.INSERT_VALUES);
        lTableName.put(TablePrices.FILE_NAME, TablePrices.INSERT_VALUES);
        lTableName.put(TableProducts.FILE_NAME, TableProducts.INSERT_VALUES);
        lTableName.put(TableTypePrices.FILE_NAME, TableTypePrices.INSERT_VALUES);
        lTableName.put(TableTypeStores.FILE_NAME, TableTypeStores.INSERT_VALUES);
        lTableName.put(TableGoodsByStores.FILE_NAME, TableGoodsByStores.INSERT_VALUES);

        return lTableName;
    }
}

