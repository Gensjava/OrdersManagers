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
    private Counteragents counteragent;
    private List<Map<String, ?>> listDataHeader;

    public Pays() {
        setCompany(new Companies());
        setCounteragent(new Counteragents());
        setListDataHeader(fillListHeaders());
    }

    @Override
    public List<Map<String, ?>> fillListHeaders() {

        dataHeader = new ArrayList<>();
        dataHeader.add(getCompany());
        dataHeader.add(getCounteragent());
        dataHeader.add(new String());

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

    public Counteragents getCounteragent() {
        return counteragent;
    }

    public void setCounteragent(Counteragents counteragent) {
        this.counteragent = counteragent;
    }

    public List<Map<String, ?>> getListDataHeader() {
        return listDataHeader;
    }

    public void setListDataHeader(List<Map<String, ?>> listDataHeader) {
        this.listDataHeader = listDataHeader;
    }

    //табличная часть
    public static class PayLines extends TableDoc {
        private String dateDoc;
        private String numberDoc;
        private double sum;

        public PayLines(String dateDoc, String numberDoc, double sum, String docId) {
            this.dateDoc = dateDoc;
            this.numberDoc = numberDoc;
            this.sum = sum;
            setDocId(docId);
        }
    }
}
