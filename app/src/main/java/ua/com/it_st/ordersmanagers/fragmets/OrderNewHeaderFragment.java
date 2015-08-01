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
import java.util.List;
import java.util.Map;

import ua.com.it_st.ordersmanagers.R;

/**
 * Created by Gens on 01.08.2015.
 */
public class OrderNewHeaderFragment extends Fragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.order_new_header_list, container,
                false);
        //

        SimpleAdapter adapter;
        adapter = new SimpleAdapter(getActivity(), createSensorsList(),
                R.layout.order_new_header_list_item,
                new String[]{"title", "imageAvatar"},
                new int[]{R.id.order_header_list_item_text, R.id.order_header_list_item_image_avatar});

        ListView lv = (ListView) rootView.findViewById(R.id.order_heander_list_position);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {

            }
        });

        ImageView imViewAdd = (ImageView) rootView.findViewById(R.id.order_heander_image_plus);
        imViewAdd.setOnClickListener(this);

        return rootView;
    }

    private List<Map<String, ?>> createSensorsList() {

        String[] sensors = getResources().getStringArray(R.array.header_orders);

        Integer[] mPictures = new Integer[]
                {R.mipmap.ic_launcher_organization,
                        R.mipmap.ic_launcher_stores,
                        R.mipmap.ic_launcher_client,
                        R.mipmap.ic_launcher_tipe_price,
                        R.mipmap.ic_launcher_coment
                };
        List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();
        byte x = 0;
        for (String s : sensors) {

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", s.toString());
            map.put("imageAvatar", mPictures[x]);
            x++;
            items.add(map);
        }
        return items;
    }


    @Override
    public void onClick(final View view) {
        switch (view.getId()) {

            case R.id.order_heander_image_plus:

                break;
            default:
                break;
        }

    }
}
