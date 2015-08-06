package ua.com.it_st.ordersmanagers.fragmets;

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
import android.widget.ImageView;
import android.widget.ListView;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.sqlTables.TableCounteragents;
import ua.com.it_st.ordersmanagers.utils.SQLiteOpenHelperUtil;

/**
 * Created by Gens on 30.07.2015.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private static SQLiteDatabase DB;
    private ListView lvData;
    private SimpleCursorAdapter scAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.main_header_list, container,
                false);

//        // открываем подключение к БД
//        DB = SQLiteOpenHelperUtil.getInstance().getDatabase();
//
//        // формируем столбцы сопоставления
//        String[] from = new String[]{TableCounteragents.COLUMN_NAME};
//        int[] to = new int[]{R.id.main_list_item_text_client};
//
//        // создааем адаптер и настраиваем список
//        scAdapter = new SimpleCursorAdapter(getActivity(), R.layout.main_list_item, null, from, to, 0);
//        lvData = (ListView) rootView.findViewById(R.id.main_heander_list_position);
//        lvData.setAdapter(scAdapter);
//
//        // добавляем контекстное меню к списку
//        registerForContextMenu(lvData);
//
//        // создаем лоадер для чтения данных
//        getActivity().getSupportLoaderManager().initLoader(0, null, this);
        //
        ImageView imViewAdd = (ImageView) rootView.findViewById(R.id.main_heander_image_plus);
        imViewAdd.setOnClickListener(this);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        return new MyCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        scAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {

    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {

            case R.id.main_heander_image_plus:
                final onEventListener someEventListener = (onEventListener) getActivity();
                someEventListener.someEvent(OrderNewHeaderFragment.class);
                break;
            default:
                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // закрываем подключение при выходе
        DB.close();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().getSupportLoaderManager().destroyLoader(0);
    }

    public interface onEventListener {
        public void someEvent(Class<?> tClass);
    }

    private static class MyCursorLoader extends CursorLoader {

        public MyCursorLoader(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {
            return DB
                    .query(TableCounteragents.TABLE_NAME, // table name
                            null, // columns
                            null, // selection
                            null, // selectionArgs
                            null, // groupBy
                            null, // having
                            null);// orderBy
        }

    }

}
