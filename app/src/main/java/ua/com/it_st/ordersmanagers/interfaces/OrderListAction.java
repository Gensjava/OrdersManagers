package ua.com.it_st.ordersmanagers.interfaces;


import ua.com.it_st.ordersmanagers.models.OrderDoc;

public interface OrderListAction {

    /*добавить запись*/
    boolean add(OrderDoc.OrderLines item);

    /*обновить запись*/
    boolean update(OrderDoc.OrderLines item);

    /*удалить запись*/
    boolean delete(OrderDoc.OrderLines item);

    /*удалить все записи*/
    boolean deleteAll();


}
