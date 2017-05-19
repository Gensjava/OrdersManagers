package ua.com.it_st.ordersmanagers.models;

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
    private ArrayList<Object> dataHeader;
    private List<Map<String, ?>> listDataHeader;

    public PayDoc() {
        dataHeader = new ArrayList<>();
//        dataHeader.add(new Companies(mFirmId, ""));
//        dataHeader.add(new Stores(mStoreId, ""));
//        dataHeader.add(new Counteragents(mClientId, "", ""));
//        dataHeader.add(new Prices(mPriceCategoryId, ""));
    }

    @Override
    public List<Map<String, ?>> fillListHeaders() {
         /* иконки к шапке заказа */
        Integer[] mPictures = new Integer[]
                {R.mipmap.ic_organization,
                        R.mipmap.ic_client,
                        R.mipmap.ic_coment
                };
        /*массив заголовков шапки заказа*/
        // String[] headerOrders = getStringArray(R.array.header_pays);
        String[] headerOrders = new String[0];
        /* список параметров шапки заказа */
        List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();
        /*заполняем шапку заказа*/
        for (byte x = 0; x < dataHeader.size(); x++) {

            Map<String, Object> map = new HashMap<>();
            map.put(String.valueOf(R.string.title), headerOrders[x]);
            map.put(String.valueOf(R.string.imageAvatar), mPictures[x]);
            items.add(map);
        }
        return items;
    }

    public void setTypeOperation(final DocTypeOperation typeOperation) {
        mTypeOperation = typeOperation;
    }
}
