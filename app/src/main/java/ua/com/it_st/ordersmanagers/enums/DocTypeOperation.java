package ua.com.it_st.ordersmanagers.enums;

import java.util.HashMap;
import java.util.Map;

public enum DocTypeOperation {
    NEW, EDIT, COPY;

    private static final Map<DocTypeOperation, String> map = new HashMap<>();

    static {
        map.put(NEW, "Новый");
        map.put(EDIT, "Редактирование");
        map.put(COPY, "Копировать");
    }

    public static String valueFor(DocTypeOperation name) {
        return map.get(name);
    }
}
