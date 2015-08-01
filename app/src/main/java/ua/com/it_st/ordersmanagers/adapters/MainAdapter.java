package ua.com.it_st.ordersmanagers.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.com.it_st.ordersmanagers.R;

/**
 * Created by Gens on 31.07.2015.
 */
public class MainAdapter extends SimpleCursorAdapter {

    private Context mContext;
    private LayoutInflater lInflater;

    public MainAdapter(final Context context, final int layout, final Cursor c, final String[] from, final int[] to, final int flags) {
        super(context, layout, c, from, to, flags);
        mContext = context;
        lInflater = LayoutInflater.from(context);
    }


}
