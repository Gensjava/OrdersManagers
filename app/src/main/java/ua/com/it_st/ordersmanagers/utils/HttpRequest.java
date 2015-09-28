package ua.com.it_st.ordersmanagers.utils;

import android.os.AsyncTask;
import android.os.Looper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Gens on 28.09.2015.
 */
public class HttpRequest extends AsyncTask<String, Integer, String> {

    // A SyncHttpClient is an AsyncHttpClient
    public static AsyncHttpClient syncHttpClient = new SyncHttpClient();
    public static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    private String mBaseUrl;
    private RequestParams mParams;
    private String mloginServer;
    private String mPasswordServer;

    public HttpRequest(final String baseUrl, final RequestParams params, final String loginServer, final String passwordServer) {
        mBaseUrl = baseUrl;
        mParams = params;
        mloginServer = loginServer;
        mPasswordServer = passwordServer;
    }

    /**
     * @return an async client when calling from the main thread, otherwise a sync client.
     */
    public static AsyncHttpClient getClient() {
        // Return the synchronous HTTP client when the thread is not prepared
        if (Looper.myLooper() == null)
            return syncHttpClient;
        return asyncHttpClient;
    }

    @Override
    protected String doInBackground(final String... strings) {
        try {
            //AsyncHttpClient syncHttpClientAsyncTask = getClient();
            getClient().setBasicAuth(mloginServer, mPasswordServer);
            getClient().get(mBaseUrl, mParams, new TextHttpResponseHandler() {
                @Override
                public void onFailure(final int statusCode, final Header[] headers, final String responseString, final Throwable throwable) {
                    InfoUtil.setmLogLine("Расчет  файлов строк ", mParams.toString(), true, " Failure: Код ошибки " + statusCode);
                }

                @Override
                public void onSuccess(final int statusCode, final Header[] headers, final String responseString) {
                    ConstantsUtil.sizeFileLine = Integer.parseInt(responseString);
                }
            });
            // syncHttpClientAsyncTask.setTimeout(50000);

            // } catch (java.net.SocketTimeoutException e) {

//            try {
//                AsyncHttpClient syncHttpClientAsyncTask = getClient();
//                syncHttpClientAsyncTask.setBasicAuth(mloginServer, mPasswordServer);
//                syncHttpClientAsyncTask.get(mBaseUrl,mParams);
//                syncHttpClientAsyncTask.setTimeout(50000);
//            } catch (Exception e1) {
//                e1.printStackTrace();
//            }

        } catch (Exception e) {
            e.printStackTrace();
            //Log
            InfoUtil.setmLogLine("Подключение к базе", true, e.toString());
        }
        return null;
    }
}
