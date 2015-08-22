package ua.com.it_st.ordersmanagers.fragmets;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.models.OrderDoc;
import ua.com.it_st.ordersmanagers.models.OrderDoc.OrderLines;
import ua.com.it_st.ordersmanagers.sqlTables.TableOrders;
import ua.com.it_st.ordersmanagers.sqlTables.TableOrdersLines;
import ua.com.it_st.ordersmanagers.utils.ConstantsUtil;
import ua.com.it_st.ordersmanagers.utils.DBHelperUtil;
import ua.com.it_st.ordersmanagers.utils.SQLiteOpenHelperUtil;

public class OrderNewCartFragment extends Fragment implements View.OnClickListener {
    private TextView tSumCart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.order_new_cart_list, container,
                false);
        /* формируем столбцы сопоставления */
        String[] from = new String[]{};
        int[] to = new int[]{};
        /* корзина */
        OrderDoc.OrderLines[] cartOrders = ConstantsUtil.mCart.toArray(new OrderLines[ConstantsUtil.mCart.size()]);
        /*создаем адаптер корзины*/
        ArrayAdapter sAdapter = new MyArrayAdapter(getActivity(), R.layout.order_new_cart_list_item, cartOrders);

        ListView lv = (ListView) rootView.findViewById(R.id.order_new_cart_list_position);
        lv.setAdapter(sAdapter);

      /*выводим данные дату и номер в шапку*/
        TextView period = (TextView) rootView.findViewById(R.id.order_new_cart_period);
        period.setText("Заказ " + getString(R.string.rNumber) + ConstantsUtil.getsCurrentNumber() + " " + getString(R.string.rOf) + " " + ConstantsUtil.getDate());

        ImageView imViewAdd = (ImageView) rootView.findViewById(R.id.order_new_cart_list_image_arrow_right);
        imViewAdd.setOnClickListener(this);
        /*выводим сумму в подвале*/
        tSumCart = (TextView) rootView.findViewById(R.id.order_new_cart_list_sum);

        /*обновляем подвал*/
        upDataFooter();

        return rootView;
    }

    public List<OrderLines> createList() {
        /* корзина */
        OrderDoc.OrderLines[] cartOrders = ConstantsUtil.mCart.toArray(new OrderLines[ConstantsUtil.mCart.size()]);

        List<OrderDoc.OrderLines> items = new ArrayList<OrderDoc.OrderLines>();

        for (OrderDoc.OrderLines s : cartOrders) {

            items.add(s);
        }
        return items;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {

            case R.id.order_new_cart_list_image_arrow_right:
                /* создаем новый заказ */
                onNewOrder();
                /* открываем журнал заказов */
                final onEventListener someEventListener = (onEventListener) getActivity();
                someEventListener.onOpenFragmentClass(OrderListFragment.class);
                break;
            default:
                break;
        }
    }

    public void onNewOrder() {
        /* открываем подключение к БД*/
        SQLiteDatabase DB = SQLiteOpenHelperUtil.getInstance().getDatabase();
        //TableOrders.onDeleteValueTable(mDb);
        // TableOrdersLines.onDeleteValueTable(mDb);
        /* Делаем запись заказа
        * */
        /* шапка*/
        DBHelperUtil dbOrder = new DBHelperUtil(DB, TableOrders.TABLE_NAME);
        dbOrder.insert(TableOrders.getContentValues(ConstantsUtil.mCurrentOrder));
        /* табличная часть*/
        DBHelperUtil dbOrderLine = new DBHelperUtil(DB, TableOrdersLines.TABLE_NAME);
        /*создаем новые позиции заказа*/
        for (final OrderLines aMCart : ConstantsUtil.mCart) {
            dbOrderLine.insert(TableOrdersLines.getContentValues(aMCart, ConstantsUtil.mCurrentOrder.getId()));
        }
         /*чистим корзину*/
        ConstantsUtil.mCart.clear();
    }

    private void upDataFooter() {
          /*Показываем сумму заказа в подвале*/
        String tSum = ConstantsUtil.getTotalOrder() == 0.0 ? "0.00" : String.valueOf(ConstantsUtil.getTotalOrder());
        tSumCart.setText(tSum + " грн.");
    }
    public interface onEventListener {
        void onOpenFragmentClass(Class<?> fClass);
    }

    /*создаем свой адаптер для списка товаров*/
    private class MyArrayAdapter extends ArrayAdapter<OrderLines> {

        private LayoutInflater mLInflater;

        public MyArrayAdapter(final Context context, final int resource, final OrderLines[] objects) {
            super(context, resource, objects);
            mLInflater = LayoutInflater.from(context);
        }


        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            convertView = mLInflater.inflate(R.layout.order_new_cart_list_item, parent, false);
            /*позиция*/
            OrderDoc.OrderLines itemOrderLines = getItem(position);

             /* номер позиции */
            short iPosition = (short) position;
            String lNumber = String.valueOf(++iPosition);
            final TextView number = (TextView) convertView.findViewById(R.id.order_new_cart_list_item_number);
            number.setText(lNumber);
            /* код */
            String lKod = itemOrderLines.getGoodsId();
            final TextView kod = (TextView) convertView.findViewById(R.id.order_new_cart_list_item_kod);
            kod.setText(lKod);
             /* наименвоание товара */
            String lName = itemOrderLines.getName();
            final TextView name = (TextView) convertView.findViewById(R.id.order_new_cart_list_item_name);
            name.setText(lName);
            /* количество товара */
            String lAmount = String.valueOf(itemOrderLines.getAmount());
            final TextView amount = (TextView) convertView.findViewById(R.id.order_new_cart_list_item_name_amount);
            amount.setText(lAmount);
             /* цена товара */
            String lPrice = String.valueOf(itemOrderLines.getPrice());
            final TextView price = (TextView) convertView.findViewById(R.id.order_new_cart_list_item_price);
            price.setText(lPrice);
            /* сумма товара */
            String lSum = String.valueOf(itemOrderLines.getSum());
            final TextView sum = (TextView) convertView.findViewById(R.id.order_new_cart_list_item_sum);
            sum.setText(lSum);

            return convertView;
        }
    }
}
