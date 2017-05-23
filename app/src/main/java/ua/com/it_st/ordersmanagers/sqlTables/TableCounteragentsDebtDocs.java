package ua.com.it_st.ordersmanagers.sqlTables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Gens on 27.04.2015.
 */
public class TableCounteragentsDebtDocs {
    public static final String TABLE_NAME = "CounteragentsDebtDocs";
    public static final String FILE_NAME = "ref_debtext.csv";
    public static final String HEADER_NAME = "долг контрагентов по документам";

    public static final String COLUMN_CLIENT_ID = "ClientId";
    public static final String COLUMN_DOC_DATE = "DocDate";
    public static final String COLUMN_DOC_NUMBER = "DocName";
    public static final String COLUMN_SUMMA = "summa";
    public static final String COLUMN_DEBT = "Debt";
    public static final String COLUMN_KOD_CURRENCY = "currency";
    public static final String COLUMN_NOTE = "note";


    public static final String INSERT_VALUES = "INSERT INTO " + TABLE_NAME + " ("
            + COLUMN_CLIENT_ID + ", "
            + COLUMN_DOC_DATE + ", "
            + COLUMN_DOC_NUMBER + ", "
            + COLUMN_SUMMA + ", "
            + COLUMN_DEBT + ", "
            + COLUMN_KOD_CURRENCY + ", "
            + COLUMN_NOTE
            + ") VALUES ";

    private static final String TAG = TableCounteragentsDebtDocs.class.getSimpleName();

    public static void createTable(final SQLiteDatabase db) {
        Log.i(TAG, "createTable");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + BaseColumns._ID + " integer PRIMARY KEY AUTOINCREMENT"
                + " ," + COLUMN_CLIENT_ID + " text"
                + " ," + COLUMN_DOC_DATE + " text"
                + " ," + COLUMN_DOC_NUMBER + " text"
                + " ," + COLUMN_SUMMA + " real"
                + " ," + COLUMN_DEBT + " real"
                + " ," + COLUMN_KOD_CURRENCY + " text"
                + " ," + COLUMN_NOTE + " text"
                + ");");
    }

    public static void upgradeTable(final SQLiteDatabase db,
                                    final int oldVersion, final int newVersion) {
        Log.i(TAG, "upgradeTable, old: " + oldVersion + ", new: " + newVersion);

    }

    public static ContentValues getContentValues(String sData[]) {

        final ContentValues data = new ContentValues();

        data.put(COLUMN_CLIENT_ID, sData[0]);
        data.put(COLUMN_DOC_DATE, sData[1]);
        data.put(COLUMN_DOC_NUMBER, sData[2]);
        data.put(COLUMN_SUMMA, sData[3]);
        data.put(COLUMN_DEBT, sData[4]);
        data.put(COLUMN_KOD_CURRENCY, sData[5]);
        data.put(COLUMN_NOTE, sData[6]);

        return data;
    }

    public static void onDeleteValueTable(final SQLiteDatabase db) {
        Log.i(TAG, "DeleteTable");
        db.execSQL("DELETE FROM " + TABLE_NAME + ";");
    }

}
