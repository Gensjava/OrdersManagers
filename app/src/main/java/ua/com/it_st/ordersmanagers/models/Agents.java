package ua.com.it_st.ordersmanagers.models;

/**
 * Created by Gena on 2017-05-22.
 */

public class Agents extends Catalogs {
    public Agents(String kod, String name) {
        super(kod, name);
    }

    public Agents() {
    }

    @Override
    public void setNameNativeLanguage() {
        nameNativeLanguage = "Агенты";
    }
}
