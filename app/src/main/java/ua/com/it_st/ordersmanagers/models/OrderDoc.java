package ua.com.it_st.ordersmanagers.models;

import ua.com.it_st.ordersmanagers.enums.DocType;
import ua.com.it_st.ordersmanagers.enums.DocTypeOperation;

/**
 * Created by Gens on 31.07.2015.
 */
public class OrderDoc {
    private String mId;
    private DocType mDocType;
    private String mDocDate;
    private String mDocNumber;
    private String mCompleted;
    private String mAgentId;
    private String mFirmId;
    private String mStoreId;
    private String mClientId;
    private String mPriceCategoryId;
    private String mTotal;
    private String mNote;
    private String mAdress;
    private DocTypeOperation mTypeOperation;

    /*этот флаг предназначен для режима редактирования заказа его таб.части
    * true - осзначает что в корзине что-то изменили
    * false - пока ни кто ничего не менял*/
    private boolean mClickModifitsirovannoiCart;

    public OrderDoc(final String id, final DocType docType,
                    final String docDate, final String docNumber,
                    final String completed, final String agentId,
                    final String firmId, final String storeId,
                    final String clientId, final String priceCategoryId,
                    final String total, final String note, final String adress,
                    final DocTypeOperation typeOperation, final boolean clickModifitsirovannoiCart) {

        mId = id;
        mDocType = docType;
        mDocDate = docDate;
        mDocNumber = docNumber;
        mCompleted = completed;
        mAgentId = agentId;
        mFirmId = firmId;
        mStoreId = storeId;
        mClientId = clientId;
        mPriceCategoryId = priceCategoryId;
        mTotal = total;
        mNote = note;
        mAdress = adress;
        mTypeOperation = typeOperation;
        mClickModifitsirovannoiCart = clickModifitsirovannoiCart;
    }

    public OrderDoc() {

    }

    public String getId() {
        return mId;
    }

    public void setId(final String id) {
        mId = id;
    }

    public DocType getDocType() {
        return mDocType;
    }

    public void setDocType(final DocType docType) {
        mDocType = docType;
    }

    public String getDocDate() {
        return mDocDate;
    }

    public void setDocDate(final String docDate) {
        mDocDate = docDate;
    }

    public String getDocNumber() {
        return mDocNumber;
    }

    public void setDocNumber(final String docNumber) {
        mDocNumber = docNumber;
    }

    public String getCompleted() {
        return mCompleted;
    }

    public void setCompleted(final String completed) {
        mCompleted = completed;
    }

    public String getAgentId() {
        return mAgentId;
    }

    public void setAgentId(final String agentId) {
        mAgentId = agentId;
    }

    public String getFirmId() {
        return mFirmId;
    }

    public void setFirmId(final String firmId) {
        mFirmId = firmId;
    }

    public String getStoreId() {
        return mStoreId;
    }

    public void setStoreId(final String storeId) {
        mStoreId = storeId;
    }

    public String getClientId() {
        return mClientId;
    }

    public void setClientId(final String clientId) {
        mClientId = clientId;
    }

    public String getPriceCategoryId() {
        return mPriceCategoryId;
    }

    public void setPriceCategoryId(final String priceCategoryId) {
        mPriceCategoryId = priceCategoryId;
    }

    public String getTotal() {
        return mTotal;
    }

    public void setTotal(final String total) {
        mTotal = total;
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(final String note) {
        mNote = note;
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

    //табличная часть заказа
    public static class OrderLines extends Product {

        private double mBalance;

        public OrderLines(final String docId,
                          final String goodsId,
                          final int rate,
                          final double amount,
                          final double price,
                          final double sum,
                          final String name,
                          final double balance) {

            super(docId, goodsId, rate, amount, price, sum, name);
            mBalance = balance;
        }

        public OrderLines() {

        }

        public double getBalance() {
            return mBalance;
        }

        public void setBalance(final double balance) {
            mBalance = balance;
        }
    }
}
