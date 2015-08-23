package ua.com.it_st.ordersmanagers.fragmets;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.enums.DocTypeEnum;
import ua.com.it_st.ordersmanagers.models.OrderDoc;
import ua.com.it_st.ordersmanagers.models.OrderDoc.OrderLines;
import ua.com.it_st.ordersmanagers.sqlTables.TableOrders;
import ua.com.it_st.ordersmanagers.sqlTables.TableOrdersLines;
import ua.com.it_st.ordersmanagers.utils.ConstantsUtil;
import ua.com.it_st.ordersmanagers.utils.DBHelperUtil;
import ua.com.it_st.ordersmanagers.utils.ErrorInfo;
import ua.com.it_st.ordersmanagers.utils.SQLiteOpenHelperUtil;

public class OrderNewCartFragment extends Fragment implements View.OnClickListener {

    private TextView tSumCart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.order_new_cart_list, container,
                false);

        /*создаем адаптер корзины*/
        final ArrayAdapter sAdapter = new MyArrayAdapter(getActivity(), R.layout.order_new_cart_list_item, getItemsGoods());

        ListView lv = (ListView) rootView.findViewById(R.id.order_new_cart_list_position);
        lv.setAdapter(sAdapter);

      /*выводим данные дату и номер в шапку*/
        TextView period = (TextView) rootView.findViewById(R.id.order_new_cart_period);
        period.setText("Заказ " + getString(R.string.rNumber) + ConstantsUtil.getsCurrentNumber() + " " + getString(R.string.rOf) + " " + ConstantsUtil.getDate());
         /* кнопка далее переход на следующий этап*/
        ImageView imViewAdd = (ImageView) rootView.findViewById(R.id.order_new_cart_list_image_arrow_right);
        imViewAdd.setOnClickListener(this);
        /*выводим сумму в подвале*/
        tSumCart = (TextView) rootView.findViewById(R.id.order_new_cart_list_sum);

        /*обновляем подвал*/
        upDataFooter();

        return rootView;
    }
    /*заполняем корзину в ArrayList*/
    private ArrayList getItemsGoods() {
        ArrayList<OrderDoc.OrderLines> lCartOrders = new ArrayList<OrderDoc.OrderLines>();

        OrderDoc.OrderLines[] cartOrders = ConstantsUtil.mCart.toArray(new OrderLines[ConstantsUtil.mCart.size()]);

        for (OrderDoc.OrderLines item : cartOrders) {
            lCartOrders.add(item);
        }
        return lCartOrders;
    }
    @Override
    public void onClick(final View view) {
        switch (view.getId()) {

            case R.id.order_new_cart_list_image_arrow_right:
               
                 /*проверяем пустая корзина или нет*/
                if (!ConstantsUtil.checkCartEmpty(getActivity())) {
                     /* создаем новый заказ */
                    onNewOrder();
                /* открываем журнал заказов */
                    final onEventListener someEventListener = (onEventListener) getActivity();
                    someEventListener.onOpenFragmentClass(OrderListFragment.class);
                }
                break;
            default:
                break;
        }
    }

    public void onNewOrder() {
        /* открываем подключение к БД*/
        SQLiteDatabase DB = SQLiteOpenHelperUtil.getInstance().getDatabase();
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
        private ArrayList<OrderDoc.OrderLines> mListItems;


        public MyArrayAdapter(final Context context, final int resource, final ArrayList<OrderDoc.OrderLines> objects) {
            super(context, resource, objects);
            mLInflater = LayoutInflater.from(context);
            mListItems = objects;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            convertView = mLInflater.inflate(R.layout.order_new_cart_list_item, parent, false);
            /*позиция*/
            final OrderDoc.OrderLines itemOrderLines = getItem(position);

             /* номер позиции */
            short iPosition = (short) position;
            String lNumber = String.valueOf(++iPosition);
            final TextView number = (TextView) convertView.findViewById(R.id.order_new_cart_list_item_number);
            number.setText(lNumber);
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

            /*menu*/
            final Spinner spinner = (Spinner) convertView.findViewById(R.id.order_new_cart_list_item_menu);

            /* Настраиваем адаптер */
            String[] spinnerMenu = getResources().getStringArray(R.array.spinner_cart_menu);
            /**/
            final MenuCustomAdapter adapter = new MenuCustomAdapter(getActivity(), R.layout.spinner_row, spinnerMenu);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            /* Вызываем адаптер */
            spinner.setAdapter(adapter);
            spinner.setSelection(2);/*устанавливаем заглушку*/

            /*костыль для того чтоб при начале открытия списка незаполнялось первая позиция
             и при выборе пользователем позицию в меню нормально обрабатывал
            */
            final int[] iCurrentSelection = {spinner.getSelectedItemPosition()};

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent,
                                           View itemSelected, int selectedItemPosition, long selectedId) {

                    if (iCurrentSelection[0] != selectedItemPosition) {

                        switch (selectedItemPosition) {
                            case 0:
                                /* удаляем товар*/
                                mListItems.remove(itemOrderLines);
                                ConstantsUtil.onListOrderLinesDelete(itemOrderLines);
                                /* обновляем */
                                notifyDataSetChanged();
                                upDataFooter();
                                ErrorInfo.Tost("Товар " + itemOrderLines.getName() + " удален из списка.", getActivity());
                                break;
                            case 1:

                                break;
                            default:
                                break;
                        }

                    } else {
                        iCurrentSelection[0] = selectedItemPosition;
                    }
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            return convertView;
        }
    }

    /* создаем свой адаптер к Spinner т.к по умолчанию нет возможности
     добалять свои изображения в меню
      */
    private class MenuCustomAdapter extends ArrayAdapter {

        private String[] objects;
        private LayoutInflater mLInflater;

        public MenuCustomAdapter(Context context, int textViewResourceId,
                                 String[] objects) {
            super(context, textViewResourceId, objects);
            this.objects = objects;
            mLInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        @Override
        public int getCount() {
            return super.getCount() - 1; // делаем невидимым последний элемент
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            convertView = mLInflater.inflate(R.layout.spinner_row, parent, false);

            TextView label = (TextView) convertView.findViewById(R.id.spinner_row_item_text);
            label.setText(objects[position]);

            ImageView icon = (ImageView) convertView.findViewById(R.id.spinner_row_icon);

            switch (position) {
                case 0:
                    icon.setImageResource(R.mipmap.ic_no_held);
                    break;
                case 1:
                    icon.setImageResource(R.mipmap.ic_edit);
                    break;
                default:
                    break;
            }
            return convertView;
        }
    }

}
