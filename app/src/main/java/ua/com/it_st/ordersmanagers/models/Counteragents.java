package ua.com.it_st.ordersmanagers.models;

/**
 * Created by Gena on 2017-05-17.
 */

public class Counteragents extends Catalogs {
    private String address;

    public Counteragents(String kod, String name) {
        super(kod, name);
    }

    public Counteragents(String kod, String name, String address) {
        super(kod, name);
        this.address = address;
    }

    @Override
    public void setNameNativeLanguage() {
        nameNativeLanguage = "Контрагент";
    }
}
