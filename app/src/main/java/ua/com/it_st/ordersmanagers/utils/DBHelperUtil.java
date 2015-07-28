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

    public static Map getListTableName() {

        Map<String, String> hTableName = new HashMap<String, String>();

        hTableName.put(TableCompanies.FILE_NAME, TableCompanies.TABLE_NAME);
        hTableName.put(TableCounteragents.FILE_NAME, TableCounteragents.TABLE_NAME);
        hTableName.put(TablePrices.FILE_NAME, TablePrices.TABLE_NAME);
        hTableName.put(TableProducts.FILE_NAME, TableProducts.TABLE_NAME);
        hTableName.put(TableTypePrices.FILE_NAME, TableTypePrices.TABLE_NAME);
        hTableName.put(TableTypeStores.FILE_NAME, TableTypeStores.TABLE_NAME);
        hTableName.put(TableGoodsByStores.FILE_NAME, TableGoodsByStores.TABLE_NAME);

        return hTableName;
    }

    @Override
    public long insert(final ContentValues values) {
        return super.insert(values);
    }

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
