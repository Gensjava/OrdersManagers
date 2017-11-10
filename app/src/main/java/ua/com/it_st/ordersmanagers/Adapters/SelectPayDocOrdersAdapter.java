package ua.com.it_st.ordersmanagers.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.enums.DocTypeOperation;
import ua.com.it_st.ordersmanagers.interfaces.implems.PayListDocAction;
import ua.com.it_st.ordersmanagers.models.Pays;
import ua.com.it_st.ordersmanagers.sqlTables.TableCounteragentsDebtDocs;
import ua.com.it_st.ordersmanagers.sqlTables.TableCurrencies;
import ua.com.it_st.ordersmanagers.sqlTables.TablePaysLines;
import ua.com.it_st.ordersmanagers.utils.ConstantsUtil;


public class SelectPayDocOrdersAdapter extends SimpleCursorAdapter {
    public static final String VIEW_HOLDER = "VIEW_HOLDER";
    private LayoutInflater mLInflater;
    private PayListDocAction payListDocAction;
    private Pays pays;
    private TextView totalSum;
    private double totalPaysNat;
    private double totalPaysUsd;
    private OnItemClickListener onItemClickListener;
    private double cPay_nat;
    private double cPay_usd;

    public SelectPayDocOrdersAdapter(final Context context, final int layout, final Cursor c, final String[] from, final int[] to, final int flags, Pays pays, TextView totalSum) {
        super(context, layout, c, from, to, flags);
        this.mLInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.payListDocAction = new PayListDocAction(pays.getPaysLines());
        this.pays = pays;
        this.totalSum = totalSum;
        this.onItemClickListener = (SelectPayDocOrdersAdapter.OnItemClickListener) context;
        if (pays.getPaysLines().size() > 0) {
            pays.getPaysLines().clear();
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        totalPaysNat = 0;
        totalPaysUsd = 0;
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

        String LineId = getLineId(itemCursor);

        if (convertView == null) {
            convertView = mLInflater.inflate(R.layout.pay_list_item, parent, false);
            viewHolder = getViewHolder(convertView, cDate, cNumber, сСurrencyKod, LineId);
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

        OnClickSuma(viewHolder);
        getPay(itemCursor, viewHolder, position);
        addTotal();
        return convertView;
    }


    private void getPay(Cursor itemCursor, ViewHolder viewHolder, final int position) {
        if (pays.getmTypeOperation() == DocTypeOperation.EDIT
                || pays.getmTypeOperation() == DocTypeOperation.COPY) {
            cPay_nat = itemCursor.getDouble(itemCursor.getColumnIndex(TablePaysLines.COLUMN_AMOUNT_NAT));
            cPay_usd = itemCursor.getDouble(itemCursor.getColumnIndex(TablePaysLines.COLUMN_AMOUNT_USD));
        } else {
            if (pays.getPaysLines().size() > 0) {
                cPay_nat = pays.getPaysLines().get(position).getSum_nat();
                cPay_usd = pays.getPaysLines().get(position).getSum_usd();
            }
        }
        if (cPay_nat == 0) {
            viewHolder.pay_summa_pay_nat.setText("");
        } else {
            viewHolder.pay_summa_pay_nat.setText(String.valueOf(cPay_nat));
        }
        if (cPay_usd == 0) {
            viewHolder.pay_summa_pay_usd.setText("");
        } else {
            viewHolder.pay_summa_pay_usd.setText(String.valueOf(cPay_usd));
        }
        viewHolder.payLines.setSum_nat(cPay_nat);
        viewHolder.payLines.setSum_usd(cPay_usd);
    }

    @Nullable
    private String getLineId(Cursor itemCursor) {
        String cLineId;
        if (pays.getmTypeOperation() == DocTypeOperation.EDIT
                || pays.getmTypeOperation() == DocTypeOperation.COPY) {
            cLineId = itemCursor.getString(itemCursor.getColumnIndex(TablePaysLines.COLUMN_LINE_ID));
        } else {
            cLineId = String.valueOf(UUID.randomUUID());
        }
        return cLineId;
    }

    @NonNull
    private ViewHolder getViewHolder(View convertView, String cDate, String cNumber, String сСurrencyKod, String lineId) {
        ViewHolder viewHolder;
        viewHolder = new ViewHolder();
        viewHolder.date = (TextView) convertView.findViewById(R.id.pay_doc_data);
        viewHolder.total = (TextView) convertView.findViewById(R.id.pay_summa);
        viewHolder.currency = (TextView) convertView.findViewById(R.id.pay_currency);
        viewHolder.number = (TextView) convertView.findViewById(R.id.pay_doc_number);
        viewHolder.debet = (TextView) convertView.findViewById(R.id.pay_summa_debet);
        viewHolder.pay_number = (TextView) convertView.findViewById(R.id.pay_number);
        viewHolder.pay_summa = convertView.findViewById(R.id.pay_summa_pay);
        viewHolder.pay_summa_pay_nat = (TextView) convertView.findViewById(R.id.pay_summa_pay_nat);
        viewHolder.pay_summa_pay_usd = (TextView) convertView.findViewById(R.id.pay_summa_pay_usd);

        viewHolder.payLines = new Pays.PaysLines(pays.getId(), cDate, cNumber, totalPaysNat, totalPaysUsd, сСurrencyKod, lineId);
        addPayLines(viewHolder);
        return viewHolder;
    }

    private void addPayLines(ViewHolder viewHolder) {
        payListDocAction.add(viewHolder.payLines);
        viewHolder.payLines.setSum_nat(totalPaysNat);
        viewHolder.payLines.setSum_usd(totalPaysUsd);
    }

    private void addTotal() {
        totalPaysNat = totalPaysNat + cPay_nat;
        totalPaysUsd = totalPaysUsd + cPay_usd;
        double newSumNat = new BigDecimal(totalPaysNat).setScale(2, RoundingMode.UP).doubleValue();
        double newSumUsd = new BigDecimal(totalPaysUsd).setScale(2, RoundingMode.UP).doubleValue();
        totalSum.setText(ConstantsUtil.getFormatSum(String.valueOf(newSumNat), String.valueOf(newSumUsd)));
    }

    private void OnClickSuma(final ViewHolder viewHolder) {
        viewHolder.pay_summa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(VIEW_HOLDER, viewHolder);
                onItemClickListener.onItemClick(bundle);
            }
        });
    }

    public interface OnItemClickListener {
        void onItemClick(Bundle bundle);
    }

    public static class ViewHolder implements Parcelable {
        public static final Creator<ViewHolder> CREATOR = new Creator<ViewHolder>() {
            @Override
            public ViewHolder createFromParcel(Parcel in) {
                return new ViewHolder(in);
            }

            @Override
            public ViewHolder[] newArray(int size) {
                return new ViewHolder[size];
            }
        };
        TextView date;
        TextView total;
        TextView currency;
        TextView number;
        TextView debet;
        TextView pay_number;
        View pay_summa;
        TextView pay_summa_pay_nat;
        TextView pay_summa_pay_usd;
        Pays.PaysLines payLines;

        ViewHolder(Parcel in) {
        }

        ViewHolder() {

        }

        public TextView getDate() {
            return date;
        }

        public TextView getPay_number() {
            return pay_number;
        }

        public TextView getTotal() {
            return total;
        }

        public TextView getCurrency() {
            return currency;
        }

        public TextView getNumber() {
            return number;
        }

        public TextView getDebet() {
            return debet;
        }

        public TextView getPay_summa_pay_nat() {
            return pay_summa_pay_nat;
        }

        public void setPay_summa_pay_nat(TextView pay_summa_pay_nat) {
            this.pay_summa_pay_nat = pay_summa_pay_nat;
        }

        public TextView getPay_summa_pay_usd() {
            return pay_summa_pay_usd;
        }

        public void setPay_summa_pay_usd(TextView pay_summa_pay_usd) {
            this.pay_summa_pay_usd = pay_summa_pay_usd;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
        }
    }
}

