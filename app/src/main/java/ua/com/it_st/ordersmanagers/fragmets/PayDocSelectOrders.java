package ua.com.it_st.ordersmanagers.fragmets;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import ua.com.it_st.ordersmanagers.Adapters.SelectPayDocOrdersAdapter;
import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.enums.DocTypeOperation;
import ua.com.it_st.ordersmanagers.interfaces.implems.PayDocBasementAction;
import ua.com.it_st.ordersmanagers.interfaces.implems.PayDocSQLAction;
import ua.com.it_st.ordersmanagers.models.Pays;
import ua.com.it_st.ordersmanagers.utils.GlobalCursorLoader;
import ua.com.it_st.ordersmanagers.utils.SQLQuery;

/**
 * Created by Gena on 2017-05-22.
 */

public class PayDocSelectOrders extends CursorLoaderFragment implements View.OnClickListener {

    private SelectPayDocOrdersAdapter scAdapter;
    private Pays pays;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /* макет фрагмента */
        View rootView = inflater.inflate(R.layout.pay_dogs_select_container, container, false);

        pays = ((MainActivity) getActivity()).getmCurrentPay();

        setCountLoad((byte) 1);
        selectQuery();

        super.onCreateView(inflater, container, savedInstanceState);

        /* создааем адаптер и настраиваем список */
        scAdapter = new SelectPayDocOrdersAdapter(getActivity(), R.layout.pay_list_item, null, new String[]{}, new int[]{}, 0, pays);
         /* сам список */
        ListView lvData = (ListView) rootView.findViewById(R.id.pay_select_dogs);
        lvData.setAdapter(scAdapter);

         /*кнопка далее к следующему этапу*/
        ImageView imViewAdd = (ImageView) rootView.findViewById(R.id.pay_dogs_container_image);
        imViewAdd.setOnClickListener(this);

        return rootView;
    }

    private void selectQuery() {

        switch (pays.getmTypeOperation()) {

            case NEW:
                setQuery(SQLQuery.queryCounteragentsDebtDocs("CounteragentsDebtDocs.ClientId = ?"));
                setParamsQuery(new String[]{pays.getCounteragent().getKod()});
                break;
            case EDIT:
            case COPY:
                setQuery(SQLQuery.queryPaysLinesEdit("CounteragentsDebtDocs.ClientId = ?", pays.getId()));
                setParamsQuery(new String[]{pays.getCounteragent().getKod()});
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new GlobalCursorLoader(getActivity(), getQuery(), getParamsQuery(), CursorLoaderFragment.sDb);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        scAdapter.swapCursor(data);
        scAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        scAdapter.swapCursor(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay_dogs_container_image:
                if (onRecord()) {
                    final PayHeaderDoc.onEventListener someEventListener = (PayHeaderDoc.onEventListener) getActivity();
                    someEventListener.onOpenFragmentClass(PayDocListDocFragment.class);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onRecord() {
        boolean failure;

        PayDocBasementAction payDocBasementAction = new PayDocBasementAction(pays);
        payDocBasementAction.sum();

        PayDocSQLAction payDocSQLAction = new PayDocSQLAction(getActivity());

        if (pays.getmTypeOperation() == DocTypeOperation.NEW || pays.getmTypeOperation() == DocTypeOperation.COPY) {
            failure = payDocSQLAction.add(pays);
        } else {
            failure = payDocSQLAction.update(pays);
        }
        return failure;
    }
}
