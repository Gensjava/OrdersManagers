package ua.com.it_st.ordersmanagers.models;

/**
 * Created by Gena on 2017-05-17.
 */

public abstract class Catalogs {
    public String nameNativeLanguage;
    private String kod, name;

    public Catalogs(String kod, String name) {
        this.kod = kod;
        this.name = name;
        setNameNativeLanguage();
    }

    public Catalogs() {
    }

    public String getKod() {
        return kod;
    }

    public void setKod(String kod) {
        this.kod = kod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract void setNameNativeLanguage();
}
