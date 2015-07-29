package ua.com.it_st.ordersmanagers.utils;


import android.content.ContentValues;
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

        DownloadAsyncFile downloadAsyncFile = new DownloadAsyncFile((String) DBHelperUtil.getListHashMapTableName().get(fileName), db, file);
                downloadAsyncFile.execute();
    }

    private static class DownloadAsyncFile extends AsyncTask<String, String, String> {

        private final String TEG = getClass().getName();
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
                        dbHelperUtil.insert(DBHelperUtil.getListContentValuesTableName(mNameTable, country));
                    }
                } finally {
                    input.close();

                }
            } catch (IOException e) {
                e.printStackTrace();
                //Log
                ErrorInfo.setmLogLine("Загрузка в таблицу ", mNameTable, true, TEG + e.toString());
            }

            return null;
        }

    }

}
