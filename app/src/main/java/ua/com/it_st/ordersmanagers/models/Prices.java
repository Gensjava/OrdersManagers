package ua.com.it_st.ordersmanagers.models;

/**
 * Created by Gena on 2017-05-17.
 */

public class Prices extends Catalogs {
    public Prices(String kod, String name) {
        super(kod, name);
    }

    @Override
    public void setNameNativeLanguage() {
        nameNativeLanguage = "Тип цен";
    }
}
