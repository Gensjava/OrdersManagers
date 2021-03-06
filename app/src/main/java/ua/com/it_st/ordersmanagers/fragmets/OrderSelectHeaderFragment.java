package ua.com.it_st.ordersmanagers.fragmets;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.models.Catalogs;
import ua.com.it_st.ordersmanagers.models.Counteragents;
import ua.com.it_st.ordersmanagers.sqlTables.TableCounteragents;
import ua.com.it_st.ordersmanagers.sqlTables.TableCounteragentsDebt;
import ua.com.it_st.ordersmanagers.utils.SQLQuery;
import ua.com.it_st.ordersmanagers.utils.SQLiteOpenHelperUtil;

/*Класс предназначен для выбора значений из списка для оформления шапки
 документа заказа
  */
public class OrderSelectHeaderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static SQLiteDatabase sDb;
    private static String nameTable;
    private SimpleCursorAdapter scAdapter;
    private OnFragmentSelectListener mListener;
    private boolean isContragents;
    private String nameTegFragment;
    private String idPoisson;
    private Class<?> nameClassSelect;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.main_list, container,
                false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            /*получаем имя таблицы и подставляем в запрос*/
            nameTable = bundle.getString(HeaderDoc.NAME_TABLE);
            nameTegFragment = bundle.getString(HeaderDoc.NAME_TAG);
            try {
                nameClassSelect = Class.forName(bundle.getString(HeaderDoc.NAME_CLASS));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            idPoisson = bundle.getString(HeaderDoc.ID_POSITION);

            /*если это таблица клиентов тогда нам нужен суб текст
                        *  * чтоб можно было физ.адресс показать*/
            if (nameTable != null) {
                isContragents = nameTable.equals(TableCounteragents.TABLE_NAME);
            }

        }

        /* открываем подключение к БД */
        sDb = SQLiteOpenHelperUtil.getInstance().getDatabase();

        /* формируем столбцы сопоставления */
        final String[] from;
        final int[] to;

        if (!isContragents) {
            from = new String[]{getString(R.string.name)};
            to = new int[]{R.id.order_new_select_header_item_text};
            // создааем адаптер и настраиваем список
            scAdapter = new SimpleCursorAdapter(getActivity(), R.layout.order_new_select_header_item, null, from, to, 0);

            /* создаем лоадер для чтения данных */
            getActivity().getSupportLoaderManager().initLoader(0, null, this);
        } else {
            from = new String[]{getString(R.string.name), TableCounteragents.COLUMN_ADDRESS, TableCounteragentsDebt.COLUMN_DEBT};
            to = new int[]{R.id.order_new_select_header_sub_item_text, R.id.order_new_select_header_sub_sub_item_text, R.id.order_new_select_header_sub_item_text_debt};
            // создааем адаптер и настраиваем список
            scAdapter = new SimpleCursorAdapter(getActivity(), R.layout.order_new_select_header_sub_item, null, from, to, 0);

             /* создаем лоадер для чтения данных */
            getActivity().getSupportLoaderManager().initLoader(1, null, this);
        }

        /**/
        final ListView lvData = (ListView) rootView.findViewById(R.id.lvMain);
        lvData.setAdapter(scAdapter);

        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {

                Cursor itemCursor = (Cursor) adapterView.getItemAtPosition(i);
                String cName = itemCursor.getString(itemCursor.getColumnIndex(getString(R.string.name)));
                String cKod = itemCursor.getString(itemCursor.getColumnIndex(getString(R.string.kod)));

                Object catalogs = null;

                if (!isContragents) {
                    try {
                        catalogs = Class.forName(nameClassSelect.getName()).newInstance();
                        ((Catalogs) catalogs).setKod(cKod);
                        ((Catalogs) catalogs).setName(cName);

                    } catch (java.lang.InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    String cAdres = itemCursor.getString(itemCursor.getColumnIndex(TableCounteragents.COLUMN_ADDRESS));
                    catalogs = new Counteragents(cKod, cName, cAdres);
                }

                /* Посылаем данные Activity */
                mListener.OnFragmentSelectListener((Catalogs) catalogs, nameTegFragment, idPoisson);
                getActivity().onBackPressed();
            }
        });
        return rootView;
    }
    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        switch (id) {
            case 0:/*получаем все заазы*/
                return new MyCursorLoader(getActivity());
            case 1:/*получаем сумму всех заазов*/
                return new MyCursorLoaderDebt(getActivity());
            default:
                return null;
        }
    }
    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        if (data.isClosed()) {
            // error
        } else {
            scAdapter.swapCursor(data);
        }

    }
    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        scAdapter.swapCursor(null);
    }
    @Override
    public void onPause() {
        super.onPause();

        if (!isContragents) {
             /* создаем лоадер для чтения данных */
            getActivity().getSupportLoaderManager().destroyLoader(0);
        } else {
             /* создаем лоадер для чтения данных */
            getActivity().getSupportLoaderManager().destroyLoader(1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
         /* открываем подключение к БД */
        if (sDb == null) {
            sDb = SQLiteOpenHelperUtil.getInstance().getDatabase();
        }
        if (!isContragents) {
             /* создаем лоадер для чтения данных */
            getActivity().getSupportLoaderManager().initLoader(0, null, this);
                 /* обновляем курсор */
            getActivity().getSupportLoaderManager().getLoader(0).forceLoad();
        } else {
             /* создаем лоадер для чтения данных */
            getActivity().getSupportLoaderManager().initLoader(1, null, this);
                 /* обновляем курсор */
            getActivity().getSupportLoaderManager().getLoader(1).forceLoad();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentSelectListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + activity.getString(R.string.error_interfice));
        }
    }

    public interface OnFragmentSelectListener {

        void OnFragmentSelectListener(Catalogs link, String nameTegFragment, String id);
    }

    /* создаем класс для загрузки данных из БД общие данные
    * загрузка происходит в фоне */
    private static class MyCursorLoader extends CursorLoader {

        public MyCursorLoader(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {
            return sDb
                    .query(nameTable, // table name
                            null, // columns
                            null, // selection
                            null, // selectionArgs
                            null, // groupBy
                            null, // having
                            null);// orderBy
        }

    }

    /* создаем класс для загрузки данных из БД долг клиентов
* загрузка происходит в фоне */
    private static class MyCursorLoaderDebt extends CursorLoader {

        public MyCursorLoaderDebt(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {
            return sDb
                    .rawQuery(SQLQuery.queryCounteragentsDebt("Counteragents.name  <> ?"), new String[]{"null"});
        }

    }
}
