package ua.com.it_st.ordersmanagers.sqlTables;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class TableOrdersLines {
    public static final String TABLE_NAME = "OrdersLines";

    public static final String COLUMN_DOC_ID = "doc_id";
    public static final String COLUMN_GOODS_ID = "goods_id";
    public static final String COLUMN_RATE = "rate";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_PRICE = "price";

    private static final String TAG = TableOrdersLines.class.getSimpleName();

    public static void createTable(final SQLiteDatabase db) {
        Log.i(TAG, "createTable");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + BaseColumns._ID + " integer PRIMARY KEY AUTOINCREMENT"
                + " ," + COLUMN_DOC_ID + " integer"
                + " ," + COLUMN_GOODS_ID + " integer"
                + " ," + COLUMN_RATE + " integer"
                + " ," + COLUMN_AMOUNT + " real"
                + " ," + COLUMN_PRICE + " real"
                + ");");
    }

    public static void upgradeTable(final SQLiteDatabase db,
                                    final int oldVersion, final int newVersion) {
        Log.i(TAG, "upgradeTable, old: " + oldVersion + ", new: " + newVersion);

    }

    public static void onDeleteValueTable(final SQLiteDatabase db) {
        Log.i(TAG, "DeleteTable");
        db.execSQL("DELETE FROM " + TABLE_NAME + ";");
    }
}