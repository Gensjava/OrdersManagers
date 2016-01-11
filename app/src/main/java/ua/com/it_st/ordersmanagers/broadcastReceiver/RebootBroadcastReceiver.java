package ua.com.it_st.ordersmanagers.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ua.com.it_st.ordersmanagers.services.GPSMonitor;

public class RebootBroadcastReceiver extends BroadcastReceiver {

    private final String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";
    Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {

        mContext = context;
        String action = intent.getAction();
        if (action.equalsIgnoreCase(BOOT_ACTION)) {
            //для Service
            Intent serviceIntent = new Intent(context, GPSMonitor.class);
            context.startService(serviceIntent);
        }
    }
}
