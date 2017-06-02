package ua.com.it_st.ordersmanagers.fragmets;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.enums.DocTypeOperation;
import ua.com.it_st.ordersmanagers.models.Orders;
import ua.com.it_st.ordersmanagers.utils.GlobalCursorLoader;
import ua.com.it_st.ordersmanagers.utils.LoaderDocFragment;
import ua.com.it_st.ordersmanagers.utils.SQLQuery;

/* Класс отображает список заказов сделанные курьером
*  В списке есть контекстное меню построенное на  Spinner */

public class OrderListDocFragment extends LoaderDocFragment {

    public static final String NUMBER_ORDER = "NUMBER_ORDER";
    public static final String DATE_ORDER = "DATE_ORDER";
    public static final String ID_ORDER = "ID_ORDER";
    public static final String DOC_TYPE_OPERATION = "DOC_TYPE_OPERATION";
    private Orders order;

    @Override
    public boolean onRecord() {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        order = ((MainActivity) getActivity()).getmCurrentOrder();

        mQuerySum = SQLQuery.queryOrdersSum("Orders.type  <> ?");
        mQueryList = SQLQuery.queryOrders("Orders._id  <> ?");

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
                bundleItem.putString(LoaderDocFragment.DOC_TYPE_OPERATION, DocTypeOperation.NEW.toString());

                final onLoaderDocListener someEventListener = (onLoaderDocListener) getActivity();
                someEventListener.onOpenFragmentClassBundle(OrderHeaderDoc.class, bundleItem);

                break;
            default:
                break;
        }
    }

    /* функция
    * отрабатывает при создании */
    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        switch (id) {
            case 0:/*получаем все заазы*/
                return new GlobalCursorLoader(getActivity(), mQueryList, getParams(), sDb);
            case 1:/*получаем сумму всех заазов*/
                return new GlobalCursorLoader(getActivity(), mQuerySum, getParams(), sDb);
            default:
                return null;
        }
    }

    /*функция отрабатывает после выполнения*/
    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {

        switch (loader.getId()) {
            case 0:
                scAdapter.swapCursor(data);
                /*следующий номер заказа*/
                //   short nextNumberDoc = (short) data.getCount();
                // order.setDocNumber(String.valueOf(++nextNumberDoc));
                break;
            case 1:
                final int cSumIndex = data.getColumnIndex("sum_orders");
                data.moveToFirst();
                final String cSum = data.getString(cSumIndex);
                updataSumOrders(cSum);
                break;
            default:
                break;
        }
    }
}
