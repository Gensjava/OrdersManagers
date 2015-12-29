package ua.com.it_st.ordersmanagers.utils;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class MyLocationListener implements LocationListener {

    public static double latitude;
    public static double longitude;

    @Override
    public void onLocationChanged(Location loc) {
        loc.getLatitude();
        loc.getLongitude();
        latitude = loc.getLatitude();
        longitude = loc.getLongitude();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i("ProviderDisabled: ", "" + provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i("ProviderEnabled: ", "" + provider);
                /*Запускаем монитор GPS*/
        // startService(new Intent(, GPSMonitor.class));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}