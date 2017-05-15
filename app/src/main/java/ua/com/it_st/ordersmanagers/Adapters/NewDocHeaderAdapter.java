package ua.com.it_st.ordersmanagers.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.fragmets.NewDocHeaderFragment;
import ua.com.it_st.ordersmanagers.fragmets.OrderNewHeaderFragment;
import ua.com.it_st.ordersmanagers.fragmets.OrderNewSelectHeaderFragment;
import ua.com.it_st.ordersmanagers.utils.Dialogs;

/**
 * Created by Gena on 2017-05-14.
 */

public class NewDocHeaderAdapter extends SimpleAdapter {

    private String[] mHeaderOrdersNameTable;
    private LayoutInflater mInflater;
    private ArrayList mHeaderOrders;
    private NewDocHeaderFragment newDocHeaderFragment;

    public NewDocHeaderAdapter(final Context context, final List<? extends Map<String, ?>> data, final int resource, final String[] from, final int[] to, NewDocHeaderFragment newDocHeaderFragment) {
        super(context, data, resource, from, to);

        mInflater = LayoutInflater.from(context);
        mHeaderOrders = (ArrayList) data;
        this.newDocHeaderFragment = newDocHeaderFragment;
        mHeaderOrdersNameTable = newDocHeaderFragment.getResources().getStringArray(R.array.header_orders_table);
    }

    @Override
    public Object getItem(final int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

            /*получаем заголовки шапки*/
        final Map<String, ?> items = (Map<String, ?>) mHeaderOrders.get(position);
        convertView = mInflater.inflate(R.layout.order_new_header_list_item, parent, false);

            /* заголовок*/
        final TextView header = (TextView) convertView.findViewById(R.id.order_header_list_item_text);
            /* позиция шапки */
        String itemP[] = newDocHeaderFragment.getmItemsHeader()[position];
            /*если параметр шапки не заполнен тогда устанвливаем заголовок*/
        if (itemP[0] == null) {
            header.setHint(items.get(newDocHeaderFragment.getString(R.string.title)).toString());
        } else {

            header.setText(itemP[0]);
                /**/
            String sub_text = null;
                /*заполняем шапку заказа*/
            if (position == 2) {
                sub_text = itemP[2];

                TextView sub_header = (TextView) convertView.findViewById(R.id.order_header_list_item_sub_text);
                sub_header.setVisibility(View.VISIBLE);
                sub_header.setText(sub_text);
            }

            newDocHeaderFragment.onfillOrder(position, itemP[1], sub_text);
                /* суб заголовок*/
        }

            /*клик на любом месте поля вызываем список занчений*/
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                newDocHeaderFragment.setmPosition(position);
                    /* если это не поле коментарий
                    * если комент вызываем диалог*/
                if (newDocHeaderFragment.getmPosition() != 4) {
                    Bundle bundleItem = new Bundle();
                    bundleItem.putString(newDocHeaderFragment.NAME_TABLE, mHeaderOrdersNameTable[position]);
                        /*открываем окно для выбора значения*/
                    final OrderNewHeaderFragment.onEventListener someEventListener = (OrderNewHeaderFragment.onEventListener) newDocHeaderFragment.getActivity();
                    someEventListener.onOpenFragmentClassBundle(OrderNewSelectHeaderFragment.class, bundleItem);
                } else {
                        /*вызывваем диалог для ввода комента*/
                    Dialogs.showCustomAlertDialogEditComment(newDocHeaderFragment.getActivity(), newDocHeaderFragment.getString(R.string.enter_your_coment));
                }
            }
        });
            /*устанвливаем аватар для каждого параметра шапки*/
        ImageView imageView = (ImageView) convertView.findViewById(R.id.order_header_list_item_image_avatar);
        imageView.setImageResource((Integer) items.get(newDocHeaderFragment.getString(R.string.imageAvatar)));

        return convertView;
    }
}


