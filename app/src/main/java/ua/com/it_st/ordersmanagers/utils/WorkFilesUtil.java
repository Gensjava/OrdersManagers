package ua.com.it_st.ordersmanagers.utils;


import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import ua.com.it_st.ordersmanagers.sqlTables.TableCompanies;
import ua.com.it_st.ordersmanagers.sqlTables.TableCounteragents;
import ua.com.it_st.ordersmanagers.sqlTables.TableGoodsByStores;
import ua.com.it_st.ordersmanagers.sqlTables.TablePrices;
import ua.com.it_st.ordersmanagers.sqlTables.TableProducts;
import ua.com.it_st.ordersmanagers.sqlTables.TableTypePrices;
import ua.com.it_st.ordersmanagers.sqlTables.TableTypeStores;

/**
 * Created by Gens on 25.07.2015.
 */
public class WorkFilesUtil {


    public static void onInsertTable(final File file, final String fileName, final SQLiteDatabase db) throws IOException {


        switch (fileName) {
            case TableCounteragents.FILE_NAME:

                break;
            case TableCompanies.FILE_NAME:

                break;
            case TablePrices.FILE_NAME:
                DownloadAsyncFile downloadAsyncFile = new DownloadAsyncFile((String) DBHelperUtil.getListTableName().get(fileName), db, file);
                downloadAsyncFile.execute();

                break;
            case TableProducts.FILE_NAME:


                break;
            case TableTypePrices.FILE_NAME:


                break;
            case TableTypeStores.FILE_NAME:


                break;
            case TableGoodsByStores.FILE_NAME:


                break;
            default:
                break;
        }
    }

    private static class DownloadAsyncFile extends AsyncTask<String, String, String> {

        private String mCvsSplitBy = ",\"";
        private String mNameTable;
        private SQLiteDatabase mDatabase;
        private File mFile;

        public DownloadAsyncFile(final String nameTable, final SQLiteDatabase database, final File file) {
            mNameTable = nameTable;
            mDatabase = database;
            mFile = file;
        }

        @Override
        protected String doInBackground(final String... files) {

            DBHelperUtil dbHelperUtil = new DBHelperUtil(mDatabase, mNameTable);

            try {
                // Считываем по одной строке
                BufferedReader input = new BufferedReader(new FileReader(mFile));

                try {
                    String line;
                    input.readLine();
                    while ((line = input.readLine()) != null) {
                        // use comma as separator
                        String[] country = line.split(mCvsSplitBy);
                        dbHelperUtil.insert(TablePrices.getContentValues(country));
                    }
                } finally {
                    input.close();
                    long msTime = System.currentTimeMillis();
                    Date curDateTime = new Date(msTime);
                    Log.i("currentTimeMillis_4", "" + curDateTime);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

}
