package ua.com.it_st.ordersmanagers.models;

/**
 * Created by Gena on 2017-05-17.
 */

public class Companies extends Catalogs {

    public Companies(String kod, String name) {
        super(kod, name);
    }

    @Override
    public void setNameNativeLanguage() {
        nameNativeLanguage = "Организация";
    }

}
