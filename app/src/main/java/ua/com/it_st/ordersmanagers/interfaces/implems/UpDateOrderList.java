package ua.com.it_st.ordersmanagers.interfaces.implems;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import ua.com.it_st.ordersmanagers.interfaces.OrderListAction;
import ua.com.it_st.ordersmanagers.interfaces.OrderListActionDetali;
import ua.com.it_st.ordersmanagers.models.OrderDoc;


public class UpDateOrderList implements OrderListAction, OrderListActionDetali {

    /* ТЧ заказа */
    public static Set<OrderDoc.OrderLines> mCart = new LinkedHashSet<OrderDoc.OrderLines>();

    /*заполняем корзину в ArrayList*/
    public static ArrayList<OrderDoc.OrderLines> getItemsGoods() {
        ArrayList<OrderDoc.OrderLines> lCartOrders = new ArrayList<OrderDoc.OrderLines>();

        OrderDoc.OrderLines[] cartOrders = UpDateOrderList.mCart.toArray(new OrderDoc.OrderLines[mCart.size()]);

        Collections.addAll(lCartOrders, cartOrders);
        return lCartOrders;
    }

    /*
    записываем новый товар в ТЧ заказа
    если есть такой товар тогда делаем замену
    */
    @Override
    public boolean add(OrderDoc.OrderLines item) {

        /* удаляем товар */
        delete(item);
       /* добавляем только если больше чем 0 */
        if (item.getAmount() > 0) {
            return mCart.add(item);
        }
        return false;
    }

    /*изменяем количество товара*/
    @Override
    public boolean update(OrderDoc.OrderLines item) {

        //если есть такой уже добавляем кол-во и делаем пересчет суммы
        for (OrderDoc.OrderLines iC : mCart) {
            if (iC.getGoodsId().equals(item.getGoodsId())) {

                iC.setAmount(item.getAmount());
                double newSum = new BigDecimal(iC.getAmount() * iC.getPrice()).setScale(2, RoundingMode.UP).doubleValue();
                iC.setSum(newSum);
                return true;
            }
        }
        return false;
    }

    /*удаляем товар из корзины*/
    @Override
    public boolean delete(OrderDoc.OrderLines item) {

        if (mCart.contains(item)) {
            return mCart.remove(item);
        }
        return false;
    }

    @Override
    public boolean deleteAll() {
        return mCart.removeAll(mCart);
    }


    @Override
    public int seze() {
        return mCart.size();
    }

    @Override
    public double sum() {
        double iNumber = 0;

        for (final OrderDoc.OrderLines aMCart : mCart) {
            iNumber = iNumber + aMCart.getSum();
        }
        iNumber = new BigDecimal(iNumber).setScale(2, RoundingMode.UP).doubleValue();
        return iNumber;
    }

    @Override
    public boolean isEmpty() {
        return mCart.size() == 0;
    }
}
