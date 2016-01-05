package ua.com.it_st.ordersmanagers.fragmets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import java.io.File;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.utils.ConstantsUtil;
import ua.com.it_st.ordersmanagers.utils.InfoUtil;

public class PictureFragment extends FilesFragment {

    private ImageView imageView;
    private ProgressBar ui_bar;
    private File mFile;
    private boolean mLuck;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /* макет фрагмента */
        View rootView = inflater.inflate(R.layout.goods_picture, container,
                false);
        imageView = (ImageView) rootView.findViewById(R.id.goods_picture);
        ui_bar = (ProgressBar) rootView.findViewById(R.id.progress_bar_picture);

        /*подключаемся к серверу*/
        FilesFragment.ConnectServer connectData = ConstantsUtil.sConnectData;
        //
        if (connectData == null) {
            isError(getString(R.string.eror_conect));
            return rootView;
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            /*получаем код товара*/
            String goodsKod = bundle.getString(OrderNewGoodsFragment.GOODS_KOD);

            if (goodsKod != null) {
                RequestParams params = new RequestParams();
                params.put(OrderNewGoodsFragment.GOODS_KOD, goodsKod);

                try {
                /* загружаем файл */
                    connectData.getAsyncHttpClientUtil().getDownloadFilesPicture(params);
                } catch (Exception e) {
                    e.printStackTrace();
                    isError(getString(R.string.eroor_dowload_picture));
                    return rootView;
                }
            } else {
                isError(getString(R.string.eror_goods_kod));
            }
        }
        return rootView;
    }

    /*загружаем изображение*/
    public void getPicture(final File file) {
        mFile = file;
        Picasso.with(getActivity())
                .load(file)
                .error(R.mipmap.ic_error)
                .into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        ui_bar.setVisibility(View.INVISIBLE);
                        mLuck = true;
                    }

                    @Override
                    public void onError() {
                        isError(getString(R.string.eror_goods_kod));
                    }
                });
    }

    /*если ошибка*/
    public void isError(String erText) {

        InfoUtil.showErrorAlertDialog(erText, getString(R.string.updata), getActivity());
        imageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_error));
        ui_bar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mLuck) {
            mFile.delete();
        }
    }
}


