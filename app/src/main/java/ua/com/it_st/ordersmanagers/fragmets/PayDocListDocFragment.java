package ua.com.it_st.ordersmanagers.fragmets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.enums.DocTypeOperation;
import ua.com.it_st.ordersmanagers.utils.LoaderDocFragment;
import ua.com.it_st.ordersmanagers.utils.SQLQuery;

/**
 * Created by Gena on 2017-05-04.
 */
public class PayDocListDocFragment extends LoaderDocFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mQuerySum = SQLQuery.queryPayDocSum("Pays.type  <> ?");
        mQueryList = SQLQuery.queryPays("Pays._id  <> ?");

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
                bundleItem.putString(LoaderDocFragment.DOC_TYPE_OPERATION, DocTypeOperation.NEW.toString());

                final onLoaderDocListener someEventListener = (onLoaderDocListener) getActivity();
                someEventListener.onOpenFragmentClassBundle(PayHeaderDoc.class, bundleItem);
                break;
            default:
                break;
        }
    }
}
