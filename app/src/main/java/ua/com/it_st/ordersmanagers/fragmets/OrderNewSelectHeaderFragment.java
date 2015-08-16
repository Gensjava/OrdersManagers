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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.HashMap;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.sqlTables.TableOrders;
import ua.com.it_st.ordersmanagers.sqlTables.TablePrices;
import ua.com.it_st.ordersmanagers.utils.SQLiteOpenHelperUtil;

/**
 * Created by Gens on 07.08.2015.
 */
public class OrderNewSelectHeaderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static SQLiteDatabase DB;
    private static String nameTable;
    private SimpleCursorAdapter scAdapter;
    private OnFragmentSelectListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.main_list, container,
                false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            nameTable = bundle.getString(OrderNewHeaderFragment.NAME_TABLE);
        }

        if (DB == null) {
            // открываем подключение к БД
            DB = SQLiteOpenHelperUtil.getInstance().getDatabase();
        }

        // формируем столбцы сопоставления
        String[] from = new String[]{getString(R.string.name)};
        int[] to = new int[]{R.id.order_new_select_header_item_text};

        // создааем адаптер и настраиваем список
        scAdapter = new SimpleCursorAdapter(getActivity(), R.layout.order_new_select_header_item, null, from, to, 0);
        final ListView lvData = (ListView) rootView.findViewById(R.id.lvMain);
        lvData.setAdapter(scAdapter);

        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {

                Cursor itemCursor = (Cursor) adapterView.getItemAtPosition(i);
                String cName = itemCursor.getString(itemCursor.getColumnIndex(getString(R.string.name)));
                String cKod = itemCursor.getString(itemCursor.getColumnIndex(getString(R.string.kod)));

                String[] cData = new String[2];
                cData[0] = cName;
                cData[1] = cKod;

                // Посылаем данные Activity
                mListener.OnFragmentSelectListener(cData);
                getActivity().onBackPressed();

            }
        });

        // создаем лоадер для чтения данных
        getActivity().getSupportLoaderManager().initLoader(0, null, this);
        //

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        return new MyCursorLoader(getActivity());
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

    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().getSupportLoaderManager().destroyLoader(0);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentSelectListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " должен реализовывать интерфейс OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentSelectListener {

        void OnFragmentSelectListener(String[] link);
    }

    private static class MyCursorLoader extends CursorLoader {

        public MyCursorLoader(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {
            return DB
                    .query(nameTable, // table name
                            null, // columns
                            null, // selection
                            null, // selectionArgs
                            null, // groupBy
                            null, // having
                            null);// orderBy
        }

    }
}
