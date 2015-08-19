package ua.com.it_st.ordersmanagers.fragmets;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.loopj.android.http.RequestParams;

import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.R;
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

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.exchange_layout, container,
                false);
        final TextView exchegeData = (TextView) rootView.findViewById(R.id.exchege_text_data);
        final TextView exchegeStatus = (TextView) rootView.findViewById(R.id.exchege_text_string_status);

        ImageView BHost = (ImageView) rootView.findViewById(R.id.exchege_image_button);
        BHost.setOnClickListener(this);
        /* открываем подключение к БД */
        mDb = SQLiteOpenHelperUtil.getInstance().getDatabase();

        return rootView;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.exchege_image_button:

                /* удаляем все записи из таблиц */
                onDeleteValueTables();
                /* список файлов для загрузки */
                String[] nameFile = getResources().getStringArray(R.array.name_file_data_test);

                //Log
                ErrorInfo.setmLogLine("Начало загрузки");

                /* подключаемся через HTTP к базе и загужаем данные */
                AsyncHttpClientUtil utilAsyncHttpClient = null;
                boolean lConnect;
                try {
                    utilAsyncHttpClient = new AsyncHttpClientUtil((MainActivity) getActivity());
                    utilAsyncHttpClient.setBasicAuth("admin", "123");
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
                        params.put(getString(R.string.name_file), i);
                        //params.put("login", "admin");
                        //params.put("password", "123");
                        //Log
                        ErrorInfo.setmLogLine(getString(R.string.action_download_file), i);

                        try {
                            /* загружаем файл */
                            utilAsyncHttpClient.getDownloadFiles(params, mDb);
                        } catch (Exception e) {
                            e.printStackTrace();
                            //Log
                            ErrorInfo.setmLogLine(getString(R.string.action_download_file), i, true, TEG + ": " + e.toString());
                        }
                    }
                    if (mDb != null) {
                        /* заканчиваем транзакцию */
                        mDb.setTransactionSuccessful();
                        mDb.endTransaction();
                    }
                } else {
                    //Log
                    ErrorInfo.setmLogLine(getString(R.string.action_conect_base), true, TEG + getString(R.string.error_login_password_inet));
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDb.close();
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
}

