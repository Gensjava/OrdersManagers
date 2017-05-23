package ua.com.it_st.ordersmanagers.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.fragmets.PayDocSelectOrders;
import ua.com.it_st.ordersmanagers.sqlTables.TableCounteragentsDebtDocs;
import ua.com.it_st.ordersmanagers.sqlTables.TableCurrencies;
import ua.com.it_st.ordersmanagers.utils.LoaderDocFragment;

/**
 * Created by Gena on 2017-05-14.
 */

public class SelectPayDocOrdersAdapter extends SimpleCursorAdapter {
    private LayoutInflater mLInflater;
    private LoaderDocFragment loaderDocFragment;

    public SelectPayDocOrdersAdapter(final Context context, final int layout, final Cursor c, final String[] from, final int[] to, final int flags, PayDocSelectOrders loaderDocFragment) {
        super(context, layout, c, from, to, flags);
        mLInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //  this.loaderDocFragment = loaderDocFragment;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        convertView = mLInflater.inflate(R.layout.pay_list_item, parent, false);
            /*позиция*/
        Cursor itemCursor = (Cursor) getItem(position);
        String sPosition = String.valueOf(position + 1);
            /*получаем колонки*/

        final String cDate = itemCursor.getString(itemCursor.getColumnIndex(TableCounteragentsDebtDocs.COLUMN_DOC_DATE));
        final String cNumber = itemCursor.getString(itemCursor.getColumnIndex(TableCounteragentsDebtDocs.COLUMN_DOC_NUMBER));
        final String cDebet = itemCursor.getString(itemCursor.getColumnIndex(TableCounteragentsDebtDocs.COLUMN_DEBT));
        final String сСurrency = itemCursor.getString(itemCursor.getColumnIndex(TableCurrencies.COLUMN_NAME));
        final String cTotal = itemCursor.getString(itemCursor.getColumnIndex(TableCounteragentsDebtDocs.COLUMN_SUMMA));

            /*дата */
        final TextView date = (TextView) convertView.findViewById(R.id.pay_doc_data);
        date.setText(cDate);
        /*сумма*/
        final TextView total = (TextView) convertView.findViewById(R.id.pay_summa);
        total.setText(cTotal);
        //валюта
        final TextView currency = (TextView) convertView.findViewById(R.id.pay_currency);
        currency.setText(сСurrency);
             /*номер*/
        final TextView number = (TextView) convertView.findViewById(R.id.pay_doc_number);
        number.setText(cNumber);
            /*долг*/
        final TextView debet = (TextView) convertView.findViewById(R.id.pay_summa_debet);
        debet.setText(cDebet);

        final TextView pay_number = (TextView) convertView.findViewById(R.id.pay_number);
        pay_number.setText(sPosition);

        return convertView;
    }
}

