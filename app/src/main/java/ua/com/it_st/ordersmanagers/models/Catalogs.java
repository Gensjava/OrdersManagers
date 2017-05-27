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
        setNameNativeLanguage();
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Catalogs that = (Catalogs) o;

        if (kod != null ? !kod.equals(that.kod) : that.kod != null) return false;
        return !(name != null ? !name.equals(that.name) : that.name != null);
    }

    @Override
    public int hashCode() {
        int result = kod != null ? kod.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
