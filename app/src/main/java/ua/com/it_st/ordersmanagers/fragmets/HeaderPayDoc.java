package ua.com.it_st.ordersmanagers.fragmets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.com.it_st.ordersmanagers.Adapters.HeaderDocAdapter;
import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.interfaces.implems.UpdateDocDB;
import ua.com.it_st.ordersmanagers.models.Pays;

/**
 * Created by Gena on 2017-05-14.
 */

public class HeaderPayDoc extends HeaderDoc {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            CurrentNewDog = new Pays();
        /* создаем адаптер */
            setmAdapter(new HeaderDocAdapter(getActivity(), UpdateDocDB.mCurrentPays.fillListHeaders(),
                    R.layout.order_new_header_list_item,
                    new String[]{"title", "imageAvatar"},
                    new int[]{R.id.order_header_list_item_text, R.id.order_header_list_item_image_avatar}, this));

            super.onCreateView(inflater, container, savedInstanceState);
        }
        return rootView;
    }
}
