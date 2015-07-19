package ua.com.it_st.ordersmanagers.models;

/**
 * Created by Gens on 19.07.2015.
 */
public class Category {
    private String mName;
    private int mId;
    private byte mLevel;

    public Category(final String name, final int id, final byte level) {
        mName = name;
        mId = id;
        mLevel = level;
    }

    public String getName() {
        return mName;
    }

    public void setName(final String name) {
        mName = name;
    }

    public int getId() {
        return mId;
    }

    public void setId(final int id) {
        mId = id;
    }

    public byte getLevel() {
        return mLevel;
    }

    public void setLevel(final byte level) {
        mLevel = level;
    }
}
