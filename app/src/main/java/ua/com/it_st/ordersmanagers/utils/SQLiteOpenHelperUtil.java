package ua.com.it_st.ordersmanagers.utils;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ua.com.it_st.ordersmanagers.sqlTables.TableCompanies;
import ua.com.it_st.ordersmanagers.sqlTables.TableCounteragents;
import ua.com.it_st.ordersmanagers.sqlTables.TableCounteragentsDebt;
import ua.com.it_st.ordersmanagers.sqlTables.TableCounteragentsDebtDocs;
import ua.com.it_st.ordersmanagers.sqlTables.TableCurrencies;
import ua.com.it_st.ordersmanagers.sqlTables.TableCursCurrencies;
import ua.com.it_st.ordersmanagers.sqlTables.TableGoodsByStores;
import ua.com.it_st.ordersmanagers.sqlTables.TableOrders;
import ua.com.it_st.ordersmanagers.sqlTables.TableOrdersLines;
import ua.com.it_st.ordersmanagers.sqlTables.TablePays;
import ua.com.it_st.ordersmanagers.sqlTables.TablePaysLines;
import ua.com.it_st.ordersmanagers.sqlTables.TablePrices;
import ua.com.it_st.ordersmanagers.sqlTables.TableProducts;
import ua.com.it_st.ordersmanagers.sqlTables.TableStores;
import ua.com.it_st.ordersmanagers.sqlTables.TableTypePrices;

public class SQLiteOpenHelperUtil extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "db_courier_orders.db";
    public static final int DATABASE_VERSION = 11;

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
        // mDatabase.compileStatement()

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

    /* чистим таблицы */
    public static void onDeleteValueTables(final SQLiteDatabase mDb) {

        TableCompanies.onDeleteValueTable(mDb);
        TableCounteragents.onDeleteValueTable(mDb);
        TablePrices.onDeleteValueTable(mDb);
        TableProducts.onDeleteValueTable(mDb);
        TableTypePrices.onDeleteValueTable(mDb);
        TableStores.onDeleteValueTable(mDb);
        TableGoodsByStores.onDeleteValueTable(mDb);
        TableOrders.onDeleteValueTable(mDb);
        TableOrdersLines.onDeleteValueTable(mDb);
        TableCounteragentsDebt.onDeleteValueTable(mDb);
        TableCounteragentsDebtDocs.onDeleteValueTable(mDb);
        TableCurrencies.onDeleteValueTable(mDb);
        TableCursCurrencies.onDeleteValueTable(mDb);
        TablePays.onDeleteValueTable(mDb);
        TablePaysLines.onDeleteValueTable(mDb);

    }

    @Override
    public void onCreate(final SQLiteDatabase db) {

        TableCounteragents.createTable(db);
        TableOrders.createTable(db);
        TableOrdersLines.createTable(db);
        TablePrices.createTable(db);
        TableProducts.createTable(db);
        TableTypePrices.createTable(db);
        TableCompanies.createTable(db);
        TableGoodsByStores.createTable(db);
        TableStores.createTable(db);
        TableCounteragentsDebt.createTable(db);
        TableCounteragentsDebtDocs.createTable(db);
        TableCurrencies.createTable(db);
        TableCursCurrencies.createTable(db);
        TablePays.createTable(db);
        TablePaysLines.createTable(db);

    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        if (oldVersion == 1 & newVersion == 2) {
            TableCounteragentsDebt.createTable(db);
        }

        if (oldVersion == 2 & newVersion == 3) {
            db.execSQL("CREATE INDEX \"kod_GoodsByStores\" ON \"GoodsByStores\" (\"kod_coods\" ASC)");
            db.execSQL("CREATE INDEX \"kod_Products\" ON \"Products\" (\"kod\" ASC)");
            db.execSQL("CREATE INDEX \"kod_Prices\" ON \"Prices\" (\"kod\" ASC)");
        }

        if (newVersion < 9) {
            TableCounteragentsDebtDocs.createTable(db);
            TableCurrencies.createTable(db);
            TableCursCurrencies.createTable(db);
            TablePays.createTable(db);
            TablePaysLines.createTable(db);
        }
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
