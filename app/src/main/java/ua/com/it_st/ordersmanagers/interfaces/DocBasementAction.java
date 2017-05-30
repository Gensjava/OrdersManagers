package ua.com.it_st.ordersmanagers.interfaces;


public interface DocBasementAction {

    /*количество всех элементов записей*/
    int size();

    /* Получаем сумму всех элементов */
    double sum();

    /*проверяем пустая корзина или нет*/
    boolean isEmpty();
}
