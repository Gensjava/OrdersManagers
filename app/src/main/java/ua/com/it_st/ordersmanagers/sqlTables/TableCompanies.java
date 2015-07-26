package ua.com.it_st.ordersmanagers.sqlTables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import ua.com.it_st.ordersmanagers.utils.UtilSQLiteOpenHelper;

/**
 * Created by Gens on 19.07.2015.
 */
public class TableCompanies {
    public static final String TABLE_NAME = "Сompanys";
    public static final String FILE_NAME = "NameFile=ref_firms.csv";

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_NAME_FULL = "name_full";
    public static final String COLUMN_KOD = "kod";

    private static final String TAG = TableCompanies.class.getSimpleName();

    public static void createTable(final SQLiteDatabase db) {
        Log.i(TAG, "createTable");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + BaseColumns._ID + " integer PRIMARY KEY AUTOINCREMENT"
                + " ," + COLUMN_NAME + " text"
                + " ," + COLUMN_KOD + " text"
                + " ," + COLUMN_NAME_FULL + " text"
                + ");");
    }

    public static void upgradeTable(final SQLiteDatabase db,
                                    final int oldVersion, final int newVersion) {
        Log.i(TAG, "upgradeTable, old: " + oldVersion + ", new: " + newVersion);

    }

    public static void onInsert(String sData[], SQLiteDatabase db) {

        final ContentValues data = new ContentValues();

        data.put(COLUMN_KOD, sData[0]);
        data.put(COLUMN_NAME, sData[1]);
        data.put(COLUMN_NAME_FULL, sData[1]);

        db.beginTransaction();
        try {
            db.insert(TABLE_NAME, null, data);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

    }

    public static void onDeleteValueTable(final SQLiteDatabase db) {
        Log.i(TAG, "DeleteTable");
        db.execSQL("DELETE FROM " + TABLE_NAME + ";");
    }
}
