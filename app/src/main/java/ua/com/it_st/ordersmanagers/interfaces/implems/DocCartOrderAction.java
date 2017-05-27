package ua.com.it_st.ordersmanagers.interfaces.implems;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import ua.com.it_st.ordersmanagers.interfaces.OrderListActionDetali;
import ua.com.it_st.ordersmanagers.models.Orders;

public class DocCartOrderAction implements ua.com.it_st.ordersmanagers.interfaces.DocListAction, OrderListActionDetali {

    /* ТЧ заказа */
    public static Set<Orders.OrderLines> mCart = new LinkedHashSet<Orders.OrderLines>();

    /*заполняем корзину в ArrayList*/
    public static ArrayList<Orders.OrderLines> getItemsGoods() {
        ArrayList<Orders.OrderLines> lCartOrders = new ArrayList<Orders.OrderLines>();

        Orders.OrderLines[] cartOrders = DocCartOrderAction.mCart.toArray(new Orders.OrderLines[mCart.size()]);

        Collections.addAll(lCartOrders, cartOrders);
        return lCartOrders;
    }

    /*
    записываем новый товар в ТЧ заказа
    если есть такой товар тогда делаем замену
    */
    @Override
    public boolean add(Orders.OrderLines item) {
        Orders.OrderLines itemOrder = (Orders.OrderLines) item;
        /* удаляем товар */
        delete(item);
       /* добавляем только если больше чем 0 */
        if (itemOrder.getAmount() > 0) {
            return mCart.add(itemOrder);
        }
        return false;
    }

    /*изменяем количество товара*/
    @Override
    public boolean update(Orders.OrderLines item) {
        Orders.OrderLines itemOrder = (Orders.OrderLines) item;
        //если есть такой уже добавляем кол-во и делаем пересчет суммы
        for (Orders.OrderLines iC : mCart) {
            if (iC.getGoodsId().equals(itemOrder.getGoodsId())) {

                iC.setAmount(itemOrder.getAmount());
                double newSum = new BigDecimal(iC.getAmount() * iC.getPrice()).setScale(2, RoundingMode.UP).doubleValue();
                iC.setSum(newSum);
                return true;
            }
        }
        return false;
    }

    /*удаляем товар из корзины*/
    @Override
    public boolean delete(Orders.OrderLines item) {
        Orders.OrderLines itemOrder = (Orders.OrderLines) item;

        if (mCart.contains(itemOrder)) {
            return mCart.remove(itemOrder);
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

        for (final Orders.OrderLines aMCart : mCart) {
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
