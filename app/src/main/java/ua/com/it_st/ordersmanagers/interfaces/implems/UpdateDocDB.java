package ua.com.it_st.ordersmanagers.interfaces.implems;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.enums.DocTypeOperation;
import ua.com.it_st.ordersmanagers.interfaces.DocAction;
import ua.com.it_st.ordersmanagers.models.OrderDoc;
import ua.com.it_st.ordersmanagers.models.PayDoc;
import ua.com.it_st.ordersmanagers.sqlTables.TableOrders;
import ua.com.it_st.ordersmanagers.sqlTables.TableOrdersLines;
import ua.com.it_st.ordersmanagers.utils.Dialogs;
import ua.com.it_st.ordersmanagers.utils.InfoUtil;

public class UpdateDocDB implements DocAction {

    /* текущий новый заказ */
    public static OrderDoc mCurrentOrder = new OrderDoc();
    /* it is a new play doc */
    public static PayDoc mCurrentPayDoc = new PayDoc();
    /*текущий номер заказа*/
    public static short sCurrentNumber;
    private SQLiteDatabase mDB;
    private Context mContext;

    public UpdateDocDB(SQLiteDatabase pDB, Context pContext) {
        mDB = pDB;
        mContext = pContext;
    }

    /* записываем новый номер заказа */
    public static void setsCurrentNumber(short pCurrentNumber) {

        if (pCurrentNumber == 0) {
            sCurrentNumber = 1;
        } else {
            sCurrentNumber = ++pCurrentNumber;
        }
    }

    /* проверяем на обязательные поля шапки документа*/
    public static boolean checkHeaderOrder(Context context) {

        boolean bCheck = false;

        if (mCurrentOrder.getDocNumber() == null
                || mCurrentOrder.getDocDate() == null
                || mCurrentOrder.getAgentId() == null
                || mCurrentOrder.getFirmId() == null
                || mCurrentOrder.getPriceCategoryId() == null
                || mCurrentOrder.getClientId() == null
                || mCurrentOrder.getAdress() == null) {

            bCheck = true;
            InfoUtil.Tost(context.getString(R.string.not_all_cap_mandatory_filled), context);
        }
        return bCheck;
    }

    /*чистим документ заказа и устанавливаем вид операции дока*/
    public static void clearOrderHeader(final DocTypeOperation docTypeOperation) {
        mCurrentOrder = new OrderDoc();
        mCurrentOrder.setTypeOperation(docTypeOperation);
    }

    /*чистим документ заказа и устанавливаем вид операции дока*/
    public static void clearPayHeader(final DocTypeOperation docTypeOperation) {
        mCurrentPayDoc = new PayDoc();
        mCurrentPayDoc.setTypeOperation(docTypeOperation);
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
                TableOrders.getContentValues(mCurrentOrder));

        if (inTable == -1) {
            InfoUtil.showErrorAlertDialog(mContext.getString(R.string.eror_save_cap_doc), mContext.getString(R.string.eror_add_new_order), mContext);
            mDB.endTransaction();
            return false;
        }
         /* табличная часть*/
        /*создаем новые позиции заказа*/
        for (final OrderDoc.OrderLines aMCart : UpDateDocList.mCart) {
            long inTableLines = mDB.insert(
                    TableOrdersLines.TABLE_NAME,
                    null,
                    TableOrdersLines.getContentValues(aMCart, mCurrentOrder.getId()));

            if (inTableLines == -1) {
                InfoUtil.Tost(mContext.getString(R.string.error_position) + mCurrentOrder.getId() + ")", mContext);
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
                TableOrders.getContentValuesUpdata(mCurrentOrder),
                "Orders.view_id = ?",
                new String[]{mCurrentOrder.getId()});

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
                new String[]{mCurrentOrder.getId()});

        /*обновляем позиции заказа (создаем новые)*/
        for (final OrderDoc.OrderLines aMCart : UpDateDocList.mCart) {

            long inTableLinesNew = mDB.insert(TableOrdersLines.TABLE_NAME,
                    null,
                    TableOrdersLines.getContentValues(aMCart, mCurrentOrder.getId()));

            if (inTableLines == -1 || inTableLinesNew == -1) {
                InfoUtil.Tost(mContext.getString(R.string.error_position) + mCurrentOrder.getId() + ")", mContext);
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
