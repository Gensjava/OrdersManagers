package ua.com.it_st.ordersmanagers.sqlTables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ua.com.it_st.ordersmanagers.models.Pays;
import ua.com.it_st.ordersmanagers.utils.SQLQuery;

public class TablePaysLines {
    public static final String TABLE_NAME = "PaysLines";
    public static final String FILE_NAME = "doc_pay_lines.csv";
    public static final String sHeader = "DocId,CurrencyId,Amount_usd,Amount_nut,DocDate,DocNumber,line_id,";
    public static final String HEADER_NAME = "табличной части оплаты";

    public static final String COLUMN_DOC_ID = "doc_id";
    public static final String COLUMN_CURRENCY_ID = "currency_id";
    public static final String COLUMN_AMOUNT_NAT = "amount_nat";
    public static final String COLUMN_AMOUNT_USD = "amount_usd";
    public static final String COLUMN_DOC_DATE = "doc_date";
    public static final String COLUMN_NUMBER_DOC = "doc_number";
    public static final String COLUMN_LINE_ID = "line_id";
    public static final String COLUMN_TYPE = "type";

    private static final String TAG = TablePaysLines.class.getSimpleName();

    public static void createTable(final SQLiteDatabase db) {
        Log.i(TAG, "createTable");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + BaseColumns._ID + " integer PRIMARY KEY AUTOINCREMENT"
                + " ," + COLUMN_DOC_ID + " integer"
                + " ," + COLUMN_CURRENCY_ID + " integer"
                + " ," + COLUMN_AMOUNT_NAT + " real"
                + " ," + COLUMN_AMOUNT_USD + " real"
                + " ," + COLUMN_DOC_DATE + " text"
                + " ," + COLUMN_NUMBER_DOC + " text"
                + " ," + COLUMN_LINE_ID + " integer"
                + " ," + COLUMN_TYPE + " text"
                + ");");
    }

    public static void upgradeTable(final SQLiteDatabase db,
                                    final int oldVersion, final int newVersion) {
        Log.i(TAG, "upgradeTable, old: " + oldVersion + ", new: " + newVersion);
    }

    public static ContentValues getContentValues(Pays.PaysLines sData) {
        final ContentValues data = new ContentValues();
        data.put(COLUMN_DOC_ID, sData.getDocId());
        data.put(COLUMN_CURRENCY_ID, sData.getCurrency());
        data.put(COLUMN_AMOUNT_NAT, sData.getSum_nat());
        data.put(COLUMN_AMOUNT_USD, sData.getSum_usd());
        data.put(COLUMN_DOC_DATE, sData.getDateDoc());
        data.put(COLUMN_NUMBER_DOC, sData.getNumberDoc());
        data.put(COLUMN_LINE_ID, sData.getLineId());
        return data;
    }

    public static List<Object> getValueForUpload() {
        List<Object> list = new ArrayList<>();
        list.add(FILE_NAME);
        list.add(sHeader.split(","));
        list.add(HEADER_NAME);
        list.add(SQLQuery.queryPaysLinesFilesCsv("Pays.type  <> ?"));
        list.add(new String[]{"NO_HELD"});
        return list;
    }

    public static void onDeleteValueTable(final SQLiteDatabase db) {
        db.delete(TABLE_NAME, "type = ?", new String[]{"NO_HELD"});
    }
}
