package ua.com.it_st.ordersmanagers.fragmets;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.http.Header;
import org.apache.http.HttpStatus;

import java.io.File;

import ua.com.it_st.ordersmanagers.MainActivity;
import ua.com.it_st.ordersmanagers.R;

import ua.com.it_st.ordersmanagers.utils.ErrorInfo;
import ua.com.it_st.ordersmanagers.utils.UtilAsyncHttpClient;
import ua.com.it_st.ordersmanagers.utils.UtilsWorkFiles;

/**
 * Created by Gens on 19.07.2015.
 */
public class ExchangeFragment extends Fragment implements View.OnClickListener {

    TextView exchegeData;
    TextView exchegeStatus;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.exchange_layout, container,
                false);
        exchegeData = (TextView) rootView.findViewById(R.id.exchege_text_data);
        exchegeStatus = (TextView) rootView.findViewById(R.id.exchege_text_string_status);

        ImageView BHost = (ImageView) rootView.findViewById(R.id.exchege_image_button);
        BHost.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.exchege_image_button:

                UtilAsyncHttpClient utilAsyncHttpClient = new UtilAsyncHttpClient((MainActivity) getActivity());
                utilAsyncHttpClient.setBasicAuth("admin", "123");

                String[] nameFile = getResources().getStringArray(R.array.name_file_data);

                for (String i : nameFile) {

                    RequestParams params = new RequestParams();
                    params.put("NameFile", i.toString());

                    try {
                        utilAsyncHttpClient.getDownloadFiles(params);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            default:
                break;
        }
    }


}
