package ua.com.it_st.ordersmanagers.fragmets;


import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.models.OrderDoc;
import ua.com.it_st.ordersmanagers.sqlTables.TableCounteragents;
import ua.com.it_st.ordersmanagers.sqlTables.TableOrders;
import ua.com.it_st.ordersmanagers.sqlTables.TableOrdersLines;
import ua.com.it_st.ordersmanagers.utils.AsyncHttpClientUtil;
import ua.com.it_st.ordersmanagers.utils.SQLQuery;
import ua.com.it_st.ordersmanagers.utils.SQLiteOpenHelperUtil;

public class UnloadFilesFragment extends FilesFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
         /* создаем лоадер для чтения данных */
        return super.onCreateView(inflater, container, savedInstanceState);
        // getActivity().getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.load_files_button:

                getActivity().getSupportLoaderManager().destroyLoader(0);
                getActivity().getSupportLoaderManager().initLoader(0, null, this);

                break;
//            case R.id.load_files_image_button_n:
//                /*переходим в журанл заказаов*/
//                final onEventListener someEventListener = (onEventListener) getActivity();
//                someEventListener.onOpenFragmentClass(OrderListFragment.class);
//                break;
            default:
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        /* выходим из загрузчкика*/
        getActivity().getSupportLoaderManager().destroyLoader(0);
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        return new MyCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {

        switch (loader.getId()) {
            case 0:
                  /*формируем документ заказ шапку*/
                try {
                    getAllOrdersHeader(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 1:/*режим редактирование*/
                   /*Заполняем корзину*/
                //  onFillCart(data);
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {

    }

    /*получаем все заказы (шапку заказов)*/
    private void getAllOrdersHeader(Cursor itemCursor) throws IOException {

        //BufferedWriter input = new BufferedWriter(new FileReader(mFile));

        String path = "data/data/ua.com.it_st.ordersmanagers/";
        CSVWriter writer = new CSVWriter(new FileWriter(path + "doc_orders.csv"), ',');
        writer.writeNext(TableOrders.sHeader.split(","));

        final String[] arrayColumnNames = itemCursor.getColumnNames();

        while (itemCursor.moveToNext()) {

            final String[] entries = new String[arrayColumnNames.length];

            for (byte i = 0; i < arrayColumnNames.length; i++) {
                int cIndex = itemCursor.getColumnIndex(arrayColumnNames[i]);
                entries[i] = itemCursor.getString(cIndex);
            }

            writer.writeNext(entries);
        }
        writer.flush();
        writer.close();
        /**/
        // UnloadAsyncFile unloadAsyncFile = new UnloadAsyncFile();
        ////unloadAsyncFile.execute();
        Object[] data = connectServer();

        AsyncHttpClientUtil utilAsyncHttpClient = (AsyncHttpClientUtil) data[1];
        try {
            utilAsyncHttpClient.postUnloadFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* создаем класс для загрузки данных в дерево товаров из БД
               * загрузка происходит в фоне */
    private static class MyCursorLoader extends CursorLoader {

        public MyCursorLoader(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {

            return getDb()
                    .rawQuery(SQLQuery.queryOrdersFilesCsv("Orders._id  <> ?"), new String[]{"null"});
        }
    }

    public class UnloadAsyncFile extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(final String... params) {

            return null;
        }
    }

}
