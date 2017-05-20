package ua.com.it_st.ordersmanagers.interfaces;


import ua.com.it_st.ordersmanagers.models.Orders;

public interface DocListAction {

    /*добавить запись*/
    boolean add(Orders.OrderLines item);

    /*обновить запись*/
    boolean update(Orders.OrderLines item);

    /*удалить запись*/
    boolean delete(Orders.OrderLines item);

    /*удалить все записи*/
    boolean deleteAll();


}
