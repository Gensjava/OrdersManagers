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
import ua.com.it_st.ordersmanagers.fragmets.HeaderDoc;
import ua.com.it_st.ordersmanagers.fragmets.OrderNewSelectHeaderFragment;
import ua.com.it_st.ordersmanagers.models.Catalogs;
import ua.com.it_st.ordersmanagers.models.Counteragents;
import ua.com.it_st.ordersmanagers.utils.Dialogs;

/**
 * Created by Gena on 2017-05-14.
 */

public class HeaderDocAdapter extends SimpleAdapter {

    private LayoutInflater mInflater;
    private ArrayList mHeaderOrders;
    private HeaderDoc headerDoc;

    public HeaderDocAdapter(final Context context, final List<? extends Map<String, ?>> data, final int resource, final String[] from, final int[] to, HeaderDoc headerDoc) {
        super(context, data, resource, from, to);

        mInflater = LayoutInflater.from(context);
        mHeaderOrders = (ArrayList) data;
        this.headerDoc = headerDoc;
    }

    @Override
    public Object getItem(final int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        convertView = mInflater.inflate(R.layout.order_new_header_list_item, parent, false);
        /* заголовок*/
        final TextView header = (TextView) convertView.findViewById(R.id.order_header_list_item_text);
        /*получаем заголовки шапки*/
        final Map<String, ?> itemsCatalogs = (Map<String, ?>) mHeaderOrders.get(position);
               /*устанвливаем аватар для каждого параметра шапки*/
        ImageView imageView = (ImageView) convertView.findViewById(R.id.order_header_list_item_image_avatar);
        // imageView.setImageResource((Integer) itemsCatalogs.get(R.string.imageAvatar));
        /* позиция шапки */
        final Object object = itemsCatalogs.get(String.valueOf(position));
        String sub_address = null;

        /*если параметр шапки не заполнен тогда устанвливаем заголовок*/
        if (object instanceof Catalogs) {
            Catalogs catalogs = (Catalogs) object;

            header.setHint(catalogs.nameNativeLanguage);
            header.setText(catalogs.getName());

            if (catalogs instanceof Counteragents) {
                Counteragents counteragent = (Counteragents) catalogs;
                sub_address = counteragent.getAddress();

                TextView sub_header = (TextView) convertView.findViewById(R.id.order_header_list_item_sub_text);
                sub_header.setVisibility(View.VISIBLE);
                sub_header.setText(sub_address);
            }
            headerDoc.onfillOrder(position, catalogs.getKod(), sub_address);
        } else {

            if (object.toString().equals("")) {
                header.setHint("Комментарий");
            } else {
                header.setText(object.toString());
            }
        }

            /*клик на любом месте поля вызываем список занчений*/
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                    /* если это не поле коментарий
                    * если комент вызываем диалог*/
                if (!(object.getClass() == String.class)) {

                    Bundle bundleItem = new Bundle();
                    bundleItem.putString(HeaderDoc.NAME_TABLE, object.getClass().getSimpleName());
                    bundleItem.putString(HeaderDoc.NAME_TAG, headerDoc.getTag());
                    bundleItem.putString(HeaderDoc.ID_POSITION, String.valueOf(position));
                    bundleItem.putString(HeaderDoc.NAME_CLASS, object.getClass().getName());

                    /*открываем окно для выбора значения*/
                    final HeaderDoc.onEventListener someEventListener = (HeaderDoc.onEventListener) headerDoc.getActivity();
                    someEventListener.onOpenFragmentClassBundle(OrderNewSelectHeaderFragment.class, bundleItem);
                } else {
                        /*вызывваем диалог для ввода комента*/
                    Dialogs.showCustomAlertDialogEditComment(headerDoc.getActivity(), headerDoc.getString(R.string.enter_your_coment));
                }
            }
        });

        return convertView;
    }
}


