package ua.com.it_st.ordersmanagers.utils;


import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.filippudak.ProgressPieView.ProgressPieView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;


public class WorkFilesUtil {

    public static void onInsertTable(final File file,
                                     final String fileName,
                                     final SQLiteDatabase db,
                                     final Map<String,
                                             String> lTableNameInsert,
                                     final Map<String,
                                             String> lTableName,
                                     final ProgressPieView progressPieView,
                                     final DiscreteSeekBar discreteSeekBar) throws IOException {

        DownloadAsyncFile downloadAsyncFile = new DownloadAsyncFile(
                lTableNameInsert.get(fileName),
                lTableName.get(fileName),
                db,
                file,
                progressPieView,
                discreteSeekBar);
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
        private ProgressPieView mProgressPieView;
        private DiscreteSeekBar mDiscreteSeekBar;
        Handler handlerDiscreteSeek = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                int date = bundle.getInt("DiscreteSeek");
                mDiscreteSeekBar.setProgress(date);
            }
        };
        Handler handlerTotalLinesFile = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                /*выставляем макс и минимум прогресса*/
                int totalLinesFileSeekBar = bundle.getInt("totalLinesFile");
                mDiscreteSeekBar.setMax(totalLinesFileSeekBar);
                mDiscreteSeekBar.setMin(0);
            }
        };
        private int totalLinesFile;

        public DownloadAsyncFile(final String lineInsert,
                                 final String nameTable,
                                 final SQLiteDatabase database,
                                 final File file,
                                 final ProgressPieView progressPieView,
                                 final DiscreteSeekBar discreteSeekBar) {
            mLineInsert = lineInsert;
            mNameTable = nameTable;
            mDatabase = database;
            mFile = file;
            mProgressPieView = progressPieView;
            mDiscreteSeekBar = discreteSeekBar;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
                        /* начинаем транзакцию */
            // mDatabase.beginTransaction();

        }

        @Override
        protected String doInBackground(final String... files) {

            try {
                /* Считываем по одной строке */
                BufferedReader input = new BufferedReader(new FileReader(mFile));
                /*вычисляем к-во строк в файле*/
                totalLinesFile = getCountFileLines(mFile);

                /*отсылаем сообщения прогрессу*/
                Message msgTotalLinesFile = handlerTotalLinesFile.obtainMessage();
                Bundle bundleTotalLinesFile = new Bundle();
                bundleTotalLinesFile.putInt("totalLinesFile", totalLinesFile);
                msgTotalLinesFile.setData(bundleTotalLinesFile);
                handlerTotalLinesFile.sendMessage(msgTotalLinesFile);

                /*создаем строку команды для базы данных в базу*/
                StringBuilder sql = new StringBuilder();

                int n = 0;/*счетчик*/
                int nDSeek = 0;/*счетчик*/
                /**/
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
                      /*отсылаем сообщения прогрессу*/
                    Message msgDiscreteSeek = handlerDiscreteSeek.obtainMessage();
                    Bundle bundleDiscreteSeek = new Bundle();
                    bundleDiscreteSeek.putInt("DiscreteSeek", ++nDSeek);
                    msgDiscreteSeek.setData(bundleDiscreteSeek);
                    handlerDiscreteSeek.sendMessage(msgDiscreteSeek);

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
            mProgressPieView.setProgress(++ConstantsUtil.nPieViewProgress);
            double sProgress = ConstantsUtil.nPieViewdProgress += 14.30;
            mProgressPieView.setText(String.valueOf((int) sProgress) + "%");
            //Log
            ErrorInfo.setmLogLine("Загрузка в таблицу завершена", mNameTable);
                                    /* заканчиваем транзакцию */
            // mDatabase.setTransactionSuccessful();
            //   mDatabase.endTransaction();
        }

        /*получаем кол-во строк в файле*/
        private int getCountFileLines(File mFile) {
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
