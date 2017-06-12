package ua.com.it_st.ordersmanagers.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.CursorLoader;

/**
 * Created by Gena on 2017-05-14.
 */

public class GlobalCursorLoader extends CursorLoader {

    private String querySQL;
    private SQLiteDatabase sDb;
    private String[] params;

    public GlobalCursorLoader(Context context, String querySQL, String[] params, SQLiteDatabase sDb) {
        super(context);
        this.querySQL = querySQL;
        this.sDb = sDb;
        this.params = params;
    }

    @Override
    public Cursor loadInBackground() {

        return sDb
                .rawQuery(querySQL, params);
    }
}
