package ua.com.it_st.ordersmanagers.models;

/**
 * Created by Gens on 19.07.2015.
 */
public class Product {

    String mName;
    double mPrice;
    int mId;

    public Product(final String name, final double price, final int id) {
        mName = name;
        mPrice = price;
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(final String name) {
        mName = name;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(final double price) {
        mPrice = price;
    }

    public int getId() {
        return mId;
    }

    public void setId(final int id) {
        mId = id;
    }
}
