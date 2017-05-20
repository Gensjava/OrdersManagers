package ua.com.it_st.ordersmanagers.fragmets;

import android.content.Context;
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
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.enums.DocTypeOperation;
import ua.com.it_st.ordersmanagers.interfaces.implems.UpDateDocList;
import ua.com.it_st.ordersmanagers.interfaces.implems.UpdateDocDB;
import ua.com.it_st.ordersmanagers.models.Orders;
import ua.com.it_st.ordersmanagers.models.Orders.OrderLines;
import ua.com.it_st.ordersmanagers.utils.Dialogs;
import ua.com.it_st.ordersmanagers.utils.InfoUtil;
import ua.com.it_st.ordersmanagers.utils.SQLiteOpenHelperUtil;

/* Класс предназначен показа корзины*/
public class OrderNewCartFragment extends Fragment implements View.OnClickListener {

    private TextView tSumCart;
    private ArrayAdapter<OrderLines> sAdapter;
    private ArrayList<Orders.OrderLines> mListItems = new ArrayList<>();
    private SQLiteDatabase DB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.order_new_cart_list, container,
                false);

        mListItems = UpDateDocList.getItemsGoods();
        /*создаем адаптер корзины*/
        sAdapter = new MyArrayAdapter(getActivity(), R.layout.order_new_cart_list_item, mListItems);

        ListView lv = (ListView) rootView.findViewById(R.id.order_new_cart_list_position);
        lv.setAdapter(sAdapter);

                /* открываем подключение к БД*/
        DB = SQLiteOpenHelperUtil.getInstance().getDatabase();
        /**/
        String numberDoc = "";
        String dateDoc = "";

        if (UpdateDocDB.mCurrentOrder != null) {
            numberDoc = UpdateDocDB.mCurrentOrder.getDocNumber();
            dateDoc = UpdateDocDB.mCurrentOrder.getDocDate();

        TextView header = (TextView) rootView.findViewById(R.id.order_new_cart_list_header_root);

            if (UpdateDocDB.mCurrentOrder.getTypeOperation().equals(DocTypeOperation.EDIT)) {
            header.setText(getString(R.string.edit_cart));
            } else if (UpdateDocDB.mCurrentOrder.getTypeOperation().equals(DocTypeOperation.COPY)) {
            header.setText(getString(R.string.copy_cart));
        }
        }

      /*выводим данные дату и номер в шапку*/
        TextView period = (TextView) rootView.findViewById(R.id.order_new_cart_period);
        period.setText(getString(R.string.Order) + getString(R.string.rNumber) + numberDoc + " " + getString(R.string.rOf) + " " + dateDoc);
         /* кнопка далее переход на следующий этап*/
        ImageView imViewAdd = (ImageView) rootView.findViewById(R.id.order_new_cart_list_image_arrow_right);
        imViewAdd.setOnClickListener(this);
        /*выводим сумму в подвале*/
        tSumCart = (TextView) rootView.findViewById(R.id.order_new_cart_list_sum);

        /*обновляем подвал*/
        upDataFooter();

        return rootView;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {

            case R.id.order_new_cart_list_image_arrow_right:

                UpDateDocList lUpDateOrderList = new UpDateDocList();
                 /*проверяем пустая корзина или нет*/
                if (!lUpDateOrderList.isEmpty()) {
                    boolean bCheck;

                    UpdateDocDB lUpdateOrderDB = new UpdateDocDB(DB, getActivity());
                     /* создаем новый заказ */
                    if (UpdateDocDB.mCurrentOrder.getTypeOperation().equals(DocTypeOperation.NEW) ||
                            UpdateDocDB.mCurrentOrder.getTypeOperation().equals(DocTypeOperation.COPY)) {
                        bCheck = lUpdateOrderDB.add();
                    } else {
                        /*редактируем заказ*/
                        bCheck = lUpdateOrderDB.update();
                    }

                    if (bCheck) {
                      /* открываем журнал заказов */
                        final onEventListener someEventListener = (onEventListener) getActivity();
                        someEventListener.onOpenFragmentClass(OrderListDocFragment.class);
                        /*чистим корзину*/
                        UpDateDocList.mCart.clear();
                    }
                } else {
                    InfoUtil.showErrorAlertDialog(getString(R.string.car_empty), getString(R.string.order), getActivity());
                }
                break;
            default:
                break;
        }
    }

    /*обновление после выбора к-во товара в списке товаров*/
    public void setDialogAmount(final double numberInDialog, final double sumInDialog, final OrderLines product) {

        /* делаем проверку товара на остатке */
        if (product.getBalance() >= numberInDialog) {
            if (numberInDialog > 0) {
                 /* строка ТЧ заказа */
                /* к-во заказа, сумма */
                product.setAmount(numberInDialog);
                product.setSum(sumInDialog);
                /* редактируем табличную часть заказа */
                UpDateDocList lUpDateOrderList = new UpDateDocList();
                lUpDateOrderList.update(product);
            }
        } else {
            //
            InfoUtil.Tost(getString(R.string.not_goods_store_number), getActivity());
        }
        /*перезаписываем список товаров*/
        mListItems = UpDateDocList.getItemsGoods();
        /*обновляем*/
        sAdapter.notifyDataSetChanged();
        upDataFooter();
    }

    /*обновляем подвал*/
    private void upDataFooter() {

        UpDateDocList lUpDateOrderList = new UpDateDocList();
        double sum = lUpDateOrderList.sum();

          /*Показываем сумму заказа в подвале*/
        String tSum = sum == 0.0 ? getString(R.string.zero_point_text) : String.valueOf(sum);
        tSumCart.setText(tSum + getString(R.string.grn));
    }

    /* создаем класс - интефейс для открытия фрагментов */
    public interface onEventListener {
        void onOpenFragmentClass(Class<?> fClass);
    }

    /*создаем свой адаптер для списка товаров*/
    private class MyArrayAdapter extends ArrayAdapter<OrderLines> {

        private LayoutInflater mLInflater;

        public MyArrayAdapter(final Context context, final int resource, final ArrayList<Orders.OrderLines> objects) {
            super(context, resource, objects);
            mLInflater = LayoutInflater.from(context);
            mListItems = objects;
        }

        @Override
        public int getCount() {
            return mListItems.size();
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            convertView = mLInflater.inflate(R.layout.order_new_cart_list_item, parent, false);

            /*позиция*/
            final Orders.OrderLines itemOrderLines = mListItems.get(position);

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
            final Spinner spinner = (Spinner) convertView.findViewById(R.id.order_new_cart_list_item_menu_n);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    spinner.performClick();
                }
            });

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

                                UpDateDocList lUpDateOrderList = new UpDateDocList();
                                lUpDateOrderList.delete(itemOrderLines);

                                InfoUtil.Tost(getString(R.string.goods) + itemOrderLines.getName() + getString(R.string.delete_list), getActivity());
                                 /* обновляем */
                                notifyDataSetChanged();
                                upDataFooter();
                                /**/
                                UpdateDocDB.mCurrentOrder.setClickModifitsirovannoiCart(true);
                                break;
                            case 1:
                                Dialogs.showCustomAlertDialogEnterNumber(getActivity(), getString(R.string.addCart), itemOrderLines, OrderNewCartFragment.class.toString());
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
