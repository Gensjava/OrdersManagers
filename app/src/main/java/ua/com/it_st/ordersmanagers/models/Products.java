package ua.com.it_st.ordersmanagers.models;

public class Products extends Catalogs {
    private boolean isCategory;

    public Products(String kod, String name) {
        super(kod, name);
    }

    public Products(String kod, String name, boolean isCategory) {
        super(kod, name);
        this.isCategory = isCategory;
    }

    @Override
    public void setNameNativeLanguage() {
        nameNativeLanguage = "Товары";
    }

    public boolean isCategory() {
        return isCategory;
    }

    public void setCategory(boolean category) {
        isCategory = category;
    }


}
