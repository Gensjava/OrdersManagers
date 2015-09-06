package ua.com.it_st.ordersmanagers.utils;

import android.app.Application;

public class AppDeliveriMan extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SQLiteOpenHelperUtil.init(this);
    }
}
