package ua.com.it_st.ordersmanagers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ua.com.it_st.ordersmanagers.R;

/**
 * Created by Gena on 2017-05-14.
 */

public class MenuCustomAdapter extends ArrayAdapter {
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

    public View getCustomView(final int position, View convertView, ViewGroup parent) {

        convertView = mLInflater.inflate(R.layout.spinner_row, parent, false);

        TextView label = (TextView) convertView.findViewById(R.id.spinner_row_item_text);
        label.setText(objects[position]);

        ImageView icon = (ImageView) convertView.findViewById(R.id.spinner_row_icon);

        switch (position) {
            case 0:
                icon.setImageResource(R.mipmap.ic_edit);
                break;
            case 1:
                icon.setImageResource(R.mipmap.ic_held);
                break;
            case 2:
                icon.setImageResource(R.mipmap.ic_no_held);
                break;
            case 3:
                icon.setImageResource(R.mipmap.ic_copy);
                break;
            default:
                break;
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return super.getCount() - 1; // делаем невидимым последний элемент
    }
}
