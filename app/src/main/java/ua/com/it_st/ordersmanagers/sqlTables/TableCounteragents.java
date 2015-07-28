package ua.com.it_st.ordersmanagers.sqlTables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Gens on 27.04.2015.
 */
public class TableCounteragents {
    public static final String TABLE_NAME = "Counteragents";
    public static final String FILE_NAME = "NameFile=ref_clients.csv";

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_NAME_FULL = "name_full";
    public static final String COLUMN_KOD = "kod";
    public static final String COLUMN_CATEGORY_KOD = "category_kod";
    public static final String COLUMN_CATEGORY_NAME = "name_category";
    public static final String COLUMN_LEVEL = "level";
    public static final String COLUMN_DEFAULT_PRICE = "default_price_category_kod";
    public static final String COLUMN_DISCOUNT = "discount";
    public static final String COLUMN_ADDRESS = "address";

    private static final String TAG = TableCounteragents.class.getSimpleName();

    public static void createTable(final SQLiteDatabase db) {
        Log.i(TAG, "createTable");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + BaseColumns._ID + " integer PRIMARY KEY AUTOINCREMENT"
                + " ," + COLUMN_NAME + " text"
                + " ," + COLUMN_KOD + " text"
                + " ," + COLUMN_NAME_FULL + " text"
                + " ," + COLUMN_CATEGORY_KOD + " text"
                + " ," + COLUMN_CATEGORY_NAME + " text"
                + " ," + COLUMN_LEVEL + " text"
                + " ," + COLUMN_DEFAULT_PRICE + " text"
                + " ," + COLUMN_DISCOUNT + " text"
                + " ," + COLUMN_ADDRESS + " text"
                + ");");
    }

    public static void upgradeTable(final SQLiteDatabase db,
                                    final int oldVersion, final int newVersion) {

        Log.i(TAG, "upgradeTable, old: " + oldVersion + ", new: " + newVersion);

    }

    public static ContentValues getContentValues(String sData[]) {

        final ContentValues data = new ContentValues();

        data.put(COLUMN_KOD, sData[0]);
        data.put(COLUMN_CATEGORY_KOD, sData[1]);
        data.put(COLUMN_CATEGORY_NAME, sData[2]);
        data.put(COLUMN_LEVEL, sData[3]);
        data.put(COLUMN_NAME, sData[4]);
        data.put(COLUMN_DEFAULT_PRICE, sData[5]);
        data.put(COLUMN_DISCOUNT, sData[6]);
        data.put(COLUMN_ADDRESS, sData[7]);

        return data;

    }

    public static void onDeleteValueTable(final SQLiteDatabase db) {
        Log.i(TAG, "DeleteTable");
        db.execSQL("DELETE FROM " + TABLE_NAME + ";");
    }
}
