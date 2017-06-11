package ua.com.it_st.ordersmanagers.fragmets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.enums.DocTypeOperation;
import ua.com.it_st.ordersmanagers.sqlTables.TablePays;
import ua.com.it_st.ordersmanagers.utils.LoaderDocFragment;
import ua.com.it_st.ordersmanagers.utils.SQLQuery;

/**
 * Created by Gena on 2017-05-04.
 */
public class PayDocListDocFragment extends LoaderDocFragment {

    @Override
    public boolean onRecord() {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setmQuerySum(SQLQuery.queryPayDocSum("Pays.type  <> ?"));
        setmQueryList(SQLQuery.queryPays("Pays._id  <> ?"));
        setParamsQuery(new String[]{"null"});
        setaClass(PayHeaderDoc.class);
        setTableName(TablePays.TABLE_NAME);

        super.onCreateView(inflater, container, savedInstanceState);
        header_journal.setText(R.string.JurnalPayDoc);

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
                someEventListener.onOpenFragmentClassBundle(PayHeaderDoc.class, bundleItem);
                break;
            default:
                break;
        }
    }
}
