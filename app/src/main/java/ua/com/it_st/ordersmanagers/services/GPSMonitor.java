package ua.com.it_st.ordersmanagers.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import ua.com.it_st.ordersmanagers.utils.ConstantsUtil;
import ua.com.it_st.ordersmanagers.utils.MyLocationListener;
import ua.com.it_st.ordersmanagers.utils.SendDataGPS;

public class GPSMonitor extends Service {

    // constant
    public static final long NOTIFY_INTERVAL = 30 * 60 * 1000; // полчаса
    public static final long FIRST_NOTIFY_INTERVAL = 5 * 1000; // 5 сек для первого запуска
    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;
    private Timer mFistTimer = null;
    //
    private int mInitialPeriod = 8;
    private int mEndPeriod = 19;
    private int mCurrentTime;

    public GPSMonitor() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            SimpleWakefulReceiver.completeWakefulIntent(intent);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        // cancel if already existed
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }

        if (mFistTimer != null) {
            mFistTimer.cancel();
        } else {
            // recreate new
            mFistTimer = new Timer();
        }
        // schedule task
        mFistTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0,
                FIRST_NOTIFY_INTERVAL);
        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0,
                NOTIFY_INTERVAL);
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {

                    //выставляем период времени когда отправлять
                    Calendar c = Calendar.getInstance();
                    mCurrentTime = c.get(Calendar.HOUR_OF_DAY);

                    if (mCurrentTime >= mInitialPeriod & mCurrentTime <= mEndPeriod) {

                        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        LocationListener mlocListener = new MyLocationListener();
                        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

                        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            //отправляем данные на сервер
                            SendDataGPS sendDataGPS = new SendDataGPS(ConstantsUtil.getDate() + " " + ConstantsUtil.getTime(),
                                    String.valueOf(MyLocationListener.latitude),
                                    String.valueOf(MyLocationListener.longitude), "SendDataGPS", getApplicationContext());
                            sendDataGPS.sendDataOnServer();
                            if (MyLocationListener.latitude > 0) {
//                                // schedule task
                                if (mFistTimer != null) {
                                    mFistTimer.cancel();
                                    mFistTimer = null;
                                }
                            }

                        } else {
                            stopSelf();
                            mTimer.cancel();
                        }
                        // } else {
                        //stopSelf();
                        // mTimer.cancel();
                    }
                }
            });
        }
    }
}
