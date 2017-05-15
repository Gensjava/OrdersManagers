package ua.com.it_st.ordersmanagers.fragmets;

/*Класс предназначен для отображения и заполнения шапки документа заказа
* Все поля кроме коммнтария являются обязательными для заполнения*/

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OrderNewHeaderFragment extends NewDocHeaderFragment {

    public static final String NAME_TABLE = "NAME_TABLE";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
          /*массив принимает выбранные занчения шапки и передает их в адаптер*/
        setmItemsHeader(new String[5][3]);

        return super.onCreateView(inflater, container, savedInstanceState);

    }
}
