package ua.com.it_st.ordersmanagers.utils;

import android.widget.ImageView;

import com.loopj.android.http.RequestParams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.sqlTables.TableCompanies;
import ua.com.it_st.ordersmanagers.sqlTables.TableCounteragents;
import ua.com.it_st.ordersmanagers.sqlTables.TableCounteragentsDebt;
import ua.com.it_st.ordersmanagers.sqlTables.TableCounteragentsDebtDocs;
import ua.com.it_st.ordersmanagers.sqlTables.TableCurrencies;
import ua.com.it_st.ordersmanagers.sqlTables.TableCursCurrencies;
import ua.com.it_st.ordersmanagers.sqlTables.TableGoodsByStores;
import ua.com.it_st.ordersmanagers.sqlTables.TablePrices;
import ua.com.it_st.ordersmanagers.sqlTables.TableProducts;
import ua.com.it_st.ordersmanagers.sqlTables.TableTypePrices;
import ua.com.it_st.ordersmanagers.sqlTables.TableTypeStores;

public class WorkFiles {

    private static String TEG = WorkFiles.class.getSimpleName();

    /*получаем кол-во строк в файле*/
    public static int getCountFileLines(File mFile) {
        int n = 0;
        BufferedReader input = null;
        try {
            input = new BufferedReader(new FileReader(mFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            input.readLine();
            while ((input.readLine()) != null) {
                n++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return n;
    }

    /* получаем количество всех строк в файлах до загрузки всех файлов */
    public static void getSizeLine(String wayCatalog, String idServer, String loginServer, String passwordServer, MainActivity pMainActivity, ImageView ImageViewInfo) {

        RequestParams params = new RequestParams();
        params.put(pMainActivity.getString(R.string.SizeFileCatalog), wayCatalog);

        SyngHttpClientUtil fileSizeLine = new SyngHttpClientUtil(idServer, params, loginServer, passwordServer, pMainActivity);
        fileSizeLine.execute();
        try {
            fileSizeLine.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            //Log
            InfoUtil.setmLogLine(pMainActivity.getString(R.string.action_conect_base), true, TEG + ": " + e.toString());
        } catch (ExecutionException e) {
            e.printStackTrace();
            //Log
            InfoUtil.setmLogLine(pMainActivity.getString(R.string.action_conect_base), true, TEG + ": " + e.toString());
            InfoUtil.getFleshImage(R.mipmap.ic_info_red, R.anim.scale_image, ImageViewInfo, pMainActivity);
        }
    }

    public static Map<String, String> getFileNameInsert() {

        Map<String, String> lTableNameInsert = new HashMap<>();

        lTableNameInsert.put(TableCompanies.FILE_NAME, TableCompanies.INSERT_VALUES);
        lTableNameInsert.put(TableCounteragents.FILE_NAME, TableCounteragents.INSERT_VALUES);
        lTableNameInsert.put(TablePrices.FILE_NAME, TablePrices.INSERT_VALUES);
        lTableNameInsert.put(TableProducts.FILE_NAME, TableProducts.INSERT_VALUES);
        lTableNameInsert.put(TableTypePrices.FILE_NAME, TableTypePrices.INSERT_VALUES);
        lTableNameInsert.put(TableTypeStores.FILE_NAME, TableTypeStores.INSERT_VALUES);
        lTableNameInsert.put(TableGoodsByStores.FILE_NAME, TableGoodsByStores.INSERT_VALUES);
        lTableNameInsert.put(TableCounteragentsDebt.FILE_NAME, TableCounteragentsDebt.INSERT_VALUES);
        lTableNameInsert.put(TableCounteragentsDebtDocs.FILE_NAME, TableCounteragentsDebtDocs.INSERT_VALUES);
        lTableNameInsert.put(TableCursCurrencies.FILE_NAME, TableCursCurrencies.INSERT_VALUES);
        lTableNameInsert.put(TableCurrencies.FILE_NAME, TableCurrencies.INSERT_VALUES);

        return lTableNameInsert;
    }

    public static Map<String, String> getFileNameHeader() {

        Map<String, String> lTableName = new HashMap<>();

        lTableName.put(TableCompanies.FILE_NAME, TableCompanies.HEADER_NAME);
        lTableName.put(TableCounteragents.FILE_NAME, TableCounteragents.HEADER_NAME);
        lTableName.put(TablePrices.FILE_NAME, TablePrices.HEADER_NAME);
        lTableName.put(TableProducts.FILE_NAME, TableProducts.HEADER_NAME);
        lTableName.put(TableTypePrices.FILE_NAME, TableTypePrices.HEADER_NAME);
        lTableName.put(TableTypeStores.FILE_NAME, TableTypeStores.HEADER_NAME);
        lTableName.put(TableGoodsByStores.FILE_NAME, TableGoodsByStores.HEADER_NAME);
        lTableName.put(TableCounteragentsDebt.FILE_NAME, TableCounteragentsDebt.HEADER_NAME);
        lTableName.put(TableCounteragentsDebtDocs.FILE_NAME, TableCounteragentsDebtDocs.HEADER_NAME);
        lTableName.put(TableCursCurrencies.FILE_NAME, TableCursCurrencies.HEADER_NAME);
        lTableName.put(TableCurrencies.FILE_NAME, TableCurrencies.HEADER_NAME);

        return lTableName;
    }
}
