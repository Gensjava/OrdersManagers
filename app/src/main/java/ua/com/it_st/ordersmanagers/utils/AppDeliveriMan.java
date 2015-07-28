package ua.com.it_st.ordersmanagers.utils;

import android.app.Application;

/**
 * Created by Gens on 27.04.2015.
 */
public class AppDeliveriMan extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SQLiteOpenHelperUtil.init(this);
    }
}
