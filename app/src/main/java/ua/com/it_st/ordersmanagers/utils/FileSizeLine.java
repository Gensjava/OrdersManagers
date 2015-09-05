package ua.com.it_st.ordersmanagers.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.auth.AuthScope;

import java.io.File;

import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.fragmets.LoadFilesFragment;

/**
 * Created by Gens on 06.09.2015.
 */
public class FileSizeLine extends AsyncTask<String, Integer, String> {

    private String mBaseUrl;
    private RequestParams mParams;

    public FileSizeLine(final String baseUrl, final RequestParams params) {
        mBaseUrl = baseUrl;
        mParams = params;
    }

    @Override
    protected String doInBackground(final String... params) {
        try {
            AsyncHttpClientAsyncTask asyncHttpClientAsyncTask = new AsyncHttpClientAsyncTask();
            asyncHttpClientAsyncTask.setBasicAuth("admin", "123", AuthScope.ANY);
            asyncHttpClientAsyncTask.getSizeFile(mParams);

        } catch (Exception e) {
            e.printStackTrace();
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

                }

                @Override
                public void onSuccess(final int statusCode, final Header[] headers, final String responseString) {
                    ConstantsUtil.sizeFileLine = Integer.parseInt(responseString);
                }
            });
        }
    }


}
