package ua.com.it_st.ordersmanagers.models;

/**
 * Created by Gena on 2017-05-17.
 */

public class Stores extends Catalogs {
    public Stores(String kod, String name) {
        super(kod, name);
    }

    public Stores() {
    }
    @Override
    public void setNameNativeLanguage() {
        nameNativeLanguage = "Склад";
    }

}
