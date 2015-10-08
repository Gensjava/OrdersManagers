package ua.com.it_st.ordersmanagers.utils;

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
                "GROUP by Products.name";
        return sq;
    }

    /*
    запрос журнал заказов
    sp параметры условий запроса
    */
    public static String queryOrders(final String sp) {

        String sq;
        sq = "Select   Orders._id, Orders.type,  Orders.view_id, Orders.date, Orders.number, Orders.total," +
                "Counteragents.name, Counteragents.address\n" +
                "FROM Orders\n" +
                "LEFT OUTER JOIN Counteragents ON Orders.client_id  = Counteragents.kod " +
                "and Orders.client_adress  = Counteragents.address\n" +
                "WHERE " + sp + "\n";

        return sq;
    }

    /*
    запрос журнал заказов сумма всех заказов
    sp параметры условий запроса
    */
    public static String queryOrdersSum(final String sp) {

        String sq;
        sq = "Select Sum(Orders.total) as sum_orders " +
                "FROM Orders\n" +
                "WHERE " + sp + "\n";

        return sq;
    }

    /*
    запрос журнал заказов для выгрузки в файл csv
    sp параметры условий запроса
    */
    public static String queryOrdersHeaderFilesCsv(final String sp) {

        String sq;
        sq = "Select Orders.view_id, Orders.type,  Orders.date, Orders.number, Orders.completed, Orders.agent_id," +
                "Orders.company_id, Orders.store_id,  Orders.client_id, Orders.price_id, Orders.total, Orders.note\n" +
                "FROM Orders\n" +
                "WHERE " + sp + "\n";

        return sq;
    }

    /*
    запрос шапки документа
    sp параметры условий запроса
    */
    public static String queryOrdersHeader(final String sp) {

        String sq;
        sq = "Select Orders._id, Orders.note, Orders.agent_id," +
                "Сompanys.name as name_comp, Сompanys.kod as kod_comp,\n" +
                "TypeStores.name as name_type_stores, TypeStores.kod as kod_type_stores,\n" +
                "Counteragents.name as name_contr, Counteragents.address as address_contr, Counteragents.kod as kod_contr,\n" +
                "TypePrices.name as name_type_price, TypePrices.kod as kod_type_price\n" +
                "FROM Orders\n" +
                "LEFT OUTER JOIN Сompanys ON Orders.company_id  = Сompanys.kod " +
                "LEFT OUTER JOIN TypeStores ON Orders.store_id  = TypeStores.kod \n" +
                "LEFT OUTER JOIN Counteragents ON Orders.client_id  = Counteragents.kod \n" +
                "LEFT OUTER JOIN TypePrices ON Orders.price_id  = TypePrices.kod \n" +
                "WHERE " + sp + "\n";

        return sq;
    }

    /* запрос табличной части документа
     sp параметры условий запроса
     */
    public static String queryOrdersLinesEdit(final String sp) {

        String sq;
        sq = "Select OrdersLines.goods_id, OrdersLines.rate, OrdersLines.price, OrdersLines.amount ,\n" +
                "GoodsByStores.amount as amount_stores,\n" +
                "Products.name\n" +
                "FROM OrdersLines\n" +
                "LEFT OUTER JOIN GoodsByStores ON OrdersLines.goods_id  = GoodsByStores.kod_coods\n" +
                "LEFT OUTER JOIN Products ON OrdersLines.goods_id  = Products.kod\n" +
                "WHERE " + sp + "\n";

        return sq;
    }

    /* запрос табличной части документа  для выгрузки в файл csv
     sp параметры условий запроса
     */
    public static String queryOrdersLinesFilesCsv(final String sp) {

        String sq;
        sq = "Select OrdersLines.doc_id, OrdersLines.goods_id, OrdersLines.rate,  OrdersLines.amount ,OrdersLines.price\n" +
                "FROM Orders\n" +
                "LEFT OUTER JOIN OrdersLines ON Orders.view_id  = OrdersLines.doc_id\n" +
                "WHERE " + sp + "\n";
        return sq;
    }

    /* запрос считам сколько строк в табличной части документа
     и в шапке для выгрузки в файл csv
    sp параметры условий запроса
    */
    public static String queryOrdersLinesAcuont() {

        String sq;
        sq = "SELECT (a.count + b.count) as sum\n" +
                "FROM \n" +
                "(SELECT COUNT(*) count FROM Orders) a, \n" +
                "(SELECT COUNT(*) count FROM OrdersLines) b \n";
        return sq;
    }

    /* запрос получаем долг клиентов
    sp параметры условий запроса
    */
    public static String queryCounteragentsDebt(final String sp) {

        String sq;
        sq = "Select Counteragents._id, Counteragents.kod,Counteragents.name, Counteragents.address, CounteragentsDebt.debt\n" +
                "From Counteragents\n" +
                "LEFT OUTER JOIN CounteragentsDebt ON Counteragents.kod  = CounteragentsDebt.kod\n" +
                "WHERE " + sp + "\n" +
                "GROUP by Counteragents.name,Counteragents.kod, Counteragents.address, CounteragentsDebt.debt;";
        return sq;
    }
}
