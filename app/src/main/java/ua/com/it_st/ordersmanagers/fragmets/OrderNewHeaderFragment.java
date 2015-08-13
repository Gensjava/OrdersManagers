package ua.com.it_st.ordersmanagers.fragmets;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

    public static final String NAME_TABLE = "NAME_TABLE";
    private TextView textView;
    private SimpleAdapter adapter;
    private View rootView;
    private String[] mItem;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.order_new_header_list, container,
                    false);

            //создаем адаптер
            adapter = new MySimpleAdapter(getActivity(), createList(),
                    R.layout.order_new_header_list_item,
                    new String[]{"title", "imageAvatar"},
                    new int[]{R.id.order_header_list_item_text, R.id.order_header_list_item_image_avatar});

            ListView lv = (ListView) rootView.findViewById(R.id.order_new_header_list_position);
            lv.setAdapter(adapter);

            ImageView imViewAdd = (ImageView) rootView.findViewById(R.id.order_new_header_list_image_arrow_right);
            imViewAdd.setOnClickListener(this);
        }

        return rootView;
    }

    private List<Map<String, ?>> createList() {
        //иконки к шапке заказа
        Integer[] mPictures = new Integer[]
                {R.mipmap.ic_organization,
                        R.mipmap.ic_stores,
                        R.mipmap.ic_client,
                        R.mipmap.ic_tipe_price,
                        R.mipmap.ic_coment
                };
        String[] headerOrders = getResources().getStringArray(R.array.header_orders);
        //список параметров шапки заказа
        List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();
        byte x = 0;
        for (String s : headerOrders) {

            Map<String, Object> map = new HashMap<String, Object>();
            map.put(getString(R.string.title), s);
            map.put(getString(R.string.imageAvatar), mPictures[x]);
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

    public void SetSelectUpdate(final String[] item) {

        mItem = item;
        adapter.notifyDataSetChanged();
    }

    public interface onEventListener {

        void onOpenFragmentClass(Class<?> fClass);
        void onOpenFragmentClassBundle(Class<?> fClass, Bundle bundleItem);
    }

    private class MySimpleAdapter extends SimpleAdapter {

        String[] headerOrdersNameTable = getResources().getStringArray(R.array.header_orders_table);
        private LayoutInflater mInflater;
        private ArrayList mHeaderOrders;
        private Context mContext;
        private int mPosition;

        public MySimpleAdapter(final Context context, final List<? extends Map<String, ?>> data, final int resource, final String[] from, final int[] to) {
            super(context, data, resource, from, to);
            mInflater = LayoutInflater.from(context);
            mHeaderOrders = (ArrayList) data;
            mContext = context;
        }

        @Override
        public Object getItem(final int position) {
            return super.getItem(position);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            Map<String, ?> items = (Map<String, ?>) mHeaderOrders.get(position);
            convertView = mInflater.inflate(R.layout.order_new_header_list_item, parent, false);

            textView = (TextView) convertView.findViewById(R.id.order_header_list_item_text);

            textView.setHint(items.get(getString(R.string.title)).toString());

            if (mItem != null & mPosition == position) {
                textView.setText(mItem[0]);
            }

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    Bundle bundleItem = new Bundle();
                    bundleItem.putString(NAME_TABLE, headerOrdersNameTable[position]);

                    mPosition = position;

                    final onEventListener someEventListener = (onEventListener) getActivity();
                    someEventListener.onOpenFragmentClassBundle(OrderNewSelectHeaderFragment.class, bundleItem);
                }
            });

            ImageView imageView = (ImageView) convertView.findViewById(R.id.order_header_list_item_image_avatar);
            imageView.setImageResource((Integer) items.get(getString(R.string.imageAvatar)));

            return convertView;
        }
    }
}
