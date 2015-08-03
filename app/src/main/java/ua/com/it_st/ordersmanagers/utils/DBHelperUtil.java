package ua.com.it_st.ordersmanagers.utils;

import android.content.ContentValues;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import java.util.HashMap;
import java.util.Map;
import ua.com.it_st.ordersmanagers.sqlTables.TableCompanies;
import ua.com.it_st.ordersmanagers.sqlTables.TableCounteragents;
import ua.com.it_st.ordersmanagers.sqlTables.TableGoodsByStores;
import ua.com.it_st.ordersmanagers.sqlTables.TablePrices;
import ua.com.it_st.ordersmanagers.sqlTables.TableProducts;
import ua.com.it_st.ordersmanagers.sqlTables.TableTypePrices;
import ua.com.it_st.ordersmanagers.sqlTables.TableTypeStores;

/**
 * Created by Gens on 28.07.2015.
 */
public class DBHelperUtil extends DatabaseUtils.InsertHelper {

    public DBHelperUtil(final SQLiteDatabase db, final String tableName) {
        super(db, tableName);
    }

    public static Map getListHashMapTableName() {

        Map<String, String> lTableName = new HashMap<String, String>();

        lTableName.put(TableCompanies.FILE_NAME, TableCompanies.TABLE_NAME);
        lTableName.put(TableCounteragents.FILE_NAME, TableCounteragents.TABLE_NAME);
        lTableName.put(TablePrices.FILE_NAME, TablePrices.TABLE_NAME);
        lTableName.put(TableProducts.FILE_NAME, TableProducts.TABLE_NAME);
        lTableName.put(TableTypePrices.FILE_NAME, TableTypePrices.TABLE_NAME);
        lTableName.put(TableTypeStores.FILE_NAME, TableTypeStores.TABLE_NAME);
        lTableName.put(TableGoodsByStores.FILE_NAME, TableGoodsByStores.TABLE_NAME);

        return lTableName;
    }

    public static ContentValues getListContentValuesTableName(String nameTable, String[] sData) {

        ContentValues lContentValues = new ContentValues();
        switch (nameTable) {
            case TableCompanies.TABLE_NAME:
                lContentValues = TableCompanies.getContentValues(sData);
                break;
            case TableCounteragents.TABLE_NAME:
                lContentValues = TableCounteragents.getContentValues(sData);
                break;
            case TablePrices.TABLE_NAME:
                lContentValues = TablePrices.getContentValues(sData);
                break;
            case TableProducts.TABLE_NAME:
                lContentValues = TableProducts.getContentValues(sData);
                break;
            case TableTypePrices.TABLE_NAME:
                lContentValues = TableTypePrices.getContentValues(sData);
                break;
            case TableTypeStores.TABLE_NAME:
                lContentValues = TableTypeStores.getContentValues(sData);
                break;
            case TableGoodsByStores.TABLE_NAME:
                lContentValues = TableGoodsByStores.getContentValues(sData);
                break;
            default:
                break;
        }
        return lContentValues;
    }



    @Override
    public void bind(final int index, final boolean value) {
        super.bind(index, value);
    }

    @Override
    public long insert(final ContentValues values) {
        return super.insert(values);
    }
    //  db.addRec("sometext " + (scAdapter.getCount() + 1), R.drawable.ic_launcher);

    //    public static Map getListContentValuesTableName(String [] sData) {
//
//        Map<String, ContentValues> lContentValues = new HashMap<String, ContentValues>();
//
//        lContentValues.put(TableCompanies.TABLE_NAME, TableCompanies.getContentValues(sData));
//        lContentValues.put(TableCounteragents.TABLE_NAME, TableCounteragents.getContentValues(sData));
//        lContentValues.put(TablePrices.TABLE_NAME, TablePrices.getContentValues(sData));
//        lContentValues.put(TableProducts.TABLE_NAME, TableProducts.getContentValues(sData));
//        lContentValues.put(TableTypePrices.TABLE_NAME, TableTypePrices.getContentValues(sData));
//        lContentValues.put(TableTypeStores.TABLE_NAME, TableTypeStores.getContentValues(sData));
//        lContentValues.put(TableGoodsByStores.TABLE_NAME, TableGoodsByStores.getContentValues(sData));
//
//        return lContentValues;
//    }
    //                Cursor cursor = db.query(TableCompanies.TABLE_NAME, // table name
//                         null, // columns
//                         null, // selection
//                         null, // selectionArgs
//                         null, // groupBy
//                         null, // having
//                         null);// orderBy
//
//                        while (cursor.moveToNext()){
//                            String catName = cursor.getString(cursor.getColumnIndex(TableCompanies.COLUMN_NAME));
//                            Log.i("TABLE_NAME",""+catName);
//        }
//                cursor.close();
}
