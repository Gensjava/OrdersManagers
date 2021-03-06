package ua.com.it_st.ordersmanagers.sqlTables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ua.com.it_st.ordersmanagers.enums.DocType;
import ua.com.it_st.ordersmanagers.models.Pays;
import ua.com.it_st.ordersmanagers.utils.AppDeliveriMan;
import ua.com.it_st.ordersmanagers.utils.ConstantsUtil;
import ua.com.it_st.ordersmanagers.utils.SQLQuery;
import ua.com.it_st.ordersmanagers.utils.WorkSharedPreferences;

public class TablePays {
    public static final String TABLE_NAME = "Pays";
    public static final String FILE_NAME = "doc_pay.csv";
    public static final String HEADER_NAME = "шапка оплат";
    public static final String COLUMN_TOTAL_NAT = "total_nat";
    public static final String COLUMN_TOTAL_USD = "total_usd";
    private static final String sHeader = "Id, DocType, DocDate, DocNumber, Completed, AgentId, FirmId, ClientId, Total_nat,Total_usd, Note,";
    private static final String COLUMN_VIEW_ID = "view_id";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_DATE_BASE = "date_base";
    private static final String COLUMN_NUMBER = "number";
    private static final String COLUMN_NUMBER_BASE = "number_base";
    private static final String COLUMN_COMPLETED = "completed";
    private static final String COLUMN_AGENT_ID = "agent_id";
    private static final String COLUMN_COMPANY_ID = "company_id";
    private static final String COLUMN_CLIENT_ID = "client_id";
    private static final String COLUMN_NOTE = "note";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_CLIENT_ADRESS = "client_adress";

    private static final String TAG = TablePays.class.getSimpleName();

    public static void createTable(final SQLiteDatabase db) {
        Log.i(TAG, "createTable");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + BaseColumns._ID + " integer PRIMARY KEY AUTOINCREMENT"
                + " ," + COLUMN_VIEW_ID + " text"
                + " ," + COLUMN_TYPE + " text"
                + " ," + COLUMN_DATE + " text"
                + " ," + COLUMN_DATE_BASE + " text"
                + " ," + COLUMN_NUMBER + " text"
                + " ," + COLUMN_NUMBER_BASE + " text"
                + " ," + COLUMN_COMPLETED + " text"
                + " ," + COLUMN_AGENT_ID + " text"
                + " ," + COLUMN_COMPANY_ID + " text"
                + " ," + COLUMN_CLIENT_ID + " text"
                + " ," + COLUMN_TOTAL_NAT + " real"
                + " ," + COLUMN_TOTAL_USD + " real"
                + " ," + COLUMN_NOTE + " text"
                + " ," + COLUMN_TIME + " text"
                + " ," + COLUMN_CLIENT_ADRESS + " text"
                + ");");
    }

    public static void upgradeTable(final SQLiteDatabase db,
                                    final int oldVersion, final int newVersion) {
        Log.i(TAG, "upgradeTable, old: " + oldVersion + ", new: " + newVersion);
    }

    public static ContentValues getContentValues(Pays sData) {
        WorkSharedPreferences lWorkSharedPreferences = new WorkSharedPreferences(AppDeliveriMan.getApp());
        final ContentValues data = new ContentValues();
        data.put(COLUMN_VIEW_ID, sData.getId());
        data.put(COLUMN_TYPE, DocType.HELD.toString());
        data.put(COLUMN_DATE, sData.getDocDate());
        data.put(COLUMN_NUMBER, sData.getDocNumber());
        data.put(COLUMN_COMPLETED, "");
        data.put(COLUMN_AGENT_ID, lWorkSharedPreferences.getIDUser());
        data.put(COLUMN_COMPANY_ID, sData.getCompany().getKod());
        data.put(COLUMN_CLIENT_ID, sData.getCounteragent().getKod());
        data.put(COLUMN_TOTAL_NAT, sData.getTotal_nut());
        data.put(COLUMN_TOTAL_USD, sData.getTotal_usd());
        data.put(COLUMN_NOTE, sData.getNote());
        data.put(COLUMN_TIME, ConstantsUtil.getTime());
        data.put(COLUMN_CLIENT_ADRESS, sData.getCounteragent().getAddress());
        return data;
    }

    public static ContentValues getContentValuesUpdata(Pays sData) {
        final ContentValues data = new ContentValues();
        data.put(COLUMN_TOTAL_NAT, sData.getTotal_nut());
        data.put(COLUMN_COMPANY_ID, sData.getCompany().getKod());
        data.put(COLUMN_CLIENT_ID, sData.getCounteragent().getKod());
        data.put(COLUMN_NOTE, sData.getNote());
        data.put(COLUMN_CLIENT_ADRESS, sData.getCounteragent().getAddress());
        return data;
    }

    public static List<Object> getValueForUpload() {
        List<Object> list = new ArrayList<>();
        list.add(FILE_NAME);
        list.add(sHeader.split(","));
        list.add(HEADER_NAME);
        list.add(SQLQuery.queryPaysHeaderFilesCsv("Pays.type  <> ?"));
        list.add(new String[]{"NO_HELD"});
        return list;
    }

    public static void onDeleteValueTable(final SQLiteDatabase db) {
        db.delete(TABLE_NAME, "type = ?", new String[]{"NO_HELD"});
    }
}
