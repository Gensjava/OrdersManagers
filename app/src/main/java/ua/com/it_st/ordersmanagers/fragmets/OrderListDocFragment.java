package ua.com.it_st.ordersmanagers.fragmets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.enums.DocTypeOperation;
import ua.com.it_st.ordersmanagers.utils.LoaderDocFragment;
import ua.com.it_st.ordersmanagers.utils.SQLQuery;

/* Класс отображает список заказов сделанные курьером
*  В списке есть контекстное меню построенное на  Spinner */

public class OrderListDocFragment extends LoaderDocFragment {

    public static final String NUMBER_ORDER = "NUMBER_DOC";
    public static final String DATE_ORDER = "DATE_DOC";
    public static final String ID_ORDER = "ID_DOC";
    public static final String DOC_TYPE_OPERATION = "TYPE_OPERATION_DOC";

    @Override
    public boolean onRecord() {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setmQuerySum(SQLQuery.queryOrdersSum("Orders.type  <> ?"));
        setmQueryList(SQLQuery.queryOrders("Orders._id  <> ?"));
        setParams(new String[]{"null"});

        super.onCreateView(inflater, container, savedInstanceState);
        header_journal.setText(R.string.JurnalOrders);

        return rootView;
    }

    /* обработка кликов на кнопки */
    @Override
    public void onClick(final View view) {

        switch (view.getId()) {
            case R.id.main_heander_image_plus:
                Bundle bundleItem = new Bundle();
                bundleItem.putString(LoaderDocFragment.TYPE_OPERATION_DOC, DocTypeOperation.NEW.toString());
                bundleItem.putString(LoaderDocFragment.NUMBER_DOC, String.valueOf(getNextNumberDoc()));

                final onLoaderDocListener someEventListener = (onLoaderDocListener) getActivity();
                someEventListener.onOpenFragmentClassBundle(OrderHeaderDoc.class, bundleItem);

                break;
            default:
                break;
        }
    }
}
