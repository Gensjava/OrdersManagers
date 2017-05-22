package ua.com.it_st.ordersmanagers.models;

import java.util.List;
import java.util.Map;


public abstract class Documents {

    private String mId;
    private String mDocDate;
    private String mDocNumber;
    private String mNote;

    private Companies company;
    private Agents agent;

    public abstract List<Map<String, ?>> fillListHeaders();

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

    public Companies getCompany() {
        return company;
    }

    public void setCompany(Companies company) {
        this.company = company;
    }

    public Agents getAgent() {
        return agent;
    }

    public void setAgent(Agents agent) {
        this.agent = agent;
    }
}
