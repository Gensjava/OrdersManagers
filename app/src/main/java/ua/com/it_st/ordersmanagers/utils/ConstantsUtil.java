package ua.com.it_st.ordersmanagers.utils;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import ua.com.it_st.ordersmanagers.models.Order;

public class ConstantsUtil {

    //текущий новый заказ
    public static Order mCurrentOrder = new Order();
    //ТЧ заказа
    public static Set<Order.OrderLines> mCart = new LinkedHashSet<Order.OrderLines>();

    //записываем новый товар в ТЧ заказа
    //если есть такой товар тогда делаем замену
    public static void setListOrderLines(Order.OrderLines item) {

        //удаляем товар
        onListOrderLinesDelete(item);

        //добавляем только если больше чем 0
        if (item.getAmount() > 0) {
            mCart.add(item);
        }
    }

    //если есть в ТЧ товар такой удаляем его
    public static void onListOrderLinesDelete(Order.OrderLines item) {

        if (mCart.contains(item)) {
            mCart.remove(item);
        }
    }

    // Получаем текущее дату системы
    // Возвращаем дату "текущюю дату"
    private static Date getDate() {
        //текущая дата
        long curTime = System.currentTimeMillis();
        Date date = new Date(curTime);
        return date;
    }
}
