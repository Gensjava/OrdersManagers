package ua.com.it_st.ordersmanagers.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Gens on 25.07.2015.
 */
public class UtilsWorkFiles {

    // функция для чтения текстового файла
    static public String getContent(File file) {
        StringBuilder content = new StringBuilder();

        try {
            // Считываем по одной строке
            BufferedReader input = new BufferedReader(new FileReader(file));

            try {
                String line = null;

                while ((line = input.readLine()) != null) {
                    // прочитанную строку добавляем в буфер
                    // после каждой строки добавляем разделитель строк
                    content.append(line);
                    content.append(System.getProperty("line.separator"));
                    Log.i("appendappend", "" + line.toString());
                }
            } finally {
                input.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // возвращаем буфер строк как одну больщую строку
        return content.toString();
    }
}
