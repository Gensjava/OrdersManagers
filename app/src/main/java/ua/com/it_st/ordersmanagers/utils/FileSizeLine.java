package ua.com.it_st.ordersmanagers.utils;

import android.os.AsyncTask;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import org.apache.http.Header;
import org.apache.http.auth.AuthScope;

import ua.com.it_st.ordersmanagers.R;

public class FileSizeLine extends AsyncTask<String, Integer, String> {

    private String mBaseUrl;
    private RequestParams mParams;
    private String mloginServer;
    private String mPasswordServer;

    public FileSizeLine(final String baseUrl, final RequestParams params, final String loginServer, final String passwordServer) {
        mBaseUrl = baseUrl;
        mParams = params;
        mloginServer = loginServer;
        mPasswordServer = passwordServer;
    }

    @Override
    protected String doInBackground(final String... params) {
        try {
            AsyncHttpClientAsyncTask asyncHttpClientAsyncTask = new AsyncHttpClientAsyncTask();
            asyncHttpClientAsyncTask.setBasicAuth(mloginServer, mPasswordServer, AuthScope.ANY);
            asyncHttpClientAsyncTask.getSizeFile(mParams);
            asyncHttpClientAsyncTask.setMaxRetriesAndTimeout(3, 60);
        } catch (Exception e) {
            e.printStackTrace();
            //Log
            InfoUtil.setmLogLine("Подключение к базе", true, e.toString());
        }
        return null;
    }

    public class AsyncHttpClientAsyncTask extends SyncHttpClient {

        public AsyncHttpClientAsyncTask() throws Exception {

        }

        public void getSizeFile(final RequestParams params) {

            get(mBaseUrl, params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(final int statusCode, final Header[] headers, final String responseString, final Throwable throwable) {
                    //Log
                    InfoUtil.setmLogLine("Расчет  файлов строк ", params.toString(), true, " Failure: Код ошибки " + statusCode);
                }

                @Override
                public void onSuccess(final int statusCode, final Header[] headers, final String responseString) {
                    ConstantsUtil.sizeFileLine = Integer.parseInt(responseString);
                }
            });
        }
    }


}
