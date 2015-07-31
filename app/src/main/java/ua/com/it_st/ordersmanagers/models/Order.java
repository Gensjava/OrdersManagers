package ua.com.it_st.ordersmanagers.models;

/**
 * Created by Gens on 31.07.2015.
 */
public class Order {
    private String mId;
    private String mDocType;
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

    public Order(final String id, final String docType,
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

    public String getId() {
        return mId;
    }

    public void setId(final String id) {
        mId = id;
    }

    public String getDocType() {
        return mDocType;
    }

    public void setDocType(final String docType) {
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
}
