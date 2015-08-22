package ua.com.it_st.ordersmanagers.utils;

import android.content.Context;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.models.OrderDoc;

public class ConstantsUtil {

    /* текущий новый заказ */
    public static OrderDoc mCurrentOrder = new OrderDoc();
    /* ТЧ заказа */
    public static Set<OrderDoc.OrderLines> mCart = new LinkedHashSet<OrderDoc.OrderLines>();
    /*текущий номер заказа*/
    public static short sCurrentNumber;

    /*
    записываем новый товар в ТЧ заказа
    если есть такой товар тогда делаем замену
    */
    public static void setListOrderLines(OrderDoc.OrderLines item) {

        /* удаляем товар */
        onListOrderLinesDelete(item);

        /* добавляем только если больше чем 0 */
        if (item.getAmount() > 0) {
            mCart.add(item);
        }
    }

    /* если есть в ТЧ товар такой удаляем его */
    public static void onListOrderLinesDelete(OrderDoc.OrderLines item) {

        if (mCart.contains(item)) {
            mCart.remove(item);
        }
    }

    /* Получаем сумму всего */
    public static double getTotalOrder() {
        double iNumber = 0;

        for (final OrderDoc.OrderLines aMCart : ConstantsUtil.mCart) {
            iNumber = iNumber + aMCart.getSum();
        }
        iNumber = new BigDecimal(iNumber).setScale(2, RoundingMode.UP).doubleValue();
        return iNumber;
    }

    /* Получаем ИД Агента */
    public static String getIdAgent() {
        String iNumber = null;
        return iNumber;
    }

    /* получаем новый номер заказа */
    public static short getsCurrentNumber() {
        return sCurrentNumber;
    }

    /* записываем новый номер заказа */
    public static void setsCurrentNumber(short sCurrentNumber) {
        if (sCurrentNumber == 0) {
            ConstantsUtil.sCurrentNumber = 1;
        } else {

            ConstantsUtil.sCurrentNumber = ++sCurrentNumber;
        }
    }

    /*
      Получаем текущее дату системы
      Возвращаем строку дату "текущюю дату"
        */
    public static String getDate() {
        /* текущая дата */
        long curTime = System.currentTimeMillis();
        Date date = new Date(curTime);
        /* делаем формат даты */
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(date);
    }
    /*
    Получаем текущее дату системы
    Возвращаем строку время "текущее время"
    */
    public static String getTime() {
        /* текущая дата */
        long curTime = System.currentTimeMillis();
        Date date = new Date(curTime);
        /* делаем формат времени */
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(date);
    }

    /* проверяем на обязательные поля шапки документа*/
    public static boolean checkHeaderOrder(Context context) {

        boolean bCheck = false;

        if (mCurrentOrder.getDocNumber() == null
                || mCurrentOrder.getDocDate() == null
                || mCurrentOrder.getFirmId() == null
                        || mCurrentOrder.getPriceCategoryId() == null
                        || mCurrentOrder.getClientId() == null
                        || mCurrentOrder.getAdress() == null) {

            bCheck = true;
            ErrorInfo.Tost(context.getString(R.string.not_all_cap_mandatory_filled), context);
        }
        return bCheck;
    }

    /*чистим документ заказа*/
    public static void clearOrderHeader() {
        mCurrentOrder = new OrderDoc();
    }
}
