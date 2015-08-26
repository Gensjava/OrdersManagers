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

    public static Map getListTableName() {

        Map<String, String> lTableName = new HashMap<String, String>();

        lTableName.put(TableCompanies.FILE_NAME, TableCompanies.INSERT_VALUES);
        lTableName.put(TableCounteragents.FILE_NAME, TableCounteragents.INSERT_VALUES);
        lTableName.put(TablePrices.FILE_NAME, TablePrices.INSERT_VALUES);
        lTableName.put(TableProducts.FILE_NAME, TableProducts.INSERT_VALUES);
        lTableName.put(TableTypePrices.FILE_NAME, TableTypePrices.INSERT_VALUES);
        lTableName.put(TableTypeStores.FILE_NAME, TableTypeStores.INSERT_VALUES);
        lTableName.put(TableGoodsByStores.FILE_NAME, TableGoodsByStores.INSERT_VALUES);

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

    @Override
    public long replace(final ContentValues values) {
        return super.replace(values);
    }
}
