package ua.com.it_st.ordersmanagers.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;

/*клас для подключения к серверу 1с*/
    /* подключаемся через HTTP к базе и загужаем данные */
public class ConnectServer {

    private final String TEG = ConnectServer.class.getSimpleName();
    private boolean mlConnect;
    private AsyncHttpClientUtil mAsyncHttpClientUtil;
    private SharedPreferences mSettings;
    private Context mContext;

    public ConnectServer(Context context, byte tWay) {

        mContext = context;
             /*вызываем менеджера настроек*/
        mSettings = PreferenceManager.getDefaultSharedPreferences(mContext);

        if (mSettings == null || mContext == null) {
            return;
        }

            /*проверка есть интерент или нет*/
        boolean isInternet = ConstantsUtil.isInternetAvailable(mContext);
        if (!isInternet) {
            InfoUtil.showErrorAlertDialog(context.getString(R.string.error_inet), context.getString(R.string.updata), mContext);
            return;
        }

        //класс работает с настройками программы
        WorkSharedPreferences lWorkSharedPreferences = new WorkSharedPreferences(mContext);

              /* список шаблонов пути к серверу  */
        String[] templateWay = mContext.getResources().getStringArray(R.array.template_way);

        String loginServer = lWorkSharedPreferences.getMloginServer();
        if (loginServer == null) {
            return;
        }

        String passwordServer = lWorkSharedPreferences.getPasswordServer();
        String IdServer = lWorkSharedPreferences.getIdServer();

        AsyncHttpClientUtil utilAsyncHttpClient = null;
        try {
            if (mContext.getClass().equals(MainActivity.class)) {//выгрузка загрузка файлов
                utilAsyncHttpClient = new AsyncHttpClientUtil((MainActivity) mContext, IdServer + templateWay[tWay]);
            } else {
                utilAsyncHttpClient = new AsyncHttpClientUtil(IdServer + templateWay[tWay]);//обмен данными GPS
            }

            utilAsyncHttpClient.setBasicAuth(loginServer, passwordServer);
            utilAsyncHttpClient.setTimeout(30000);
            utilAsyncHttpClient.setConnectTimeout(30000);
            utilAsyncHttpClient.setResponseTimeout(30000);

            setMlConnect(true);

        } catch (Exception e) {
            e.printStackTrace();
            setMlConnect(false);
            //Log
            InfoUtil.setmLogLine(mContext.getString(R.string.action_conect_base), true, TEG + ": " + e.toString());
        }
        setAsyncHttpClientUtil(utilAsyncHttpClient);
    }

    public AsyncHttpClientUtil getAsyncHttpClientUtil() {
        return mAsyncHttpClientUtil;
    }

    public void setAsyncHttpClientUtil(final AsyncHttpClientUtil asyncHttpClientUtil) {
        mAsyncHttpClientUtil = asyncHttpClientUtil;
    }

    public boolean isMlConnect() {
        return mlConnect;
    }

    public void setMlConnect(final boolean mlConnect) {
        this.mlConnect = mlConnect;
    }

}
