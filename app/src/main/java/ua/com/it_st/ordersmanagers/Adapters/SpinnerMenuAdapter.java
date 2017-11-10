package ua.com.it_st.ordersmanagers.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ua.com.it_st.ordersmanagers.R;


public class SpinnerMenuAdapter extends ArrayAdapter {
    protected String[] objects;
    protected LayoutInflater mLInflater;

    public SpinnerMenuAdapter(Context context, int textViewResourceId,
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

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // TODO Auto-generated method stub
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(final int position, View convertView, ViewGroup parent) {

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
