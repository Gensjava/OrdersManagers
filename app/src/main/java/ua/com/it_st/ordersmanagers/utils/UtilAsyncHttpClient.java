package ua.com.it_st.ordersmanagers.utils;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.json.JSONException;

import java.io.File;

import ua.com.it_st.ordersmanagers.MainActivity;

/**
 * Created by Gens on 25.07.2015.
 */
public class UtilAsyncHttpClient extends AsyncHttpClient {

    private static final String BASE_URL = "http://10.0.3.2/Pekin/hs/file";
    private MainActivity mMainActivity;

    public UtilAsyncHttpClient(final MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    public void getDownloadFiles(final RequestParams params) throws Exception {

        get(BASE_URL, params, new FileAsyncHttpResponseHandler(mMainActivity) {
            @Override
            public void onFailure(final int statusCode, final Header[] headers, final Throwable throwable, final File file) {
                if (statusCode != HttpStatus.SC_OK) {
                    ErrorInfo.showErrorAlertDialog("Данные не загрузились! имя файла " + params.toString(), mMainActivity);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File response) {
                // Do something with the file `response`
                if (statusCode == HttpStatus.SC_OK) {
                    UtilsWorkFiles.getContent(response);
                } else {
                    ErrorInfo.showErrorAlertDialog("Данные не загрузились! имя файла " + params.toString(), mMainActivity);
                }
            }
        });
    }

}
