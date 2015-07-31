package ua.com.it_st.ordersmanagers.utils;

import android.database.sqlite.SQLiteDatabase;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import java.io.File;
import java.io.IOException;

import ua.com.it_st.ordersmanagers.activiteies.MainActivity;

/**
 * Created by Gens on 25.07.2015.
 */
public class AsyncHttpClientUtil extends AsyncHttpClient {

    private static final String BASE_URL = "http://10.0.3.2/Pekin/hs/file";
    private final String TEG = getClass().getName();
    private MainActivity mMainActivity;

    public AsyncHttpClientUtil(final MainActivity mainActivity) throws Exception {
        mMainActivity = mainActivity;
    }

    public void getDownloadFiles(final RequestParams params, final SQLiteDatabase db) throws Exception {

        get(BASE_URL, params, new FileAsyncHttpResponseHandler(mMainActivity) {
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
                        WorkFilesUtil.onInsertTable(response, params.toString(), db);
                    } catch (IOException e) {
                        e.printStackTrace();
                        //Log
                        ErrorInfo.setmLogLine("Загрузка файла ", params.toString(), true, TEG + " " + e.toString());
                    }
                } else {
                    //Log
                    ErrorInfo.setmLogLine("Загрузка файла ", params.toString(), true, TEG + "Success: Код ошибки " + statusCode);
                }
            }
        });
    }

}
