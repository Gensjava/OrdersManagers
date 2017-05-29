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
import ua.com.it_st.ordersmanagers.interfaces.implems.DocListPayAction;
import ua.com.it_st.ordersmanagers.models.Pays;
import ua.com.it_st.ordersmanagers.sqlTables.TableCounteragentsDebtDocs;
import ua.com.it_st.ordersmanagers.sqlTables.TableCurrencies;

/**
 * Created by Gena on 2017-05-14.
 */

public class SelectPayDocOrdersAdapter extends SimpleCursorAdapter {
    private LayoutInflater mLInflater;
    private DocListPayAction docListPayAction;
    private Pays pays;

    public SelectPayDocOrdersAdapter(final Context context, final int layout, final Cursor c, final String[] from, final int[] to, final int flags, Pays pays) {
        super(context, layout, c, from, to, flags);
        this.mLInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.docListPayAction = new DocListPayAction(pays.getPaysLines());
        this.pays = pays;
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
        final double cDebet = itemCursor.getDouble(itemCursor.getColumnIndex(TableCounteragentsDebtDocs.COLUMN_DEBT));
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

            viewHolder.payLines = new Pays.PaysLines(pays.getId(), viewHolder.date.getText().toString(), viewHolder.number.getText().toString(), 0, сСurrency);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.date.setText(cDate);
        viewHolder.total.setText(cTotal);
        viewHolder.currency.setText(сСurrency);
        viewHolder.number.setText(cNumber);
        viewHolder.debet.setText(String.valueOf(cDebet));
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

                    viewHolder.payLines.setSum(Double.valueOf(viewHolder.pay_summa_pay.getText().toString()));
                    docListPayAction.add(viewHolder.payLines);

                } else {
                    viewHolder.pay_summa_pay.setText("");
                    docListPayAction.delete(viewHolder.payLines);
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
        Pays.PaysLines payLines;
    }
}

