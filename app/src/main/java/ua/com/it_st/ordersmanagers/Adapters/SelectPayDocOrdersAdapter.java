package ua.com.it_st.ordersmanagers.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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

        final ViewHolder viewHolder;
            /*позиция*/
        Cursor itemCursor = (Cursor) getItem(position);
        String sPosition = String.valueOf(position + 1);
            /*получаем колонки*/
        final String cDate = itemCursor.getString(itemCursor.getColumnIndex(TableCounteragentsDebtDocs.COLUMN_DOC_DATE));
        final String cNumber = itemCursor.getString(itemCursor.getColumnIndex(TableCounteragentsDebtDocs.COLUMN_DOC_NUMBER));
        final String cDebet = itemCursor.getString(itemCursor.getColumnIndex(TableCounteragentsDebtDocs.COLUMN_DEBT));
        final String сСurrency = itemCursor.getString(itemCursor.getColumnIndex(TableCurrencies.COLUMN_NAME));
        final String cTotal = itemCursor.getString(itemCursor.getColumnIndex(TableCounteragentsDebtDocs.COLUMN_SUMMA));

        if (convertView == null) {

            convertView = mLInflater.inflate(R.layout.pay_list_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.date = (TextView) convertView.findViewById(R.id.pay_doc_data);
            viewHolder.total = (TextView) convertView.findViewById(R.id.pay_summa);
            viewHolder.currency = (TextView) convertView.findViewById(R.id.pay_currency);
            viewHolder.number = (TextView) convertView.findViewById(R.id.pay_doc_number);
            viewHolder.debet = (TextView) convertView.findViewById(R.id.pay_summa_debet);
            viewHolder.pay_number = (TextView) convertView.findViewById(R.id.pay_number);
            viewHolder.pay_checkBox = (CheckBox) convertView.findViewById(R.id.pay_checkBox);
            viewHolder.pay_summa_pay = (EditText) convertView.findViewById(R.id.pay_summa_pay);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.date.setText(cDate);
        viewHolder.total.setText(cTotal);
        viewHolder.currency.setText(сСurrency);
        viewHolder.number.setText(cNumber);
        viewHolder.debet.setText(cDebet);
        viewHolder.pay_number.setText(sPosition);
        //
        OnClickChekBox(viewHolder);

        return convertView;
    }

    private void OnClickChekBox(final ViewHolder viewHolder) {

        viewHolder.pay_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    viewHolder.pay_summa_pay.setText(viewHolder.debet.getText());
                } else {
                    viewHolder.pay_summa_pay.setText("");
                }
            }
        });
    }

    private static class ViewHolder {
        TextView date;
        TextView total;
        TextView currency;
        TextView number;
        TextView debet;
        TextView pay_number;
        CheckBox pay_checkBox;
        EditText pay_summa_pay;
    }
}

