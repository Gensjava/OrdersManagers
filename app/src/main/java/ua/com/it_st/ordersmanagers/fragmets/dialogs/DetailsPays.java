package ua.com.it_st.ordersmanagers.fragmets.dialogs;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ua.com.it_st.ordersmanagers.Adapters.SelectPayDocOrdersAdapter;
import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.models.Pays;

public class DetailsPays extends BaseDialogFragment {
    OnDetailsPaysFragmentListener onDetailsPaysFragmentListener;
    private View rootView;
    private EditText paySummaNat;
    private EditText paySummaUsd;
    private MainActivity mainActivity;
    private int position;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());
        rootView = mLayoutInflater.inflate(R.layout.pay_list_item_detali, null);

        if (getArguments() != null) {
            SelectPayDocOrdersAdapter.ViewHolder viewHolder = getArguments().getParcelable(SelectPayDocOrdersAdapter.VIEW_HOLDER);

            TextView payDocData = (TextView) rootView.findViewById(R.id.pay_doc_data_value);
            payDocData.setText(viewHolder.getDate().getText().toString());
            TextView payDocNumber = (TextView) rootView.findViewById(R.id.pay_doc_number_value);
            payDocNumber.setText(viewHolder.getNumber().getText().toString());
            TextView payCurrency = (TextView) rootView.findViewById(R.id.pay_currency_value);
            payCurrency.setText(viewHolder.getCurrency().getText().toString());
            TextView paySumma = (TextView) rootView.findViewById(R.id.pay_summa_value);
            paySumma.setText(viewHolder.getTotal().getText().toString());
            TextView paySummaDebet = (TextView) rootView.findViewById(R.id.pay_summa_debet_value);
            paySummaDebet.setText(viewHolder.getDebet().getText().toString());

            paySummaNat = (EditText) rootView.findViewById(R.id.pay_summa_pay_nat);
            paySummaNat.setText(viewHolder.getPay_summa_pay_nat().getText().toString());
            paySummaUsd = (EditText) rootView.findViewById(R.id.pay_summa_pay_usd);
            paySummaUsd.setText(viewHolder.getPay_summa_pay_usd().getText().toString());

            position = Integer.valueOf(viewHolder.getPay_number().getText().toString());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        super.getBuilder().setTitle("Детали платежа:");
        super.getBuilder().setPositiveButton("Ок", this);
        super.getBuilder().setNegativeButton("Отмена", this);
        super.getBuilder().setView(rootView);
        return super.getBuilder().create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        super.onClick(dialogInterface, i);
        switch (i) {
            case Dialog.BUTTON_POSITIVE:
                Pays.PaysLines paysLines = mainActivity.getmCurrentPay().getPaysLines().get(position - 1);
                if (!TextUtils.isEmpty(paySummaNat.getText().toString())) {
                    paysLines.setSum_nat(Double.parseDouble(paySummaNat.getText().toString()));
                } else {
                    paysLines.setSum_nat(0);
                }
                if (!TextUtils.isEmpty(paySummaUsd.getText().toString())) {
                    paysLines.setSum_usd(Double.parseDouble(paySummaUsd.getText().toString()));
                } else {
                    paysLines.setSum_usd(0);
                }
                onDetailsPaysFragmentListener.onDetailsPaysOKListener();
            case Dialog.BUTTON_NEGATIVE:
                dismiss();
            default:
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onDetailsPaysFragmentListener = null;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        try {
            onDetailsPaysFragmentListener = (OnDetailsPaysFragmentListener) context;
            mainActivity = (MainActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    public interface OnDetailsPaysFragmentListener {
        void onDetailsPaysOKListener();
    }
}
