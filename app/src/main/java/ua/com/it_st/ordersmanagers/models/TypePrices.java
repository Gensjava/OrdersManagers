package ua.com.it_st.ordersmanagers.models;

/**
 * Created by Gena on 2017-05-17.
 */

public class TypePrices extends Catalogs {

    public TypePrices(String kod, String name) {
        super(kod, name);
    }

    public TypePrices() {
    }

    @Override
    public void setNameNativeLanguage() {
        nameNativeLanguage = "Тип цен";
    }
}
