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
    public static final String FILE_NAME = "ref_goods.csv";
    public static final String HEADER_NAME = "товаров";

    public static final String COLUMN_KOD = "kod";
    public static final String COLUMN_ID_CATEGORY = "id_category";
    public static final String COLUMN_IS_CATEGORY = "is_category";
    public static final String COLUMN_LEVEL = "level";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_RATE = "rate";
    public static final String COLUMN_BARCODE = "barcode";
    public static final String COLUMN_NOTE = "note";

    public static final String INSERT_VALUES = "INSERT INTO " + TABLE_NAME + " ("
            + COLUMN_KOD + ", "
            + COLUMN_ID_CATEGORY + ", "
            + COLUMN_IS_CATEGORY + ", "
            + COLUMN_LEVEL + ", "
            + COLUMN_NAME + ", "
            + COLUMN_RATE + ", "
            + COLUMN_BARCODE + ", "
            + COLUMN_NOTE
            + ") VALUES ";


    private static final String TAG = TableProducts.class.getSimpleName();

    public static void createTable(final SQLiteDatabase db) {
        Log.i(TAG, "createTable");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + BaseColumns._ID + " integer PRIMARY KEY AUTOINCREMENT"
                + " ," + COLUMN_KOD + " text"
                + " ," + COLUMN_ID_CATEGORY + " text"
                + " ," + COLUMN_IS_CATEGORY + " text"
                + " ," + COLUMN_LEVEL + " text"
                + " ," + COLUMN_NAME + " text"
                + " ," + COLUMN_RATE + " text"
                + " ," + COLUMN_BARCODE + " text"
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
        data.put(COLUMN_ID_CATEGORY, sData[1]);
        data.put(COLUMN_IS_CATEGORY, sData[2]);
        data.put(COLUMN_LEVEL, sData[3]);
        data.put(COLUMN_NAME, sData[4]);

        return data;
    }
    public static void onDeleteValueTable(final SQLiteDatabase db) {
        Log.i(TAG, "DeleteTable");
        db.execSQL("DELETE FROM " + TABLE_NAME + ";");
    }

}
