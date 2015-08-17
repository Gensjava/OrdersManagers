package ua.com.it_st.ordersmanagers.models;

import ua.com.it_st.ordersmanagers.enums.DocTypeEnum;

/**
 * Created by Gens on 17.08.2015.
 */
public class Documents {

    private String mId;
    private DocTypeEnum mDocType;
    private String mDocDate;
    private String mDocNumber;
    private String mCompleted;
    private String mAgentId;
    private String mFirmId;
    private String mTotal;
    private String mNote;


    public String getNote() {
        return mNote;
    }

    public void setNote(final String note) {
        mNote = note;
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

    public String getTotal() {
        return mTotal;
    }

    public void setTotal(final String total) {
        mTotal = total;
    }

    private void dd() {

    }
}
