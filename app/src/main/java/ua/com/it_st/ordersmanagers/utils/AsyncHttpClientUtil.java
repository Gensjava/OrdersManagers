package ua.com.it_st.ordersmanagers.utils;


import android.net.http.AndroidHttpClient;
import android.os.Environment;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.fragmets.LoadFilesFragment;

public class AsyncHttpClientUtil extends AsyncHttpClient {

    private final String TEG = AsyncHttpClientUtil.class.getSimpleName();
    //private String mBaseUrl = "http://192.168.1.7/Pekin/hs/file";
    //private static final String BASE_URL = "http://10.0.3.2/Pekin/hs/file";
    private String mBaseUrl;
    private MainActivity mMainActivity;

    public AsyncHttpClientUtil(final MainActivity mainActivity, String url) throws Exception {
        mMainActivity = mainActivity;
        mBaseUrl = url;
    }

    public void getDownloadFiles(final RequestParams params, final String fileName) throws Exception {

        get(mBaseUrl, params, new FileAsyncHttpResponseHandler(mMainActivity) {
            @Override
            public void onFailure(final int statusCode, final Header[] headers, final Throwable throwable, final File file) {
                if (statusCode != HttpStatus.SC_OK) {
                    //Log
                    ErrorInfo.setmLogLine("Загрузка файла ", params.toString(), true, TEG + " Failure: Код ошибки " + statusCode);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File response) {
                // Do something with the file `response`
                if (statusCode == HttpStatus.SC_OK) {
                    try {

                        final LoadFilesFragment fragment = (LoadFilesFragment) mMainActivity.getSupportFragmentManager().findFragmentByTag(LoadFilesFragment.class.toString());
                        if (fragment != null) {
                            /**/
                            fragment.onInsertTable(response, fileName);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        //Log
                        ErrorInfo.setmLogLine("Загрузка файла ", fileName, true, TEG + " " + e.toString());
                    }
                } else {
                    //Log
                    ErrorInfo.setmLogLine("Загрузка файла ", fileName, true, TEG + "Success: Код ошибки " + statusCode);
                }
            }
        });
    }

    public void postUnloadFiles() throws Exception {

        File myFile = new File("data/data/ua.com.it_st.ordersmanagers", "doc_orders.csv");
        RequestParams params = new RequestParams();
        try {
            params.put("doc_orders", myFile);
        } catch (FileNotFoundException e) {
        }

        post(mBaseUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
                // handle success response
                Log.i("headersSuccess", "" + statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {
                // handle failure response
                Log.i("headersFailure", "" + statusCode);
            }
        });

    }
}
