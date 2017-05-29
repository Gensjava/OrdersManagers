package ua.com.it_st.ordersmanagers.models;

/**
 * Created by Gena on 2017-05-26.
 */

public abstract class TableLines {
    private String docId;
    private int ordinal;
    private double total;

    public TableLines() {
    }

    public TableLines(String docId) {
        this.docId = docId;
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

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }
}
