package ua.com.it_st.ordersmanagers.interfaces;


public interface OrderListActionDetali {

    /*количество всех элементов записей*/
    int seze();

    /* Получаем сумму всех элементов */
    double sum();

    /*проверяем пустая корзина или нет*/
    boolean isEmpty();
}
