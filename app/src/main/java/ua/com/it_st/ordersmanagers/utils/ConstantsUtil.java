package ua.com.it_st.ordersmanagers.utils;

import java.util.LinkedHashSet;
import java.util.Set;

import ua.com.it_st.ordersmanagers.models.Order;

public class ConstantsUtil {

    public static Order mCurrentOrder = new Order();
    public static Set<Order.OrderLines> mCart = new LinkedHashSet<Order.OrderLines>();

    public static void setListOrderLines(Order.OrderLines item) {

        if (mCart.contains(item)) {
            mCart.remove(item);
        }
        mCart.add(item);
    }
}
