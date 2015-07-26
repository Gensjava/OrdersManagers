package ua.com.it_st.ordersmanagers.sqlTables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Gens on 27.04.2015.
 */
public class TableGoodsByStores {
    public static final String TABLE_NAME = "ViewPrices";
    public static final String FILE_NAME = "NameFile=ref_goodsbystores.csv";

    public static final String COLUMN_COODS_KOD = "kod_coods";
    public static final String COLUMN_STORES_KOD = "kod_stores";
    public static final String COLUMN_AMOUNT = "Amount";

    private static final String TAG = TableGoodsByStores.class.getSimpleName();

    public static void createTable(final SQLiteDatabase db) {
        Log.i(TAG, "createTable");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + BaseColumns._ID + " integer PRIMARY KEY AUTOINCREMENT"
                + " ," + COLUMN_COODS_KOD + " text"
                + " ," + COLUMN_STORES_KOD + " text"
                + " ," + COLUMN_AMOUNT + " real"
                + ");");
    }

    public static void upgradeTable(final SQLiteDatabase db,
                                    final int oldVersion, final int newVersion) {
        Log.i(TAG, "upgradeTable, old: " + oldVersion + ", new: " + newVersion);

    }

    public static void onInsert(String sData[], SQLiteDatabase db) {

        final ContentValues data = new ContentValues();

        data.put(COLUMN_COODS_KOD, sData[0]);
        data.put(COLUMN_STORES_KOD, sData[1]);
        data.put(COLUMN_STORES_KOD, sData[2]);

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
