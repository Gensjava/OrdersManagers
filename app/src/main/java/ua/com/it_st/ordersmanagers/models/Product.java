package ua.com.it_st.ordersmanagers.models;

public class Product {

    private String mDocId;
    private String mGoodsId;
    private int mRate;
    private double mAmount;
    private double mPrice;
    private double mSum;
    private String mName;

    public Product() {

    }

    public Product(final String docId,
                   final String goodsId,
                   final int rate,
                   final double amount,
                   final double price,
                   final double sum,
                   final String name) {
        mDocId = docId;
        mGoodsId = goodsId;
        mRate = rate;
        mAmount = amount;
        mPrice = price;
        mSum = sum;
        mName = name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Product that = (Product) o;

        if (mDocId != null ? !mDocId.equals(that.mDocId) : that.mDocId != null) return false;
        return !(mGoodsId != null ? !mGoodsId.equals(that.mGoodsId) : that.mGoodsId != null);

    }

    @Override
    public int hashCode() {
        int result = mDocId != null ? mDocId.hashCode() : 0;
        result = 31 * result + (mGoodsId != null ? mGoodsId.hashCode() : 0);
        return result;
    }

    public String getDocId() {
        return mDocId;
    }

    public void setDocId(final String docId) {
        mDocId = docId;
    }

    public String getGoodsId() {
        return mGoodsId;
    }

    public void setGoodsId(final String goodsId) {
        mGoodsId = goodsId;
    }

    public int getRate() {
        return mRate;
    }

    public void setRate(final int rate) {
        mRate = rate;
    }

    public double getAmount() {
        return mAmount;
    }

    public void setAmount(final double amount) {
        mAmount = amount;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(final double price) {
        mPrice = price;
    }

    public double getSum() {
        return mSum;
    }

    public void setSum(final double sum) {
        mSum = sum;
    }

    public String getName() {
        return mName;
    }

    public void setName(final String name) {
        mName = name;
    }
}
