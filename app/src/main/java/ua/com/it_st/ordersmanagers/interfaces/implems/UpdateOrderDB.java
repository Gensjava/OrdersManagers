package ua.com.it_st.ordersmanagers.interfaces.implems;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.interfaces.OrderAction;
import ua.com.it_st.ordersmanagers.models.OrderDoc;
import ua.com.it_st.ordersmanagers.sqlTables.TableOrders;
import ua.com.it_st.ordersmanagers.sqlTables.TableOrdersLines;
import ua.com.it_st.ordersmanagers.utils.ConstantsUtil;
import ua.com.it_st.ordersmanagers.utils.Dialogs;
import ua.com.it_st.ordersmanagers.utils.InfoUtil;

public class UpdateOrderDB implements OrderAction {

    private SQLiteDatabase mDB;
    private Context mContext;

    public UpdateOrderDB(SQLiteDatabase pDB, Context pContext) {
        mDB = pDB;
        mContext = pContext;
    }

    @Override
    public boolean add() {
/* начинаем транзакцию */
        mDB.beginTransaction();
        /* Делаем запись заказа
        * */
        /* шапка*/
        long inTable = mDB.insert(
                TableOrders.TABLE_NAME,
                null,
                TableOrders.getContentValues(ConstantsUtil.mCurrentOrder));

        if (inTable == -1) {
            InfoUtil.showErrorAlertDialog(mContext.getString(R.string.eror_save_cap_doc), mContext.getString(R.string.eror_add_new_order), mContext);
            mDB.endTransaction();
            return false;
        }
         /* табличная часть*/
        /*создаем новые позиции заказа*/
        for (final OrderDoc.OrderLines aMCart : ConstantsUtil.mCart) {
            long inTableLines = mDB.insert(
                    TableOrdersLines.TABLE_NAME,
                    null,
                    TableOrdersLines.getContentValues(aMCart, ConstantsUtil.mCurrentOrder.getId()));

            if (inTableLines == -1) {
                InfoUtil.Tost(mContext.getString(R.string.error_position) + ConstantsUtil.mCurrentOrder.getId() + ")", mContext);
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
    public boolean update() {

         /* начинаем транзакцию */
        mDB.beginTransaction();
        /* Делаем запись заказа
        * */
        /* шапка*/
        long inTable = mDB.update(
                TableOrders.TABLE_NAME,
                TableOrders.getContentValuesUpdata(ConstantsUtil.mCurrentOrder),
                "Orders.view_id = ?",
                new String[]{ConstantsUtil.mCurrentOrder.getId()});

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
                new String[]{ConstantsUtil.mCurrentOrder.getId()});

        /*обновляем позиции заказа (создаем новые)*/
        for (final OrderDoc.OrderLines aMCart : ConstantsUtil.mCart) {

            long inTableLinesNew = mDB.insert(TableOrdersLines.TABLE_NAME,
                    null,
                    TableOrdersLines.getContentValues(aMCart, ConstantsUtil.mCurrentOrder.getId()));

            if (inTableLines == -1 || inTableLinesNew == -1) {
                InfoUtil.Tost(mContext.getString(R.string.error_position) + ConstantsUtil.mCurrentOrder.getId() + ")", mContext);
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
    public boolean delete() {
        return false;
    }
}
