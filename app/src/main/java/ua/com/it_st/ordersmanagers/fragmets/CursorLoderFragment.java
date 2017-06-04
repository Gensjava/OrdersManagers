package ua.com.it_st.ordersmanagers.fragmets;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.com.it_st.ordersmanagers.utils.SQLiteOpenHelperUtil;

/**
 * Created by Gena on 2017-05-22.
 */

public abstract class CursorLoderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static SQLiteDatabase sDb;
    private String query;
    private byte countLoad;
    private String[] paramsQuery;

    public abstract boolean onRecord();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        sDb = SQLiteOpenHelperUtil.getInstance().getDatabase();

        for (byte x = 0; x < countLoad; x++) {
            getActivity().getSupportLoaderManager().initLoader(x, null, this);
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        /* выходим из загрузчкика*/
        for (byte x = 0; x < countLoad; x++) {
            getActivity().getSupportLoaderManager().destroyLoader(x);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        /* открываем подключение к БД */
        if (sDb == null) {
            sDb = SQLiteOpenHelperUtil.getInstance().getDatabase();
        }

        for (byte x = 0; x < countLoad; x++) {
               /* создаем загрузчик */
            getActivity().getSupportLoaderManager().initLoader(x, null, this);
                /* обновляем курсор */
            getActivity().getSupportLoaderManager().getLoader(x).forceLoad();
        }
    }

    public byte getCountLoad() {
        return countLoad;
    }

    public void setCountLoad(byte countLoad) {
        this.countLoad = countLoad;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String[] getParamsQuery() {
        return paramsQuery;
    }

    public void setParamsQuery(String[] paramsQuery) {
        this.paramsQuery = paramsQuery;
    }
}
