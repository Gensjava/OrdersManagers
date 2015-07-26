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

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.http.Header;
import org.apache.http.HttpStatus;

import java.io.File;

import ua.com.it_st.ordersmanagers.MainActivity;
import ua.com.it_st.ordersmanagers.R;

import ua.com.it_st.ordersmanagers.sqlTables.TableCompanies;
import ua.com.it_st.ordersmanagers.sqlTables.TableCounteragents;
import ua.com.it_st.ordersmanagers.sqlTables.TableGoodsByStores;
import ua.com.it_st.ordersmanagers.sqlTables.TableInformations;
import ua.com.it_st.ordersmanagers.sqlTables.TableOrders;
import ua.com.it_st.ordersmanagers.sqlTables.TablePrices;
import ua.com.it_st.ordersmanagers.sqlTables.TableProducts;
import ua.com.it_st.ordersmanagers.sqlTables.TableTasks;
import ua.com.it_st.ordersmanagers.sqlTables.TableTypeInformations;
import ua.com.it_st.ordersmanagers.sqlTables.TableTypeMeasuring;
import ua.com.it_st.ordersmanagers.sqlTables.TableTypeOrders;
import ua.com.it_st.ordersmanagers.sqlTables.TableTypePrices;
import ua.com.it_st.ordersmanagers.sqlTables.TableTypePrioritiesTasks;
import ua.com.it_st.ordersmanagers.sqlTables.TableTypeStores;
import ua.com.it_st.ordersmanagers.sqlTables.TableUsers;
import ua.com.it_st.ordersmanagers.sqlTables.TableСhat;
import ua.com.it_st.ordersmanagers.utils.ErrorInfo;
import ua.com.it_st.ordersmanagers.utils.UtilAsyncHttpClient;
import ua.com.it_st.ordersmanagers.utils.UtilSQLiteOpenHelper;
import ua.com.it_st.ordersmanagers.utils.UtilsWorkFiles;

/**
 * Created by Gens on 19.07.2015.
 */
public class ExchangeFragment extends Fragment implements View.OnClickListener {

    private SQLiteDatabase db;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.exchange_layout, container,
                false);
        final TextView exchegeData = (TextView) rootView.findViewById(R.id.exchege_text_data);
        final TextView exchegeStatus = (TextView) rootView.findViewById(R.id.exchege_text_string_status);

        ImageView BHost = (ImageView) rootView.findViewById(R.id.exchege_image_button);
        db = UtilSQLiteOpenHelper.getInstance().getDatabase();
        BHost.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.exchege_image_button:

                //подключаемся через HTTP к базе и загужаем данные
                UtilAsyncHttpClient utilAsyncHttpClient = new UtilAsyncHttpClient((MainActivity) getActivity());
                utilAsyncHttpClient.setBasicAuth("admin", "123");
                //список файлов
                String[] nameFile = getResources().getStringArray(R.array.name_file_data_test);

                for (String i : nameFile) {

                    RequestParams params = new RequestParams();
                    params.put("NameFile", i.toString());

                    try {
                        //удаляем все записи из таблиц
                        onDeleteValueTables();
                        //загружаем файл
                        utilAsyncHttpClient.getDownloadFiles(params, db);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (db != null) {
                            // db.close();
                        }
                    }
                }
                if (db != null) {
                    // db.close();
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }


    //чистим таблицы 
    private void onDeleteValueTables() {

        TableCompanies.onDeleteValueTable(db);
        TableCounteragents.onDeleteValueTable(db);
        TableInformations.onDeleteValueTable(db);
        TableOrders.onDeleteValueTable(db);
        TablePrices.onDeleteValueTable(db);
        TableProducts.onDeleteValueTable(db);
        TableTasks.onDeleteValueTable(db);
        TableTypeInformations.onDeleteValueTable(db);
        TableTypeMeasuring.onDeleteValueTable(db);
        TableTypeOrders.onDeleteValueTable(db);
        TableTypePrices.onDeleteValueTable(db);
        TableTypePrioritiesTasks.onDeleteValueTable(db);
        TableTypeStores.onDeleteValueTable(db);
        TableUsers.onDeleteValueTable(db);
        TableСhat.onDeleteValueTable(db);
        TableGoodsByStores.onDeleteValueTable(db);

    }
}
