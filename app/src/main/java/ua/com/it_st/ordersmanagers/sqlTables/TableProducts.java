package ua.com.it_st.ordersmanagers.sqlTables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Gens on 27.04.2015.
 */
public class TableProducts {
    public static final String TABLE_NAME = "Products";
    public static final String FILE_NAME = "NameFile=ref_goods.csv";

    public static final String COLUMN_KOD = "kod";
    public static final String COLUMN_ID_CATEGORY = "id_category";
    public static final String COLUMN_IS_CATEGORY = "is_category";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LEVEL = "level";


    private static final String TAG = TableProducts.class.getSimpleName();

    public static void createTable(final SQLiteDatabase db) {
        Log.i(TAG, "createTable");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + BaseColumns._ID + " integer PRIMARY KEY AUTOINCREMENT"
                + " ," + COLUMN_ID_CATEGORY + " integer"
                + " ," + COLUMN_NAME + " text"
                + " ," + COLUMN_LEVEL + " integer"
                + " ," + COLUMN_IS_CATEGORY + " text"
                + " ," + COLUMN_KOD + " text"
                + ");");
    }

    public static void upgradeTable(final SQLiteDatabase db,
                                    final int oldVersion, final int newVersion) {
        Log.i(TAG, "upgradeTable, old: " + oldVersion + ", new: " + newVersion);

    }

    public static void onInsert(String sData[], SQLiteDatabase db) {

        final ContentValues data = new ContentValues();

        data.put(COLUMN_KOD, sData[0]);
        data.put(COLUMN_ID_CATEGORY, sData[1]);
        data.put(COLUMN_IS_CATEGORY, sData[2]);
        data.put(COLUMN_LEVEL, sData[3]);
        data.put(COLUMN_NAME, sData[4]);

        db.beginTransaction();
        try {
            db.insert(TABLE_NAME, null, data);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

    }
    public static void onDeleteValueTable(final SQLiteDatabase db) {
        Log.i(TAG, "DeleteTable");
        db.execSQL("DELETE FROM " + TABLE_NAME + ";");
    }

}
