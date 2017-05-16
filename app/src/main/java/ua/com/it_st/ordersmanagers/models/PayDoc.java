package ua.com.it_st.ordersmanagers.models;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.enums.DocTypeOperation;

/**
 * Created by Gens on 17.08.2015.
 */
public class PayDoc extends Documents {

    private DocTypeOperation mTypeOperation;

    public PayDoc() {
    }

    @Override
    public List<Map<String, ?>> getListHeaders(Fragment fragment) {
         /* иконки к шапке заказа */
        Integer[] mPictures = new Integer[]
                {R.mipmap.ic_organization,
                        R.mipmap.ic_client,
                        R.mipmap.ic_coment
                };
        /*массив заголовков шапки заказа*/
        String[] headerOrders = fragment.getResources().getStringArray(R.array.header_pays);
        /* список параметров шапки заказа */
        List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();
        /*заполняем шапку заказа*/
        for (byte x = 0; x < headerOrders.length; x++) {

            Map<String, Object> map = new HashMap<>();
            map.put(fragment.getString(R.string.title), headerOrders[x]);
            map.put(fragment.getString(R.string.imageAvatar), mPictures[x]);
            items.add(map);
        }
        return items;
    }

    public void setTypeOperation(final DocTypeOperation typeOperation) {
        mTypeOperation = typeOperation;
    }
}
