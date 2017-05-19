package ua.com.it_st.ordersmanagers.enums;

public enum DocTypeOperation {
    NEW("NEW"), EDIT("EDIT"), COPY("COPY");

    String description;

    DocTypeOperation(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
