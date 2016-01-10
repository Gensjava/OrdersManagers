package ua.com.it_st.ordersmanagers.services;

import android.app.AlarmManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

import java.util.Calendar;

public class AlarmService extends Service {

    long time = SystemClock.elapsedRealtime() + 5000;
    AlarmManager manager;

    public AlarmService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        manager = (AlarmManager) getSystemService(
                Context.ALARM_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 10);
        long time = calendar.getTimeInMillis();

        // manager.setRepeating(AlarmManager.RTC_WAKEUP, time,
        //    AlarmManager.INTERVAL_HOUR, pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }
}
