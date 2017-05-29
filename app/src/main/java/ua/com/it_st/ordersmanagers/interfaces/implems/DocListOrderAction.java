package ua.com.it_st.ordersmanagers.interfaces.implems;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import ua.com.it_st.ordersmanagers.interfaces.OrderBasementAction;
import ua.com.it_st.ordersmanagers.models.Orders;
import ua.com.it_st.ordersmanagers.models.TableLines;

public class DocListOrderAction implements ua.com.it_st.ordersmanagers.interfaces.DocListAction, OrderBasementAction {

    /* ТЧ заказа */
    public static Set<Orders.OrdersLines> mCart = new LinkedHashSet<Orders.OrdersLines>();

    /*заполняем корзину в ArrayList*/
    public static ArrayList<Orders.OrdersLines> getItemsGoods() {
        ArrayList<Orders.OrdersLines> lCartOrders = new ArrayList<Orders.OrdersLines>();

        Orders.OrdersLines[] cartOrders = DocListOrderAction.mCart.toArray(new Orders.OrdersLines[mCart.size()]);

        Collections.addAll(lCartOrders, cartOrders);
        return lCartOrders;
    }

    /*
    записываем новый товар в ТЧ заказа
    если есть такой товар тогда делаем замену
    */
    @Override
    public boolean add(TableLines item) {
        Orders.OrdersLines itemOrder = (Orders.OrdersLines) item;
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
    public boolean update(TableLines item) {
        Orders.OrdersLines itemOrder = (Orders.OrdersLines) item;
        //если есть такой уже добавляем кол-во и делаем пересчет суммы
        for (Orders.OrdersLines iC : mCart) {
            if (iC.getProduct().getKod().equals(itemOrder.getProduct().getKod())) {

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
    public boolean delete(TableLines item) {
        Orders.OrdersLines itemOrder = (Orders.OrdersLines) item;

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

        for (final Orders.OrdersLines aMCart : mCart) {
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
