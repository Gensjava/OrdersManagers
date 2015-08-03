package ua.com.it_st.ordersmanagers.utils;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
        protected void onPreExecute() {
            super.onPreExecute();
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
                        String[] pCountry = new String[country.length];
                        //убираем симов "
                        for (byte s = 0; s < country.length; s++) {
                            pCountry[s] = country[s].replace('\"', ' ');
                            pCountry[s] = pCountry[s].trim();
                        }
                        dbHelperUtil.insert(DBHelperUtil.getListContentValuesTableName(mNameTable, pCountry));
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

        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);
            //Log
            ErrorInfo.setmLogLine("Загрузка в таблицу закончено", mNameTable);
        }


    }

}
