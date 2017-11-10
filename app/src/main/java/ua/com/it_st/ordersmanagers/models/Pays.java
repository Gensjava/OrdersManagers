package ua.com.it_st.ordersmanagers.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.enums.DocTypeOperation;

public class Pays extends Documents {
    private DocTypeOperation mTypeOperation;
    private ArrayList<Object> dataHeader;
    private Counteragents counteragent;
    private List<Map<String, ?>> listDataHeader;
    private List<PaysLines> paysLines;
    private double total_nut;
    private double total_usd;

    public Pays() {
        setCompany(new Companies());
        setCounteragent(new Counteragents());
        setListDataHeader(fillListHeaders());
        this.paysLines = new ArrayList<>();
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

    public double getTotal_usd() {
        return total_usd;
    }

    public void setTotal_usd(double total_usd) {
        this.total_usd = total_usd;
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

    public List<PaysLines> getPaysLines() {
        return paysLines;
    }

    public void setPaysLines(List<PaysLines> paysLines) {
        this.paysLines = paysLines;
    }

    public double getTotal_nut() {
        return total_nut;
    }

    public void setTotal_nut(double total_nut) {
        this.total_nut = total_nut;
    }

    public DocTypeOperation getmTypeOperation() {
        return mTypeOperation;
    }

    //табличная часть
    public static class PaysLines extends TableLines {
        private String dateDoc;
        private String numberDoc;
        private double sum_nat;
        private double sum_usd;
        private String currency;
        private String lineId;

        public PaysLines(String docId, String dateDoc, String numberDoc, double sum_nat, double sum_usd, String currency, String lineId) {
            this.dateDoc = dateDoc;
            this.numberDoc = numberDoc;
            this.sum_nat = sum_nat;
            this.sum_usd = sum_usd;
            this.currency = currency;
            this.lineId = lineId;
            setDocId(docId);
        }

        public String getDateDoc() {
            return dateDoc;
        }

        public String getNumberDoc() {
            return numberDoc;
        }

        public double getSum_nat() {
            return sum_nat;
        }

        public void setSum_nat(double sum_nat) {
            this.sum_nat = sum_nat;
        }

        public double getSum_usd() {
            return sum_usd;
        }

        public void setSum_usd(double sum_usd) {
            this.sum_usd = sum_usd;
        }

        public String getCurrency() {
            return currency;
        }

        public String getLineId() {
            return lineId;
        }
    }
}
