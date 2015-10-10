package ua.com.it_st.ordersmanagers.utils;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import org.apache.http.Header;
import org.apache.http.auth.AuthScope;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.fragmets.FilesLoadFragment;

public class FileSizeLine extends AsyncTask<String, Integer, String> {

    private String mBaseUrl;
    private RequestParams mParams;
    private String mloginServer;
    private String mPasswordServer;
    private MainActivity mMainActivity;

    public FileSizeLine(final String baseUrl, final RequestParams params, final String loginServer, final String passwordServer, final MainActivity mainActivity) {
        mBaseUrl = baseUrl;
        mParams = params;
        mloginServer = loginServer;
        mPasswordServer = passwordServer;
        mMainActivity = mainActivity;
    }

    @Override
    protected String doInBackground(final String... params) {
        try {
            SyncHttpClientAsyncTask syncHttpClientAsyncTask = new SyncHttpClientAsyncTask();
            syncHttpClientAsyncTask.setBasicAuth(mloginServer, mPasswordServer);
            syncHttpClientAsyncTask.setTimeout(30000);
            syncHttpClientAsyncTask.setConnectTimeout(30000);
            syncHttpClientAsyncTask.setResponseTimeout(30000);
            syncHttpClientAsyncTask.getSizeFile(mParams);

        } catch (java.net.SocketTimeoutException e) {

            try {
                SyncHttpClientAsyncTask syncHttpClientAsyncTask = new SyncHttpClientAsyncTask();
                syncHttpClientAsyncTask.setBasicAuth(mloginServer, mPasswordServer);
                syncHttpClientAsyncTask.setTimeout(30000);
                syncHttpClientAsyncTask.setConnectTimeout(30000);
                syncHttpClientAsyncTask.setResponseTimeout(30000);
                syncHttpClientAsyncTask.getSizeFile(mParams);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
            //Log
            InfoUtil.setmLogLine("Подключение к базе", true, e.toString());
        }
        return null;
    }

    public class SyncHttpClientAsyncTask extends SyncHttpClient {

        public SyncHttpClientAsyncTask() throws Exception {

        }

        public void getSizeFile(final RequestParams params) {

            get(mBaseUrl, params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(final int statusCode, final cz.msebera.android.httpclient.Header[] headers, final String responseString, final Throwable throwable) {

                    //Log
                    InfoUtil.setmLogLine("Расчет  файлов строк ", params.toString(), true, " Failure: Код ошибки " + statusCode);
                }

                @Override
                public void onSuccess(final int statusCode, final cz.msebera.android.httpclient.Header[] headers, final String responseString) {
                    ConstantsUtil.sizeFileLine = Integer.parseInt(responseString);
                }
            });
        }
    }
}
