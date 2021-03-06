package ua.com.it_st.ordersmanagers.sqlTables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ua.com.it_st.ordersmanagers.enums.DocType;
import ua.com.it_st.ordersmanagers.interfaces.implems.OrderListAction;
import ua.com.it_st.ordersmanagers.models.Orders;
import ua.com.it_st.ordersmanagers.utils.ConstantsUtil;
import ua.com.it_st.ordersmanagers.utils.SQLQuery;

public class TableOrders {
    public static final String TABLE_NAME = "Orders";
    public static final String FILE_NAME = "doc_orders.csv";
    public static final String sHeader = "Id, DocType, DocDate, DocNumber, Completed, AgentId, FirmId, StoreId, ClientId, PriceCategoryId, Total, Note,";
    public static final String HEADER_NAME = "шапка заказов";

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
    public static final String COLUMN_CLIENT_ADRESS = "client_adress";

    private static final String TAG = TableOrders.class.getSimpleName();

    public static void createTable(final SQLiteDatabase db) {
        Log.i(TAG, "createTable");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + BaseColumns._ID + " integer PRIMARY KEY AUTOINCREMENT"
                + " ," + COLUMN_VIEW_ID + " text"
                + " ," + COLUMN_TYPE + " text"
                + " ," + COLUMN_DATE + " text"
                + " ," + COLUMN_NUMBER + " text"
                + " ," + COLUMN_COMPLETED + " text"
                + " ," + COLUMN_AGENT_ID + " text"
                + " ," + COLUMN_COMPANY_ID + " text"
                + " ," + COLUMN_STORE_ID + " text"
                + " ," + COLUMN_CLIENT_ID + " text"
                + " ," + COLUMN_PRICE_CATEGORY_ID + " text"
                + " ," + COLUMN_TOTAL + " real"
                + " ," + COLUMN_NOTE + " text"
                + " ," + COLUMN_TIME + " text"
                + " ," + COLUMN_CLIENT_ADRESS + " text"
                + ");");
    }

    public static void upgradeTable(final SQLiteDatabase db,
                                    final int oldVersion, final int newVersion) {
        Log.i(TAG, "upgradeTable, old: " + oldVersion + ", new: " + newVersion);

    }

    public static ContentValues getContentValues(Orders sData) {

        final ContentValues data = new ContentValues();
        final OrderListAction lUpDateOrderList = new OrderListAction();

        data.put(COLUMN_VIEW_ID, sData.getId());
        data.put(COLUMN_TYPE, DocType.HELD.toString());
        data.put(COLUMN_DATE, sData.getDocDate());
        data.put(COLUMN_NUMBER, sData.getDocNumber());
        data.put(COLUMN_COMPLETED, "");
        data.put(COLUMN_AGENT_ID, sData.getAgent().getKod());
        data.put(COLUMN_COMPANY_ID, sData.getCompany().getKod());
        data.put(COLUMN_STORE_ID, sData.getStore().getKod());
        data.put(COLUMN_CLIENT_ID, sData.getCounteragent().getKod());
        data.put(COLUMN_PRICE_CATEGORY_ID, sData.getTypePrices().getKod());
        data.put(COLUMN_TOTAL, lUpDateOrderList.sum());
        data.put(COLUMN_NOTE, sData.getNote());
        data.put(COLUMN_TIME, ConstantsUtil.getTime());
        data.put(COLUMN_CLIENT_ADRESS, sData.getCounteragent().getAddress());

        return data;
    }

    public static ContentValues getContentValuesUpdata(Orders sData) {

        final ContentValues data = new ContentValues();
        final OrderListAction lUpDateOrderList = new OrderListAction();

        data.put(COLUMN_TOTAL, lUpDateOrderList.sum());
        data.put(COLUMN_COMPANY_ID, sData.getCompany().getKod());
        data.put(COLUMN_STORE_ID, sData.getStore().getKod());
        data.put(COLUMN_CLIENT_ID, sData.getCounteragent().getKod());
        data.put(COLUMN_PRICE_CATEGORY_ID, sData.getTypePrices().getKod());
        data.put(COLUMN_NOTE, sData.getNote());
        data.put(COLUMN_CLIENT_ADRESS, sData.getCounteragent().getAddress());

        return data;
    }

    public static List<Object> getValueForUpload() {

        List<Object> list = new ArrayList<>();
        list.add(FILE_NAME);
        list.add(sHeader.split(","));
        list.add(HEADER_NAME);
        list.add(SQLQuery.queryOrdersHeaderFilesCsv("Orders.type  <> ?"));
        list.add(new String[]{"NO_HELD"});

        return list;
    }


    public static void onDeleteValueTable(final SQLiteDatabase db) {
        Log.i(TAG, "DeleteTable");
        db.execSQL("DELETE FROM " + TABLE_NAME + ";");
    }

}
