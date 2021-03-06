package ua.com.it_st.ordersmanagers.sqlTables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;


public class TableGoodsByStores {
    public static final String TABLE_NAME = "GoodsByStores";
    public static final String FILE_NAME = "ref_goodsbystores.csv";
    public static final String HEADER_NAME = "остатков товаров";

    public static final String COLUMN_COODS_KOD = "kod_coods";
    public static final String COLUMN_STORES_KOD = "kod_stores";
    public static final String COLUMN_AMOUNT = "amount";

    public static final String INSERT_VALUES = "INSERT INTO " + TABLE_NAME + " ("
            + COLUMN_COODS_KOD + ", "
            + COLUMN_STORES_KOD + ", "
            + COLUMN_AMOUNT
            + ") VALUES ";

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

    public static ContentValues getContentValues(String sData[]) {

        final ContentValues data = new ContentValues();

        data.put(COLUMN_COODS_KOD, sData[0]);
        data.put(COLUMN_STORES_KOD, sData[1]);
        String b = sData[2].replace(',', ' ');
        data.put(COLUMN_AMOUNT, b.trim());

        return data;
    }

    public static void onDeleteValueTable(final SQLiteDatabase db) {
        Log.i(TAG, "DeleteTable");
        db.execSQL("DELETE FROM " + TABLE_NAME + ";");
    }
}
