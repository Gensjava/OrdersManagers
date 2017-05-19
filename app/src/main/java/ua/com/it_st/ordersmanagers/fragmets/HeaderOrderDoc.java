package ua.com.it_st.ordersmanagers.fragmets;

/*Класс предназначен для отображения и заполнения шапки документа заказа
* Все поля кроме коммнтария являются обязательными для заполнения*/

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.com.it_st.ordersmanagers.Adapters.NewDocHeaderAdapter;
import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.interfaces.implems.UpdateDocDB;

public class HeaderOrderDoc extends HeaderDoc {

    public static final String NAME_TABLE = "NAME_TABLE";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
           /*массив принимает выбранные занчения шапки и передает их в адаптер*/
            setmItemsHeader(new String[5][3]);
            setDataHelder(UpdateDocDB.mCurrentOrder.getDataHeader());

        /* создаем адаптер */
            setmAdapter(new NewDocHeaderAdapter(getActivity(), UpdateDocDB.mCurrentOrder.getListDataHeader(),
                    R.layout.order_new_header_list_item,
                    new String[]{"title", "imageAvatar"},
                    new int[]{R.id.order_header_list_item_text, R.id.order_header_list_item_image_avatar}, this));

            super.onCreateView(inflater, container, savedInstanceState);
        }

        return rootView;

    }


}
