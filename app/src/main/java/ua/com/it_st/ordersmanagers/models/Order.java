package ua.com.it_st.ordersmanagers.models;

import ua.com.it_st.ordersmanagers.enums.DocTypeEnum;

/**
 * Created by Gens on 31.07.2015.
 */
public class Order {
    private String mId;
    private DocTypeEnum mDocType;
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

    public Order(final String id, final DocTypeEnum docType,
                 final String docDate, final String docNumber,
                 final String completed, final String agentId,
                 final String firmId, final String storeId,
                 final String clientId, final String priceCategoryId,
                 final String total, final String note) {

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
    }

    public Order() {

    }

    public String getId() {
        return mId;
    }

    public void setId(final String id) {
        mId = id;
    }

    public DocTypeEnum getDocType() {
        return mDocType;
    }

    public void setDocType(final DocTypeEnum docType) {
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

    public String getClientId(final String item) {
        return mClientId;
    }

    public void setClientId(final String clientId) {
        mClientId = clientId;
    }

    public String getPriceCategoryId(final String item) {
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


    //табличная часть заказа
    public static class OrderLines {
        private String mDocId;
        private String mGoodsId;
        private int mRate;
        private double mAmount;
        private double mPrice;
        private double mSum;
        private String mName;

        public OrderLines(final String docId,
                          final String goodsId,
                          final int rate,
                          final double amount,
                          final double price,
                          final double sum,
                          final String name) {
            mDocId = docId;
            mGoodsId = goodsId;
            mRate = rate;
            mAmount = amount;
            mPrice = price;
            mSum = sum;
            mName = name;
        }

        public OrderLines() {

        }

        public String getName() {
            return mName;
        }

        public void setName(final String name) {
            mName = name;
        }

        public String getDocId() {
            return mDocId;
        }

        public void setDocId(final String docId) {
            mDocId = docId;
        }

        public String getGoodsId() {
            return mGoodsId;
        }

        public void setGoodsId(final String goodsId) {
            mGoodsId = goodsId;
        }

        public int getRate() {
            return mRate;
        }

        public void setRate(final int rate) {
            mRate = rate;
        }

        public double getAmount() {
            return mAmount;
        }

        public void setAmount(final double amount) {
            mAmount = amount;
        }

        public double getPrice() {
            return mPrice;
        }

        public void setPrice(final double price) {
            mPrice = price;
        }

        public double getSum() {
            return mSum;
        }

        public void setSum(final double sum) {
            mSum = sum;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final OrderLines that = (OrderLines) o;

            if (mDocId != null ? !mDocId.equals(that.mDocId) : that.mDocId != null) return false;
            return !(mGoodsId != null ? !mGoodsId.equals(that.mGoodsId) : that.mGoodsId != null);

        }

        @Override
        public int hashCode() {
            int result = mDocId != null ? mDocId.hashCode() : 0;
            result = 31 * result + (mGoodsId != null ? mGoodsId.hashCode() : 0);
            return result;
        }
    }

}
