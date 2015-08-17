package ua.com.it_st.ordersmanagers.utils;

/**
 * Created by Gens on 07.08.2015.
 */
public class SQLQuery {

    /*
    запрос товаров
    sp - параметры условий запроса
    */
    public static String queryGoods(final String sp) {

        String sq;
        sq = "Select Products.name, Products.kod, Products.id_category, Products.is_category," +
                "GoodsByStores.Amount, GoodsByStores.kod_stores, Prices.price\n" +
                "FROM Products\n" +
                "LEFT OUTER JOIN GoodsByStores ON Products.kod = GoodsByStores.kod_coods\n" +
                "LEFT OUTER JOIN Prices ON Products.kod = Prices.kod\n" +
                "WHERE " + sp + "\n" +
                "GROUP by Products.kod";
        return sq;
    }

    /*
    запрос журнал заказов
    sp параметры условий запроса
    */
    public static String queryOrders(final String sp) {

        String sq;
        sq = "Select Orders._id, Orders.type,  Orders.view_id, Orders.date, Orders.number, Orders.total, Counteragents.name, Counteragents.address\n" +
                "FROM Orders\n" +
                "LEFT OUTER JOIN Counteragents ON Orders.client_id  = Counteragents.kod\n" +
                "WHERE " + sp + "\n" +
                "ORDER BY  Orders.number";
        return sq;
    }
}
