package ua.com.it_st.ordersmanagers.fragmets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.enums.DocTypeOperation;
import ua.com.it_st.ordersmanagers.interfaces.implems.UpdateDocDB;
import ua.com.it_st.ordersmanagers.utils.LoaderDocFragment;
import ua.com.it_st.ordersmanagers.utils.SQLQuery;

/* Класс отображает список заказов сделанные курьером
*  В списке есть контекстное меню построенное на  Spinner */

public class OrderListDocFragment extends LoaderDocFragment {

    public static final String NUMBER_ORDER = "NUMBER_ORDER";
    public static final String DATE_ORDER = "DATE_ORDER";
    public static final String ID_ORDER = "ID_ORDER";
    public static final String DOC_TYPE_OPERATION = "DOC_TYPE_OPERATION";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mQuerySum = SQLQuery.queryOrdersSum("Orders.type  <> ?");
        mQueryList = SQLQuery.queryOrders("Orders._id  <> ?");

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

                bundleItem.putString(LoaderDocFragment.DOC_TYPE_OPERATION, DocTypeOperation.NEW.getDescription());

                final OrderListDocFragment.onEventListener someEventListener = (OrderListDocFragment.onEventListener) getActivity();
                someEventListener.onOpenFragmentClassBundle(HeaderOrderDoc.class, bundleItem);

                /*чистим док заказ и содаем новый заказ*/
                UpdateDocDB.clearOrderHeader(DocTypeOperation.NEW);

                break;
            default:
                break;
        }
    }
}
