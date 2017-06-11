package ua.com.it_st.ordersmanagers.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.enums.DocTypeOperation;
import ua.com.it_st.ordersmanagers.interfaces.implems.PayListDocAction;
import ua.com.it_st.ordersmanagers.models.Pays;
import ua.com.it_st.ordersmanagers.sqlTables.TableCounteragentsDebtDocs;
import ua.com.it_st.ordersmanagers.sqlTables.TableCurrencies;
import ua.com.it_st.ordersmanagers.sqlTables.TablePaysLines;

/**
 * Created by Gena on 2017-05-14.
 */

public class SelectPayDocOrdersAdapter extends SimpleCursorAdapter {
    private LayoutInflater mLInflater;
    private PayListDocAction payListDocAction;
    private Pays pays;

    public SelectPayDocOrdersAdapter(final Context context, final int layout, final Cursor c, final String[] from, final int[] to, final int flags, Pays pays) {
        super(context, layout, c, from, to, flags);
        this.mLInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.payListDocAction = new PayListDocAction(pays.getPaysLines());
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
        final String сСurrencyKod = itemCursor.getString(itemCursor.getColumnIndex(TableCurrencies.COLUMN_KOD));
        final String сСurrencyName = itemCursor.getString(itemCursor.getColumnIndex(TableCurrencies.COLUMN_NAME));
        final String cTotal = itemCursor.getString(itemCursor.getColumnIndex(TableCounteragentsDebtDocs.COLUMN_SUMMA));

        String cPay = getPay(itemCursor);

        if (convertView == null) {
            convertView = mLInflater.inflate(R.layout.pay_list_item, parent, false);
            viewHolder = getViewHolder(convertView, cDate, cNumber, сСurrencyKod);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.date.setText(cDate);
        viewHolder.total.setText(cTotal);
        viewHolder.currency.setText(сСurrencyName);
        viewHolder.number.setText(cNumber);
        viewHolder.debet.setText(String.valueOf(cDebet));
        viewHolder.pay_number.setText(sPosition);

        if (cPay != null) {
            viewHolder.pay_checkBox.setChecked(true);
            addpayLines(viewHolder, cPay);
        }

        OnClickChekBox(viewHolder);
        return convertView;
    }

    @Nullable
    private String getPay(Cursor itemCursor) {
        String cPay = null;
        if (pays.getmTypeOperation() == DocTypeOperation.EDIT
                || pays.getmTypeOperation() == DocTypeOperation.COPY) {
            cPay = itemCursor.getString(itemCursor.getColumnIndex(TablePaysLines.COLUMN_AMOUNT));
        }
        return cPay;
    }

    @NonNull
    private ViewHolder getViewHolder(View convertView, String cDate, String cNumber, String сСurrencyKod) {
        ViewHolder viewHolder;
        viewHolder = new ViewHolder();
        viewHolder.date = (TextView) convertView.findViewById(R.id.pay_doc_data);
        viewHolder.total = (TextView) convertView.findViewById(R.id.pay_summa);
        viewHolder.currency = (TextView) convertView.findViewById(R.id.pay_currency);
        viewHolder.number = (TextView) convertView.findViewById(R.id.pay_doc_number);
        viewHolder.debet = (TextView) convertView.findViewById(R.id.pay_summa_debet);
        viewHolder.pay_number = (TextView) convertView.findViewById(R.id.pay_number);
        viewHolder.pay_checkBox = (CheckBox) convertView.findViewById(R.id.pay_checkBox);
        viewHolder.pay_summa_pay = (EditText) convertView.findViewById(R.id.pay_summa_pay);

        viewHolder.payLines = new Pays.PaysLines(pays.getId(), cDate, cNumber, 0, сСurrencyKod);
        return viewHolder;
    }

    private void addpayLines(ViewHolder viewHolder, String cPay) {
        viewHolder.pay_summa_pay.setText(cPay);
        viewHolder.payLines.setSum(Double.valueOf(cPay));
        payListDocAction.add(viewHolder.payLines);
    }

    private void remove(ViewHolder viewHolder) {
        viewHolder.pay_summa_pay.setText("");
        payListDocAction.delete(viewHolder.payLines);
    }

    private void OnClickChekBox(final ViewHolder viewHolder) {
        viewHolder.pay_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    addpayLines(viewHolder, viewHolder.debet.getText().toString());
                } else {
                    remove(viewHolder);
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

