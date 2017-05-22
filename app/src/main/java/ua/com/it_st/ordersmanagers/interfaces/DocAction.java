package ua.com.it_st.ordersmanagers.interfaces;


import ua.com.it_st.ordersmanagers.models.Documents;

public interface DocAction {

    /*добавить запись*/
    boolean add(Documents documents);

    /*обновить запись*/
    boolean update(Documents documents);

    /*удалить запись*/
    boolean delete(Documents documents);


}
