package ua.com.it_st.ordersmanagers.fragmets;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import ua.com.it_st.ordersmanagers.R;

/**
 * Created by Gens on 01.08.2015.
 */
public class OrderSelectedListFragment extends Fragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.order_header_list, container,
                false);
        //
//        ArrayList<HashMap<String, String>> myArrList = new ArrayList<HashMap<String, String>>();
//
//        SimpleAdapter adapter;
//        adapter = new SimpleAdapter(getActivity(), myArrList,
//                android.R.layout.simple_list_item_2,
//                new String[] {"title", "vendor"},
//                new int[] {android.R.id.text1, android.R.id.text2});
//
//        ListView lv = (ListView) rootView.findViewById(R.id.order_heander_list_position);
//        lv.setAdapter(adapter);
//
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
//
//            }
//        });
//
//        ImageView imViewAdd = (ImageView) rootView.findViewById(R.id.order_heander_image_plus);
//        imViewAdd.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {

            // case R.id.order_heander_image_plus:

            // break;
            default:
                break;
        }

    }
}
