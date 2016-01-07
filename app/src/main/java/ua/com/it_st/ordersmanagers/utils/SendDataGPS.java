package ua.com.it_st.ordersmanagers.utils;

import android.content.Context;

import com.loopj.android.http.RequestParams;

import ua.com.it_st.ordersmanagers.fragmets.FilesFragment;

//отправляем данные на сервер
public class SendDataGPS extends FilesFragment {

    private String mTime, mLat, vLon, mStatus;
    private Context mContext;

    public SendDataGPS(String time, String lat, String lon, String status, Context context) {

        mTime = time;
        mLat = lat;
        vLon = lon;
        mContext = context;
        mStatus = status;
    }
    //отправляем данные на сервер
    public void sendDataOnServer() {

        /*подключаемся к серверу*/
        ConnectServer connectData = new ConnectServer(mContext, (byte) 1);

        /*подключились к базе или нет*/
        boolean lConnect = connectData.isMlConnect();
        //передаем данные
        AsyncHttpClientUtil mUtilAsyncHttpClient = connectData.getAsyncHttpClientUtil();

        if (!lConnect) {
            return;
        }
        //класс работает с настройками программы
        WorkSharedPreferences lWorkSharedPreferences = new WorkSharedPreferences(ConstantsUtil.mSettings, mContext);

        RequestParams params = new RequestParams();
        params.put("time", mTime);
        params.put("lat", mLat);
        params.put("lon", vLon);
        params.put("status", mStatus);
        params.put("kodAgent", lWorkSharedPreferences.getIDUser());

        try {
            mUtilAsyncHttpClient.postUnloadParams(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
