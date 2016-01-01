package ua.com.it_st.ordersmanagers.fragmets;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.loopj.android.http.RequestParams;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.utils.InfoUtil;

public class PictureFragment extends FilesFragment {

    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /* макет фрагмента */
        View rootView = inflater.inflate(R.layout.goods_picture, container,
                false);
        imageView = (ImageView) rootView.findViewById(R.id.goods_picture);

                 /*подключаемся к серверу*/
        FilesFragment.ConnectServer connectData = new FilesFragment.ConnectServer(getActivity(), (byte) 2);

        /*подключились к базе или нет*/
        boolean lConnect = connectData.isMlConnect();
        //
        if (!lConnect) {
            //Log
            InfoUtil.setmLogLine(getString(R.string.action_conect_base), true, getString(R.string.error_login_password_inet));
        }

        RequestParams params = new RequestParams();

        try {
                /* загружаем файл */
            connectData.getAsyncHttpClientUtil().getDownloadFilesPicture(params);
        } catch (Exception e) {
            e.printStackTrace();
            //Log
            // InfoUtil.setmLogLine(getString(R.string.action_download_file), i, true, TEG + ": " + e.toString());
        }
        return rootView;
    }

    /* обработка кликов на кнопки */
    @Override
    public void onClick(final View view) {
        switch (view.getId()) {

            case R.id.button:
                 /*подключаемся к серверу*/
                FilesFragment.ConnectServer connectData = new FilesFragment.ConnectServer(getActivity(), (byte) 0);

        /*подключились к базе или нет*/
                boolean lConnect = connectData.isMlConnect();
                //
                if (!lConnect) {
                    //Log
                    InfoUtil.setmLogLine(getString(R.string.action_conect_base), true, getString(R.string.error_login_password_inet));
                    return;
                }

                RequestParams params = new RequestParams();

                try {
                /* загружаем файл */
                    connectData.getAsyncHttpClientUtil().getDownloadFilesPicture(params);
                } catch (Exception e) {
                    e.printStackTrace();
                    //Log
                    // InfoUtil.setmLogLine(getString(R.string.action_download_file), i, true, TEG + ": " + e.toString());
                }

                break;
            default:
                break;
        }
    }

    public void getPicture(String url) {

        imageView.setImageURI(Uri.parse(url));
    }
}


