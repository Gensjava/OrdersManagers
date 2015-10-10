package ua.com.it_st.ordersmanagers.sqlTables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import ua.com.it_st.ordersmanagers.models.OrderDoc;

public class TableOrdersLines {
    public static final String TABLE_NAME = "OrdersLines";
    public static final String FILE_NAME = "doc_lines.csv";
    public static final String sHeader = "DocId,GoodsId,Rate,Amount,Price,";
    public static final String HEADER_NAME = "табличной части заказов";

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

    public static ContentValues getContentValues(OrderDoc.OrderLines sData, String idDoc) {

        final ContentValues data = new ContentValues();

        data.put(COLUMN_DOC_ID, idDoc);
        data.put(COLUMN_GOODS_ID, sData.getGoodsId());
        data.put(COLUMN_RATE, sData.getRate());
        data.put(COLUMN_AMOUNT, sData.getAmount());
        data.put(COLUMN_PRICE, sData.getPrice());

        return data;
    }


    public static void onDeleteValueTable(final SQLiteDatabase db) {
        Log.i(TAG, "DeleteTable");
        db.execSQL("DELETE FROM " + TABLE_NAME + ";");
    }
}
