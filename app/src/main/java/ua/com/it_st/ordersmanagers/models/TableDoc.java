package ua.com.it_st.ordersmanagers.models;

/**
 * Created by Gena on 2017-05-26.
 */

public abstract class TableDoc {
    private String docId;
    private double total;

    public TableDoc() {
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
