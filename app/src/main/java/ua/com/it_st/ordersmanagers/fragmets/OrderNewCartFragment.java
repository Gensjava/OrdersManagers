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
import ua.com.it_st.ordersmanagers.utils.ConstantsUtil;

/**
 * Created by Gens on 01.08.2015.
 */
public class OrderNewCartFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.order_new_cart_list, container,
                false);

        SimpleAdapter adapter;
        adapter = new SimpleAdapter(getActivity(), createList(),
                R.layout.order_new_header_list_item,
                new String[]{"title", "imageAvatar"},
                new int[]{R.id.order_header_list_item_text, R.id.order_header_list_item_image_avatar});

        ListView lv = (ListView) rootView.findViewById(R.id.order_new_header_list_position);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {

            }
        });

        ImageView imViewAdd = (ImageView) rootView.findViewById(R.id.order_new_cart_list_image_arrow_right);
        imViewAdd.setOnClickListener(this);

        return rootView;
    }

    public List<Map<String, ?>> createList() {
        //параметры шапки
        String[] headerOrders = (String[]) ConstantsUtil.mCart.toArray();

        List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();
        byte x = 0;
        for (String s : headerOrders) {

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", s);
            map.put("imageAvatar", R.mipmap.ic_coment);
            x++;
            items.add(map);
        }
        return items;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {

            case R.id.order_new_header_list_image_arrow_right:
                final onEventListener someEventListener = (onEventListener) getActivity();
                someEventListener.someEvent(MainFragment.class);
                break;
            default:
                break;
        }
    }

    public interface onEventListener {
        void someEvent(Class tClass);
    }
}
