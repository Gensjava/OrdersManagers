package ua.com.it_st.ordersmanagers.fragmets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Gena on 2017-05-14.
 */

public class PayNewHeaderFragment extends NewDocHeaderFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

                 /*массив принимает выбранные занчения шапки и передает их в адаптер*/
        setmItemsHeader(new String[5][3]);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
