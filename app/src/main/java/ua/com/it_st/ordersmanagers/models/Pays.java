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
public class Pays extends Documents {

    private DocTypeOperation mTypeOperation;
    private ArrayList<Object> dataHeader;
    private List<Map<String, ?>> listDataHeader;
    private String mClientId;

    public Pays() {
        dataHeader = new ArrayList<>();
        dataHeader.add(new Companies(getFirmId(), ""));
        dataHeader.add(new Counteragents(mClientId, "", ""));
        dataHeader.add(new String());
    }

    @Override
    public List<Map<String, ?>> fillListHeaders() {
         /* иконки к шапке заказа */
        Integer[] mPictures = new Integer[]
                {R.mipmap.ic_organization,
                        R.mipmap.ic_client,
                        R.mipmap.ic_coment
                };
         /* список параметров шапки заказа */
        List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();
        /*заполняем шапку заказа*/
        for (byte x = 0; x < dataHeader.size(); x++) {

            Map<String, Object> map = new HashMap<>();

            if (!dataHeader.get(x).equals("")) {
                map.put(String.valueOf(x), dataHeader.get(x));
            } else {
                map.put(String.valueOf(x), "");
            }
            map.put(String.valueOf(R.string.imageAvatar), mPictures[x]);
            items.add(map);
        }
        return items;
    }

    public void setTypeOperation(final DocTypeOperation typeOperation) {
        mTypeOperation = typeOperation;
    }
}
