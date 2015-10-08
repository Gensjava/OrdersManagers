package ua.com.it_st.ordersmanagers.sqlTables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Gens on 27.04.2015.
 */
public class TableCounteragentsDebt {
    public static final String TABLE_NAME = "CounteragentsDebt";
    public static final String FILE_NAME = "ref_debt.csv";
    public static final String HEADER_NAME = "долг контрагентов";

    public static final String COLUMN_KOD = "kod";
    public static final String COLUMN_LOCK = "lock";
    public static final String COLUMN_DEBT = "debt";
    public static final String COLUMN_NOTE = "note";

    public static final String INSERT_VALUES = "INSERT INTO " + TABLE_NAME + " ("
            + COLUMN_KOD + ", "
            + COLUMN_LOCK + ", "
            + COLUMN_DEBT + ", "
            + COLUMN_NOTE
            + ") VALUES ";

    private static final String TAG = TableCounteragentsDebt.class.getSimpleName();

    public static void createTable(final SQLiteDatabase db) {
        Log.i(TAG, "createTable");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + BaseColumns._ID + " integer PRIMARY KEY AUTOINCREMENT"
                + " ," + COLUMN_KOD + " text"
                + " ," + COLUMN_LOCK + " text"
                + " ," + COLUMN_DEBT + " real"
                + " ," + COLUMN_NOTE + " text"
                + ");");
    }

    public static void upgradeTable(final SQLiteDatabase db,
                                    final int oldVersion, final int newVersion) {
        Log.i(TAG, "upgradeTable, old: " + oldVersion + ", new: " + newVersion);

    }

    public static ContentValues getContentValues(String sData[]) {

        final ContentValues data = new ContentValues();

        data.put(COLUMN_KOD, sData[0]);
        data.put(COLUMN_LOCK, sData[1]);
        data.put(COLUMN_DEBT, sData[2]);
        data.put(COLUMN_NOTE, sData[3]);

        return data;
    }

    public static void onDeleteValueTable(final SQLiteDatabase db) {
        Log.i(TAG, "DeleteTable");
        db.execSQL("DELETE FROM " + TABLE_NAME + ";");
    }

}
