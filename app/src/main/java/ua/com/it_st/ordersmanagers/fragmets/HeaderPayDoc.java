package ua.com.it_st.ordersmanagers.fragmets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.com.it_st.ordersmanagers.Adapters.NewDocHeaderAdapter;
import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.interfaces.implems.UpdateDocDB;

/**
 * Created by Gena on 2017-05-14.
 */

public class HeaderPayDoc extends HeaderDoc {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /*массив принимает выбранные занчения шапки и передает их в адаптер*/
        setmItemsHeader(new String[3][3]);
        /* создаем адаптер */
        setmAdapter(new NewDocHeaderAdapter(getActivity(), UpdateDocDB.mCurrentPayDoc.getListHeaders(this),
                R.layout.order_new_header_list_item,
                new String[]{"title", "imageAvatar"},
                new int[]{R.id.order_header_list_item_text, R.id.order_header_list_item_image_avatar}, this));


        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
