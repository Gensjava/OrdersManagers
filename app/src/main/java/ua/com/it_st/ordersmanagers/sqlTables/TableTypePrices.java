package ua.com.it_st.ordersmanagers.sqlTables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Gens on 27.04.2015.
 */
public class TableTypePrices {
    public static final String TABLE_NAME = "ViewPrices";
    public static final String FILE_NAME = "NameFile=ref_pricecategories.csv";

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_KOD = "kod";

    private static final String TAG = TableTypePrices.class.getSimpleName();

    public static void createTable(final SQLiteDatabase db) {
        Log.i(TAG, "createTable");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + BaseColumns._ID + " integer PRIMARY KEY AUTOINCREMENT"
                + " ," + COLUMN_NAME + " text"
                + " ," + COLUMN_KOD + " text"
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
