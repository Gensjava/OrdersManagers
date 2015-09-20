package ua.com.it_st.ordersmanagers.utils;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import java.io.File;
import java.io.IOException;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.fragmets.FilesLoadFragment;

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
                    InfoUtil.setmLogLine("Загрузка файла ", params.toString(), true, TEG + " Failure: Код ошибки " + statusCode);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File response) {
                // Do something with the file `response`
                if (statusCode == HttpStatus.SC_OK) {
                    try {

                        final FilesLoadFragment fragment = (FilesLoadFragment) mMainActivity.getSupportFragmentManager().findFragmentByTag(FilesLoadFragment.class.toString());
                        if (fragment != null) {
                            /**/
                            fragment.onInsertTable(response, fileName);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        //Log
                        InfoUtil.setmLogLine("Загрузка файла ", fileName, true, TEG + " " + e.toString());
                    }
                } else {
                    //Log
                    InfoUtil.setmLogLine("Загрузка файла ", fileName, true, TEG + "Success: Код ошибки " + statusCode);
                }
            }
        });
    }

    public void postUnloadFiles(final RequestParams params, final String fileName) throws Exception {

        post(mBaseUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
                // handle success response
                if (statusCode == HttpStatus.SC_OK) {
                    //Log
                    InfoUtil.setmLogLine("Выгрузка файла ", fileName, true, TEG + " " + bytes.toString());
                } else {
                    //Log
                    InfoUtil.setmLogLine("Выгрузка файла ", fileName, true, TEG + "Success: Код ошибки " + statusCode);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {
                if (statusCode != HttpStatus.SC_OK) {
                    //Log
                    InfoUtil.setmLogLine("Выгрузка файла ", params.toString(), true, TEG + " Failure: Код ошибки " + statusCode);
                }
            }
        });

    }

}
