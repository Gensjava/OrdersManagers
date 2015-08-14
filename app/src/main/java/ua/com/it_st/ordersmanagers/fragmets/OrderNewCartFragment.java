package ua.com.it_st.ordersmanagers.fragmets;

import android.database.sqlite.SQLiteDatabase;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.models.Order;
import ua.com.it_st.ordersmanagers.models.Order.OrderLines;
import ua.com.it_st.ordersmanagers.sqlTables.TableOrders;
import ua.com.it_st.ordersmanagers.sqlTables.TableOrdersLines;
import ua.com.it_st.ordersmanagers.utils.ConstantsUtil;
import ua.com.it_st.ordersmanagers.utils.DBHelperUtil;
import ua.com.it_st.ordersmanagers.utils.SQLiteOpenHelperUtil;

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

        ListView lv = (ListView) rootView.findViewById(R.id.order_new_cart_list_position);
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
        //корзина
        Order.OrderLines[] cartOrders = ConstantsUtil.mCart.toArray(new OrderLines[ConstantsUtil.mCart.size()]);

        List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();

        for (Order.OrderLines s : cartOrders) {

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", s.getName());
            map.put("imageAvatar", R.mipmap.ic_coment);
            items.add(map);
        }
        return items;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {

            case R.id.order_new_cart_list_image_arrow_right:
                //создаем новый заказ
                onNewOrder();
                //открываем журнал заказов
                final onEventListener someEventListener = (onEventListener) getActivity();
                someEventListener.onOpenFragmentClass(OrderListFragment.class);
                break;
            default:
                break;
        }
    }

    public void onNewOrder() {
        // открываем подключение к БД
        SQLiteDatabase DB = SQLiteOpenHelperUtil.getInstance().getDatabase();
        //шапка
        DBHelperUtil dbOrder = new DBHelperUtil(DB, TableOrders.TABLE_NAME);
        dbOrder.insert(TableOrders.getContentValues(ConstantsUtil.mCurrentOrder));
        //табличная часть
        // DBHelperUtil dbOrderLine = new DBHelperUtil(DB, TableOrdersLines.TABLE_NAME);
        //
        // Iterator <Order.OrderLines> itr = ConstantsUtil.mCart.iterator();
        //  while (itr.hasNext()) {
        //      dbOrderLine.insert(TableOrdersLines.getContentValues(itr.next(), ConstantsUtil.mCurrentOrder.getId()));
        // }

        DB.close();
    }

    public interface onEventListener {
        void onOpenFragmentClass(Class<?> fClass);
    }
}
