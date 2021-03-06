package ua.com.it_st.ordersmanagers.interfaces.implems;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.interfaces.DocAction;
import ua.com.it_st.ordersmanagers.models.Documents;
import ua.com.it_st.ordersmanagers.models.Orders;
import ua.com.it_st.ordersmanagers.sqlTables.TableOrdersLines;
import ua.com.it_st.ordersmanagers.utils.Dialogs;
import ua.com.it_st.ordersmanagers.utils.InfoUtil;

public class OrderDocAction implements DocAction {

    /*текущий номер заказа*/
    private SQLiteDatabase mDB;
    private Context mContext;

    public OrderDocAction(SQLiteDatabase pDB, Context pContext) {
        mDB = pDB;
        mContext = pContext;
    }

    @Override
    public boolean add(Documents documents) {

        Orders orders = (Orders) documents;
        /* начинаем транзакцию */
        mDB.beginTransaction();
        /* Делаем запись заказа
        * */
        /* шапка*/
        long inTable = mDB.insert(
                ua.com.it_st.ordersmanagers.sqlTables.TableOrders.TABLE_NAME,
                null,
                ua.com.it_st.ordersmanagers.sqlTables.TableOrders.getContentValues(orders));

        if (inTable == -1) {
            InfoUtil.showErrorAlertDialog(mContext.getString(R.string.eror_save_cap_doc), mContext.getString(R.string.eror_add_new_order), mContext);
            mDB.endTransaction();
            return false;
        }
         /* табличная часть*/
        /*создаем новые позиции заказа*/
        for (final Orders.OrdersLines aMCart : OrderListAction.mCart) {
            long inTableLines = mDB.insert(
                    TableOrdersLines.TABLE_NAME,
                    null,
                    TableOrdersLines.getContentValues(aMCart, orders.getId()));

            if (inTableLines == -1) {
                InfoUtil.Tost(mContext.getString(R.string.error_position) + orders.getId() + ")", mContext);
                mDB.endTransaction();
                return false;
            }
        }
          /*Возвращаем положение выбор диалога к-во по умолчанию*/
        Dialogs.openDialog = false;
        /* заканчиваем транзакцию */
        mDB.setTransactionSuccessful();
        mDB.endTransaction();
        return true;
    }

    @Override
    public boolean update(Documents documents) {
        Orders orders = (Orders) documents;
         /* начинаем транзакцию */
        mDB.beginTransaction();
        /* Делаем запись заказа
        * */
        /* шапка*/
        long inTable = mDB.update(
                ua.com.it_st.ordersmanagers.sqlTables.TableOrders.TABLE_NAME,
                ua.com.it_st.ordersmanagers.sqlTables.TableOrders.getContentValuesUpdata(orders),
                "Orders.view_id = ?",
                new String[]{orders.getId()});

        if (inTable == -1) {
            InfoUtil.showErrorAlertDialog(mContext.getString(R.string.eror_save_cap_doc), mContext.getString(R.string.eror_add_new_order), mContext);
            mDB.endTransaction();
            return false;
        }

         /* табличная часть*/
        /*чистим табличную часть*/
        long inTableLines = mDB.delete(
                TableOrdersLines.TABLE_NAME,
                "OrdersLines.doc_id = ?",
                new String[]{orders.getId()});

        /*обновляем позиции заказа (создаем новые)*/
        for (final Orders.OrdersLines aMCart : OrderListAction.mCart) {

            long inTableLinesNew = mDB.insert(TableOrdersLines.TABLE_NAME,
                    null,
                    TableOrdersLines.getContentValues(aMCart, orders.getId()));

            if (inTableLines == -1 || inTableLinesNew == -1) {
                InfoUtil.Tost(mContext.getString(R.string.error_position) + orders.getId() + ")", mContext);
                mDB.endTransaction();
                return false;
            }
        }
          /*Возвращаем положение выбор диалога к-во по умолчанию*/
        Dialogs.openDialog = false;
        /* заканчиваем транзакцию */
        mDB.setTransactionSuccessful();
        mDB.endTransaction();
        return true;
    }

    @Override
    public boolean delete(Documents documents) {
        return false;
    }

}
