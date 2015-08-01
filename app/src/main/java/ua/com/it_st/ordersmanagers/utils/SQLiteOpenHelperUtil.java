package ua.com.it_st.ordersmanagers.utils;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ua.com.it_st.ordersmanagers.sqlTables.TableCompanies;
import ua.com.it_st.ordersmanagers.sqlTables.TableCounteragents;
import ua.com.it_st.ordersmanagers.sqlTables.TableGoodsByStores;
import ua.com.it_st.ordersmanagers.sqlTables.TableInformations;
import ua.com.it_st.ordersmanagers.sqlTables.TableOrders;
import ua.com.it_st.ordersmanagers.sqlTables.TablePrices;
import ua.com.it_st.ordersmanagers.sqlTables.TableProducts;
import ua.com.it_st.ordersmanagers.sqlTables.TableTasks;
import ua.com.it_st.ordersmanagers.sqlTables.TableTypeInformations;
import ua.com.it_st.ordersmanagers.sqlTables.TableTypeMeasuring;
import ua.com.it_st.ordersmanagers.sqlTables.TableTypeOrders;
import ua.com.it_st.ordersmanagers.sqlTables.TableTypePrioritiesTasks;
import ua.com.it_st.ordersmanagers.sqlTables.TableTypeStores;
import ua.com.it_st.ordersmanagers.sqlTables.TableUsers;
import ua.com.it_st.ordersmanagers.sqlTables.TableTypePrices;


/**
 * Created by Gens on 27.04.2015.
 */
public class SQLiteOpenHelperUtil extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "db_courier_orders.db";
    public static final int DATABASE_VERSION = 1;

    private static volatile SQLiteOpenHelperUtil sInstance = null;

    private SQLiteDatabase mDatabase;

    public SQLiteOpenHelperUtil(final Context context, final String name, final SQLiteDatabase.CursorFactory factory, final int version) {
        super(context, name, factory, version);
    }

    public SQLiteOpenHelperUtil(final Context context, final String name, final SQLiteDatabase.CursorFactory factory, final int version, final DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public SQLiteOpenHelperUtil(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mDatabase = getWritableDatabase();
    }

    public static SQLiteOpenHelperUtil getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("init not called");
        }
        return sInstance;
    }

    public static void init(final Context context) {

        if (sInstance == null) {
            synchronized (SQLiteOpenHelperUtil.class) {
                if (sInstance == null) {
                    sInstance = new SQLiteOpenHelperUtil(context);
                }
            }
        }
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {

        TableCounteragents.createTable(db);
        TableInformations.createTable(db);
        TableOrders.createTable(db);
        TablePrices.createTable(db);
        TableProducts.createTable(db);
        TableTasks.createTable(db);
        TableTypeInformations.createTable(db);
        TableTypeMeasuring.createTable(db);
        TableTypeOrders.createTable(db);
        TableTypePrioritiesTasks.createTable(db);
        TableUsers.createTable(db);
        TableTypePrices.createTable(db);
        TableCompanies.createTable(db);
        TableGoodsByStores.createTable(db);
        TableTypeStores.createTable(db);

    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {

        TableUsers.upgradeTable(db, oldVersion, newVersion);
        TableCounteragents.upgradeTable(db, oldVersion, newVersion);

    }

    public SQLiteDatabase getDatabase() {
        return mDatabase;
    }

    @Override
    public synchronized void close() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }
}