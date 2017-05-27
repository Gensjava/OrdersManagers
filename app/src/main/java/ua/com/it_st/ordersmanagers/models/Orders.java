package ua.com.it_st.ordersmanagers.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.enums.DocTypeOperation;

/**
 * Created by Gens on 31.07.2015.
 */
public class Orders extends Documents {

    private String mAdress;

    private Stores store;
    private TypePrices typePrices;
    private DocTypeOperation mTypeOperation;
    private ArrayList<Object> dataHeader;
    private List<Map<String, ?>> listDataHeader;
    private Counteragents counteragent;

    /*этот флаг предназначен для режима редактирования заказа его таб.части
    * true - осзначает что в корзине что-то изменили
    * false - пока ни кто ничего не менял*/
    private boolean mClickModifitsirovannoiCart;

    public Orders() {

        setCompany(new Companies());
        setStore(new Stores());
        setCounteragent(new Counteragents());
        setTypePrices(new TypePrices());
        setListDataHeader(fillListHeaders());
    }

    public String getAdress() {
        return mAdress;
    }

    public void setAdress(final String adress) {
        mAdress = adress;
    }

    public DocTypeOperation getTypeOperation() {
        return mTypeOperation;
    }

    public void setTypeOperation(final DocTypeOperation typeOperation) {
        mTypeOperation = typeOperation;
    }

    public boolean isClickModifitsirovannoiCart() {
        return mClickModifitsirovannoiCart;
    }

    public void setClickModifitsirovannoiCart(final boolean clickModifitsirovannoiCart) {
        mClickModifitsirovannoiCart = clickModifitsirovannoiCart;
    }

    public List<Map<String, ?>> getListDataHeader() {
        return listDataHeader;
    }

    public void setListDataHeader(List<Map<String, ?>> listDataHeader) {
        this.listDataHeader = listDataHeader;
    }

    public Stores getStore() {
        return store;
    }

    public void setStore(Stores store) {
        this.store = store;
    }

    public Counteragents getCounteragent() {
        return counteragent;
    }

    public void setCounteragent(Counteragents counteragent) {
        this.counteragent = counteragent;
    }

    public TypePrices getTypePrices() {
        return typePrices;
    }

    public void setTypePrices(TypePrices typePrices) {
        this.typePrices = typePrices;
    }

    /* создаем список - шапку  для адаптера
  * Иконки и заголовки*/
    public List<Map<String, ?>> fillListHeaders() {

        dataHeader = new ArrayList<>();
        dataHeader.add(getCompany());
        dataHeader.add(getStore());
        dataHeader.add(getCounteragent());
        dataHeader.add(getTypePrices());
        dataHeader.add(new String());

        /* иконки к шапке заказа */
        Integer[] mPictures = new Integer[]
                {R.mipmap.ic_organization,
                        R.mipmap.ic_stores,
                        R.mipmap.ic_client,
                        R.mipmap.ic_tipe_price,
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

    //табличная часть заказа
    public static class OrderLines extends TableDoc {

        private Products product;
        private double amount;
        private double price;
        private double sum;
        private int rate;

        public OrderLines() {
        }

        public OrderLines(Products product) {
            this.product = product;
        }

        public OrderLines(String docId, Products product, double amount, double price, double sum) {
            super(docId);
            this.product = product;
            this.amount = amount;
            this.price = price;
            this.sum = sum;
        }

        public int getRate() {
            return rate;
        }

        public void setRate(int rate) {
            this.rate = rate;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getSum() {
            return sum;
        }

        public void setSum(double sum) {
            this.sum = sum;
        }

        public Products getProduct() {
            return product;
        }

        public void setProduct(Products product) {
            this.product = product;
        }
    }


}
