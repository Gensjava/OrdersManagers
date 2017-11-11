package ua.com.it_st.ordersmanagers.utils;

/*клас предназначен для запросов к БД*/
public class SQLQuery {
    /*
    запрос товаров
    sp - параметры условий запроса
    */
    public static String queryGoods(final String sp) {
        String sq;
        sq = "Select Products.name, Products.kod, Products.id_category, Products.is_category,\n" +
                "Prices.price,(GoodsByStores.Amount - IFNULL(OrdersLinesD.amount,0)) as amount\n" +
                "FROM Products\n" +
                "LEFT OUTER JOIN GoodsByStores ON Products.kod = GoodsByStores.kod_coods\n" +
                "LEFT OUTER JOIN Prices ON Products.kod = Prices.kod\n" +
                "LEFT OUTER JOIN (\n" +
                "Select OrdersLines.goods_id,Sum(OrdersLines.amount) as amount\n" +
                "FROM OrdersLines\n" +
                "LEFT OUTER JOIN Orders ON OrdersLines.doc_id  = Orders.view_id\n" +
                "Where Orders.type <> 'NO_HELD'\n" +
                "GROUP by OrdersLines.goods_id) as  OrdersLinesD ON Products.kod =  OrdersLinesD.goods_id\n" +
                "WHERE " + sp + "\n";
        return sq;
    }

    /*
    запрос журнал заказов
    sp параметры условий запроса
    */
    public static String queryOrders(final String sp) {
        String sq;
        sq = "Select Orders._id, Orders.type,  Orders.view_id, Orders.date, Orders.number, Orders.total, " +
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
    запрос журнал заказов
    sp параметры условий запроса
    */
    public static String queryPays(final String sp) {
        String sq;
        sq = "Select  Pays._id, Pays.type,  Pays.view_id, Pays.date, Pays.number, Pays.total_nat, Pays.total_usd," +
                "Counteragents.name, Counteragents.address\n" +
                "FROM Pays\n" +
                "LEFT OUTER JOIN Counteragents ON Pays.client_id  = Counteragents.kod " +
                "and Pays.client_adress  = Counteragents.address\n" +
                "WHERE " + sp + "\n";

        return sq;
    }

    /*
    запрос журнал док оплат сумма всех оплат
    sp параметры условий запроса
    */
    public static String queryPayDocSum(final String sp) {
        String sq;
        sq = "Select Sum(Pays.total_nat) as sum_nat,Sum(Pays.total_usd) as sum_usd  " +
                "FROM Pays\n" +
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
    запрос журнал заказов для выгрузки в файл csv
    sp параметры условий запроса
    */
    public static String queryPaysHeaderFilesCsv(final String sp) {
        String sq;
        sq = "Select Pays.view_id, Pays.type,  Pays.date, Pays.number, Pays.completed, Pays.agent_id," +
                "Pays.company_id, Pays.client_id, Pays.total_usd, Pays.total_nat,Pays.note\n" +
                "FROM Pays\n" +
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
                "Companies.name as name_comp, Companies.kod as kod_comp,\n" +
                "Stores.name as name_type_stores, Stores.kod as kod_type_stores,\n" +
                "Counteragents.name as name_contr, Counteragents.address as address_contr, Counteragents.kod as kod_contr,\n" +
                "TypePrices.name as name_type_price, TypePrices.kod as kod_type_price\n" +
                "FROM Orders\n" +
                "LEFT OUTER JOIN Companies ON Orders.company_id  = Companies.kod " +
                "LEFT OUTER JOIN Stores ON Orders.store_id  = Stores.kod \n" +
                "LEFT OUTER JOIN Counteragents ON Orders.client_id  = Counteragents.kod \n" +
                "LEFT OUTER JOIN TypePrices ON Orders.price_id  = TypePrices.kod \n" +
                "WHERE " + sp + "\n";

        return sq;
    }

    /*
        запрос шапки документа
        sp параметры условий запроса
        */
    public static String queryPaysHeader(final String sp) {
        String sq;
        sq = "Select Pays._id, Pays.note, Pays.agent_id,\n" +
                "Companies.name as name_comp, Companies.kod as kod_comp,\n" +
                "Counteragents.name as name_contr, Counteragents.address as address_contr, Counteragents.kod as kod_contr\n" +
                "FROM Pays\n" +
                "LEFT OUTER JOIN Companies ON Pays.company_id  = Companies.kod\n" +
                "LEFT OUTER JOIN Counteragents ON Pays.client_id  = Counteragents.kod " +
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

    /* запрос табличной части документа
     sp параметры условий запроса
     */

    public static String queryPaysLinesEdit(final String sp, final String sp2) {
        String sq;
        sq = "select CounteragentsDebtDocs._id, CounteragentsDebtDocs.DocDate, CounteragentsDebtDocs.DocName,CounteragentsDebtDocs.summa, CounteragentsDebtDocs.Debt, PaysLinesD.amount_nat, PaysLinesD.amount_usd, PaysLinesD.line_id, Currencies.name, Currencies.kod\n" +
                " From CounteragentsDebtDocs\n" +
                "LEFT OUTER JOIN Currencies ON CounteragentsDebtDocs.currency  = Currencies.kod\n" +
                "LEFT OUTER JOIN (\n" +
                "                Select *\n" +
                "                FROM PaysLines\n" +
                "                LEFT OUTER JOIN Pays ON PaysLines.doc_id  = Pays.view_id\n" +
                "                Where PaysLines.doc_id = \"" + sp2 + "\"\n" +
                "                ) as  PaysLinesD ON PaysLinesD.doc_date = CounteragentsDebtDocs.DocDate  \n" +
                " and CounteragentsDebtDocs.DocName = PaysLinesD.doc_number\n" +
                "WHERE " + sp + "\n";
        ;

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

    /* запрос табличной части документа  для выгрузки в файл csv
     sp параметры условий запроса
     */
    public static String queryPaysLinesFilesCsv(final String sp) {
        String sq;
        sq = "Select PaysLines.doc_id, PaysLines.currency_id, PaysLines.Amount_nat, PaysLines.Amount_usd,PaysLines.doc_date, PaysLines.doc_number, PaysLines.line_id \n" +
                "FROM Pays\n" +
                "LEFT OUTER JOIN PaysLines ON Pays.view_id  = PaysLines.doc_id\n" +
                "WHERE " + sp + "\n";
        return sq;
    }

    /* запрос считам сколько строк в табличной части документа
     и в шапке для выгрузки в файл csv
    sp параметры условий запроса
    */
    public static String queryOrdersLinesAmount() {
        String sq;
        sq = "SELECT (a.count + b.count) as sum\n" +
                "FROM\n" +
                "(SELECT COUNT(*) count\n" +
                "FROM Orders\n" +
                "WHERE Orders.type <> 'NO_HELD') a,\n" +
                "(SELECT COUNT(*) count\n" +
                "FROM Orders\n" +
                "LEFT OUTER JOIN OrdersLines ON Orders.view_id = OrdersLines.doc_id\n" +
                "WHERE Orders.type <> 'NO_HELD' ) b ";
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

    /* запрос получаем долг клиентов по документам
    sp параметры условий запроса
    */
    public static String queryCounteragentsDebtDocs(final String sp) {
        String sq;
        sq = "Select CounteragentsDebtDocs.DocDate, CounteragentsDebtDocs.DocName, CounteragentsDebtDocs.summa, CounteragentsDebtDocs.Debt, Currencies.kod, Currencies.name, CounteragentsDebtDocs._id \n" +
                "From CounteragentsDebtDocs\n" +
                "LEFT OUTER JOIN Currencies ON CounteragentsDebtDocs.currency  = Currencies.kod \n" +
                "WHERE " + sp + ";";
        return sq;
    }

    /* запрос считам сколько документов
   sp параметры условий запроса
   */
    public static String queryDocsAmount(String sp, String tableName) {
        String sq;
        sq = "select  COUNT(*) from " + tableName + " " +
                "WHERE " + sp + ";";
        return sq;
    }
}
