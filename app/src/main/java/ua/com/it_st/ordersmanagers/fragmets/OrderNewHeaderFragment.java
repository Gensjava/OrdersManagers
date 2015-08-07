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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.com.it_st.ordersmanagers.R;

/**
 * Created by Gens on 01.08.2015.
 */
public class OrderNewHeaderFragment extends Fragment implements View.OnClickListener {

    public static final String KEY_ITEM = "KEY_ITEM";
    String[] headerOrders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.order_new_header_list, container,
                false);

        //параметры шапки
        headerOrders = getResources().getStringArray(R.array.header_orders);
        //создаем адаптер
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

                TextView textView = (TextView) view.findViewById(R.id.order_header_list_item_text);
                textView.setText("sssssssss");

                Bundle bundleItem = new Bundle();
                bundleItem.putString(KEY_ITEM, headerOrders[i]);

                final onEventListener someEventListener = (onEventListener) getActivity();
                someEventListener.onOpenFragmentClassBundle(OrderNewSelectHeaderFragment.class, bundleItem);
            }
        });

        ImageView imViewAdd = (ImageView) rootView.findViewById(R.id.order_new_header_list_image_arrow_right);
        imViewAdd.setOnClickListener(this);

        return rootView;
    }

    public List<Map<String, ?>> createList() {
        //иконки к шапке заказа
        Integer[] mPictures = new Integer[]
                {R.mipmap.ic_organization,
                        R.mipmap.ic_stores,
                        R.mipmap.ic_client,
                        R.mipmap.ic_tipe_price,
                        R.mipmap.ic_coment
                };
        //список параметров шапки заказа
        List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();
        byte x = 0;
        for (String s : headerOrders) {

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", s);
            map.put("imageAvatar", mPictures[x]);
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
                someEventListener.onOpenFragmentClass(OrderNewGoodsFragment.class);
                break;
            default:
                break;
        }
    }

    public interface onEventListener {
        void onOpenFragmentClass(Class<?> fClass);

        void onOpenFragmentClassBundle(Class<?> fClass, Bundle bundleItem);
    }
}
