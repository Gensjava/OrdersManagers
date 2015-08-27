package ua.com.it_st.ordersmanagers.utils;

import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.util.Log;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import ua.com.it_st.ordersmanagers.sqlTables.TablePrices;

public class WorkFilesUtil {

    public static void onInsertTable(final File file, final String fileName, final SQLiteDatabase db, final Map<String, String> lTableNameInsert, final Map<String, String> lTableName) throws IOException {

        DownloadAsyncFile downloadAsyncFile = new DownloadAsyncFile(lTableNameInsert.get(fileName), lTableName.get(fileName), db, file);
        downloadAsyncFile.execute();
    }

    private static class DownloadAsyncFile extends AsyncTask<String, String, String> {

        private final String TEG = WorkFilesUtil.class.getSimpleName();

        private String mCvsSplitBy = ",\"";
        private String mNameTable;
        private String mLineInsert;
        private SQLiteDatabase mDatabase;
        private File mFile;
        private int limitInsert = 100;

        public DownloadAsyncFile(final String lineInsert, final String nameTable, final SQLiteDatabase database, final File file) {
            mLineInsert = lineInsert;
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

            try {
                /* Считываем по одной строке */
                BufferedReader input = new BufferedReader(new FileReader(mFile));
                /*вычисляем к-во строк в файле*/
                int totalLinesFile = getCountFileLines(mFile);
                /*создаем строку команды для базы данных в базу*/
                StringBuilder sql = new StringBuilder();

                int n = 0;/*счетчик*/
                String line;
                input.readLine();
                while ((line = input.readLine()) != null) {
                    /*получаем массив из строчке*/
                    String[] country = line.split(mCvsSplitBy);
                    String[] pCountry = new String[country.length];
                    /*начинаем формировать значения для insert*/
                    sql.append("(");

                    for (byte s = 0; s < country.length; s++) {
                        /*делаем замену символа*/
                        pCountry[s] = country[s].replace('\"', ' ');
                        pCountry[s] = pCountry[s].replace('\'', ' ');
                        /*убираем пробелы в строке*/
                        pCountry[s] = pCountry[s].trim();
                        /*добавляем значение из  файла*/
                        sql.append("'");
                        sql.append(pCountry[s]);
                        sql.append("'");
                        if (s + 1 != country.length) {
                            sql.append(",");
                        }
                    }
                    sql.append(")");
                   /*счетчик*/
                    n++;
                    /*делаем добавления пачки строк в базу при выполнеии условий*/
                    if (n == limitInsert || n == totalLinesFile) {
                        sql.append("");
                        mDatabase.execSQL(mLineInsert + sql);
                        totalLinesFile = totalLinesFile - n;
                        n = 0;
                        sql = new StringBuilder();
                    } else {
                        sql.append(",");
                    }
                }

                input.close();

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
            ErrorInfo.setmLogLine("Загрузка в таблицу завершена", mNameTable);
        }

        /*получаем кол-во строк в файле*/
        private int getCountFileLines(File mFile) {
            int n = 0;
            String line;
            BufferedReader input = null;
            try {
                input = new BufferedReader(new FileReader(mFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                input.readLine();
                while ((line = input.readLine()) != null) {
                    n++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return n;
        }

    }


//    CSVReader reader = new CSVReader(input,',', '"', 1); // со второй строки
//
//    int n = 0;/*счетчик*/
//
//    String [] nextLine;
//    String [] pNextLine = new String[0];
//    while ((nextLine = reader.readNext()) != null) {
//
//                     /*начинаем формировать значения для insert*/
//        sql.append("(");
//
//        for (byte s = 0; s < nextLine.length; s++) {
//            //                        /*делаем замену символа*/
//            pNextLine[s] = nextLine[s].replace('\"', ' ');
//            pNextLine[s] =  pNextLine[s].replace(',', ' ');
//            pNextLine[s] =  pNextLine[s].replace('\'', ' ');
//                        /*убираем пробелы в строке*/
//            pNextLine[s] = pNextLine[s].trim();
//                        /*добавляем значение из  файла*/
//            sql.append("'");
//            sql.append(nextLine[s]);
//            sql.append("'");
//            if (s + 1 != nextLine.length ){
//                sql.append(",");
//            }
//        }
//        sql.append(")");
//        n++;
////                   // Log.i("Загрузка",""+n1);
//                    /*делаем добавления пачки строк в базу при выполнеии условий*/
//        if (n == limitInsert || n == totalLinesFile) {
//            //  sql.append("");
//            mDatabase.execSQL(mLineInsert + sql);
//            totalLinesFile = totalLinesFile - n;
//            n = 0;
//            sql = new StringBuilder();
//        } else {
//            sql.append(",");
//        }
//    }

}
