package ua.com.it_st.ordersmanagers.fragmets;

/**
 * Created by Gena on 2017-06-03.
 */

public class Gen<T> {
    T ob; // объявление объекта типа T

    // Передать конструктору ссылку на объект типа T
    Gen(T o) {
        ob = o;
    }

    // Вернуть ob
    T getob() {
        return ob;
    }

    // Показать тип T
    void showType() {
        System.out.println("Тип T: " + ob.getClass().getName());
    }

    public <T> T getMiddle(T... a) {
        return a[a.length / 2];
    }

    public void setOb(T ob) {
        this.ob = ob;
    }
}
