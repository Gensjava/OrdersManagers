package ua.com.it_st.ordersmanagers.utils;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import ua.com.it_st.ordersmanagers.models.Order;

/**
 * Created by Gens on 05.08.2015.
 */
public class ConstantsUtil {
    public static Order currentOrder;
    public static LinkedHashSet<Order.OrderLines> mCart = new LinkedHashSet<Order.OrderLines>();
}
