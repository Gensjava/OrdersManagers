package ua.com.it_st.ordersmanagers.interfaces;


/**
 * Created by Gena on 2017-05-27.
 */

public interface SQLAction {

    /*добавить запись*/
    boolean add(Object object);

    /*обновить запись*/
    boolean update(Object object);

    /*удалить запись*/
    boolean delete(Object object);
}
