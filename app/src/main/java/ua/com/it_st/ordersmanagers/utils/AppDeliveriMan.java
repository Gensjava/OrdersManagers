package ua.com.it_st.ordersmanagers.utils;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class AppDeliveriMan extends Application {
    public static AppDeliveriMan App;

    public static AppDeliveriMan getApp() {
        return App;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        SQLiteOpenHelperUtil.init(this);
        App = this;
    }
}
