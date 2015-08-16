package ua.com.it_st.ordersmanagers.sqlTables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import ua.com.it_st.ordersmanagers.enums.DocTypeEnum;
import ua.com.it_st.ordersmanagers.models.Order;
import ua.com.it_st.ordersmanagers.utils.ConstantsUtil;

public class TableOrders {
    public static final String TABLE_NAME = "Orders";

    public static final String COLUMN_VIEW_ID = "view_id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_COMPLETED = "completed";
    public static final String COLUMN_AGENT_ID = "agent_id";
    public static final String COLUMN_COMPANY_ID = "company_id";
    public static final String COLUMN_STORE_ID = "store_id";
    public static final String COLUMN_CLIENT_ID = "client_id";
    public static final String COLUMN_PRICE_CATEGORY_ID = "price_id";
    public static final String COLUMN_TOTAL = "total";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_TIME = "time";

    private static final String TAG = TableOrders.class.getSimpleName();

    public static void createTable(final SQLiteDatabase db) {
        Log.i(TAG, "createTable");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + BaseColumns._ID + " integer PRIMARY KEY AUTOINCREMENT"
                + " ," + COLUMN_VIEW_ID + " integer"
                + " ," + COLUMN_TYPE + " text"
                + " ," + COLUMN_DATE + " text"
                + " ," + COLUMN_NUMBER + " text"
                + " ," + COLUMN_COMPLETED + " text"
                + " ," + COLUMN_AGENT_ID + " integer"
                + " ," + COLUMN_COMPANY_ID + " integer"
                + " ," + COLUMN_STORE_ID + " integer"
                + " ," + COLUMN_CLIENT_ID + " integer"
                + " ," + COLUMN_PRICE_CATEGORY_ID + " integer"
                + " ," + COLUMN_TOTAL + " real"
                + " ," + COLUMN_NOTE + " text"
                + " ," + COLUMN_TIME + " text"
                + ");");
    }

    public static void upgradeTable(final SQLiteDatabase db,
                                    final int oldVersion, final int newVersion) {
        Log.i(TAG, "upgradeTable, old: " + oldVersion + ", new: " + newVersion);

    }

    public static ContentValues getContentValues(Order sData) {

        final ContentValues data = new ContentValues();

        data.put(COLUMN_VIEW_ID, sData.getId());
        data.put(COLUMN_TYPE, DocTypeEnum.HELD.toString());
        data.put(COLUMN_DATE, ConstantsUtil.getDate());
        data.put(COLUMN_NUMBER, ConstantsUtil.getNumberOrder());
        data.put(COLUMN_COMPLETED, "");
        data.put(COLUMN_AGENT_ID, ConstantsUtil.getIdAgent());
        data.put(COLUMN_COMPANY_ID, sData.getFirmId());
        data.put(COLUMN_STORE_ID, sData.getStoreId());
        data.put(COLUMN_CLIENT_ID, sData.getClientId());
        data.put(COLUMN_PRICE_CATEGORY_ID, sData.getPriceCategoryId());
        data.put(COLUMN_TOTAL, ConstantsUtil.getTotalOrder());
        data.put(COLUMN_NOTE, sData.getNote());
        data.put(COLUMN_TIME, ConstantsUtil.getTime());

        return data;
    }

    public static void onDeleteValueTable(final SQLiteDatabase db) {
        Log.i(TAG, "DeleteTable");
        db.execSQL("DELETE FROM " + TABLE_NAME + ";");
    }
}
