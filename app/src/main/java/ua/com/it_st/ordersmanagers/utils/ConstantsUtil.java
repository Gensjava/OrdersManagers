package ua.com.it_st.ordersmanagers.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import ua.com.it_st.ordersmanagers.models.Order;

public class ConstantsUtil {

    /* текущий новый заказ */
    public static Order mCurrentOrder = new Order();
    /* ТЧ заказа */
    public static Set<Order.OrderLines> mCart = new LinkedHashSet<Order.OrderLines>();

    /*
    записываем новый товар в ТЧ заказа
    если есть такой товар тогда делаем замену
    */
    public static void setListOrderLines(Order.OrderLines item) {

        /* удаляем товар */
        onListOrderLinesDelete(item);

        /* добавляем только если больше чем 0 */
        if (item.getAmount() > 0) {
            mCart.add(item);
        }
    }

    /* если есть в ТЧ товар такой удаляем его */
    public static void onListOrderLinesDelete(Order.OrderLines item) {

        if (mCart.contains(item)) {
            mCart.remove(item);
        }
    }

    /* Получем последний номер документа */
    public static int getNumberOrder() {
        short iNumber = 0;
        return iNumber;
    }

    /* Получаем сумму всего */
    public static String getTotalOrder() {
        String iNumber = null;
        return iNumber;
    }

    /* Получаем ИД Агента */
    public static String getIdAgent() {
        String iNumber = null;
        return iNumber;
    }

    /*
    Получаем текущее дату системы
    Возвращаем дату "текущюю дату"
    */
    public static String getDate() {
        /* текущая дата */
        long curTime = System.currentTimeMillis();
        Date date = new Date(curTime);
        /* делаем формат даты */
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyy HH:mm:ss");
        return dateFormat.format(date);
    }
}
