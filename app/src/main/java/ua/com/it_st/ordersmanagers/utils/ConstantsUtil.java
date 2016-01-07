package ua.com.it_st.ordersmanagers.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConstantsUtil {

    /*для расчета прогресс-бара при обмене инфой*/
    public static int nPieViewProgress;
    public static double nPieViewdProgress;
    /*общае кол-во строк при обмене всех файлов */
    public static int sizeFileLine;
    /*вызываем менеджера настроек*/
    public static SharedPreferences mSettings = null;
    /*держим подключнеие*/
    public static ConnectServer sConnectData = null;

    /*
      Получаем текущее дату системы
      Возвращаем строку дату "текущюю дату"
        */
    public static String getDate() {
        /* текущая дата */
        long curTime = System.currentTimeMillis();
        Date date = new Date(curTime);
        /* делаем формат даты */
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(date);
    }
    /*
    Получаем текущее дату системы
    Возвращаем строку время "текущее время"
    */
    public static String getTime() {
        /* текущая дата */
        long curTime = System.currentTimeMillis();
        Date date = new Date(curTime);
        /* делаем формат времени */
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(date);
    }

    /*Проверка подключение к интернету*/
    public static boolean isInternetAvailable(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }
}
