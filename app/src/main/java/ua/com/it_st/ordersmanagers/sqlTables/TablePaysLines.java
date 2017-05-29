package ua.com.it_st.ordersmanagers.sqlTables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import ua.com.it_st.ordersmanagers.models.Pays;

public class TablePaysLines {
    public static final String TABLE_NAME = "PaysLines";
    public static final String FILE_NAME = "doc_pay_lines.csv";
    public static final String sHeader = "DocId,currency_id,Amount,";
    public static final String HEADER_NAME = "табличной части оплаты";

    public static final String COLUMN_DOC_ID = "doc_id";
    public static final String COLUMN_CURRENCY_ID = "currency_id";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_DOC_DATE = "doc_date";
    public static final String COLUMN_NUMBER_DOC = "doc_number";

    private static final String TAG = TablePaysLines.class.getSimpleName();

    public static void createTable(final SQLiteDatabase db) {
        Log.i(TAG, "createTable");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + BaseColumns._ID + " integer PRIMARY KEY AUTOINCREMENT"
                + " ," + COLUMN_DOC_ID + " integer"
                + " ," + COLUMN_CURRENCY_ID + " integer"
                + " ," + COLUMN_AMOUNT + " real"
                + " ," + COLUMN_DOC_DATE + " text"
                + " ," + COLUMN_NUMBER_DOC + " text"
                + ");");
    }

    public static void upgradeTable(final SQLiteDatabase db,
                                    final int oldVersion, final int newVersion) {
        Log.i(TAG, "upgradeTable, old: " + oldVersion + ", new: " + newVersion);

    }

    public static ContentValues getContentValues(Pays.PaysLines sData, String idDoc) {

        final ContentValues data = new ContentValues();

        data.put(COLUMN_DOC_ID, idDoc);
        data.put(COLUMN_CURRENCY_ID, sData.getCurrency());
        data.put(COLUMN_AMOUNT, sData.getSum());
        // data.put(COLUMN_DOC_DATE, sData.getDateDoc());
        // data.put(COLUMN_NUMBER_DOC, sData.getNumberDoc());

        return data;
    }


    public static void onDeleteValueTable(final SQLiteDatabase db) {
        Log.i(TAG, "DeleteTable");
        db.execSQL("DELETE FROM " + TABLE_NAME + ";");
    }
}
