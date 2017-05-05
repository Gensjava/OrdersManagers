package ua.com.it_st.ordersmanagers.fragmets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.utils.SQLQuery;

/**
 * Created by Gena on 2017-05-04.
 */
public class PayDocListFragment extends OrderListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mQuerysql = SQLQuery.queryPayDocSum("PayDoc.type  <> ?");

        super.onCreateView(inflater, container, savedInstanceState);
        heander_jurnal.setText(R.string.JurnalPayDoc);

        return rootView;


    }
}
