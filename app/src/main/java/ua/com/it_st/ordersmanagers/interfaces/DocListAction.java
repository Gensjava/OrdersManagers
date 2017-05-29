package ua.com.it_st.ordersmanagers.interfaces;


import ua.com.it_st.ordersmanagers.models.TableLines;

public interface DocListAction {

    /*добавить запись*/
    boolean add(TableLines item);

    /*обновить запись*/
    boolean update(TableLines item);

    /*удалить запись*/
    boolean delete(TableLines item);

    /*удалить все записи*/
    boolean deleteAll();


}
