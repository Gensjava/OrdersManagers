package ua.com.it_st.ordersmanagers.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import ua.com.it_st.ordersmanagers.MainActivity;
import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.sqlTables.TableCompanies;

/**
 * Created by Gens on 25.07.2015.
 */
public class UtilsWorkFiles {

    // функция для чтения текстового файла
    static public void getContent(final File file, final String fileName, final SQLiteDatabase db) {

        final String mCvsSplitBy = ",";

        // StringBuilder content = new StringBuilder();

        try {
            // Считываем по одной строке
            BufferedReader input = new BufferedReader(new FileReader(file));

            try {
                String line;
                input.readLine();
                while ((line = input.readLine()) != null) {
                    // прочитанную строку добавляем в буфер
                    // после каждой строки добавляем разделитель строк
                    // content.append(line);
                    //content.append(System.getProperty("line.separator"));
                    Log.i("appendappend", "" + fileName);

                    // use comma as separator
                    String[] country = line.split(mCvsSplitBy);

                    onInsertTable(country, fileName, db);

                    System.out.println("Country [code= " + country[0]
                            + " , name=" + country[1] + "]");
                }
            } finally {
                input.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void onInsertTable(String[] country, String fileName, final SQLiteDatabase db) {

        String lNameTable = null;

        switch (fileName) {
            case TableCompanies.FILE_NAME:
                TableCompanies.onInsert(country, db);

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
                break;
            default:
                break;
        }


    }
}
