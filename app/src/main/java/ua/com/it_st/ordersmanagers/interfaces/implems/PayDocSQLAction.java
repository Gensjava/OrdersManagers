package ua.com.it_st.ordersmanagers.interfaces.implems;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.interfaces.SQLAction;
import ua.com.it_st.ordersmanagers.models.Pays;
import ua.com.it_st.ordersmanagers.sqlTables.TablePays;
import ua.com.it_st.ordersmanagers.sqlTables.TablePaysLines;
import ua.com.it_st.ordersmanagers.utils.InfoUtil;
import ua.com.it_st.ordersmanagers.utils.SQLiteOpenHelperUtil;

/**
 * Created by Gena on 2017-05-28.
 */

public class PayDocSQLAction implements SQLAction {

    private static SQLiteDatabase mDB;
    private Context mContext;

    public PayDocSQLAction(Context mContext) {
        this.mContext = mContext;
        mDB = SQLiteOpenHelperUtil.getInstance().getDatabase();
    }

    @Override
    public boolean add(Object object) {
        Pays pays = (Pays) object;
        /* начинаем транзакцию */
        mDB.beginTransaction();
        /* Делаем запись
        * */
        /* шапка*/
        long inTable = mDB.insert(
                TablePays.TABLE_NAME,
                null,
                TablePays.getContentValues(pays));

        if (inTable == -1) {
            InfoUtil.showErrorAlertDialog(mContext.getString(R.string.eror_save_cap_doc), mContext.getString(R.string.error_position), mContext);
            mDB.endTransaction();
            return false;
        }
         /* табличная часть*/
        /*создаем новые позиции*/
        for (final Pays.PaysLines pay : pays.getPaysLines()) {
            if (pay.getSum_nat() != 0 || pay.getSum_usd() != 0) {
                long inTableLines = mDB.insert(
                        TablePaysLines.TABLE_NAME,
                        null,
                        TablePaysLines.getContentValues(pay));
                if (inTableLines == -1) {
                    InfoUtil.Tost(mContext.getString(R.string.error_position) + pays.getId() + ")", mContext);
                    mDB.endTransaction();
                    return false;
                }
            }
        }
        /* заканчиваем транзакцию */
        mDB.setTransactionSuccessful();
        mDB.endTransaction();
        return true;
    }

    @Override
    public boolean update(Object object) {
        Pays pays = (Pays) object;
         /* начинаем транзакцию */
        mDB.beginTransaction();
        /* Делаем запись заказа
        * */
        /* шапка*/
        long inTable = mDB.update(
                TablePays.TABLE_NAME,
                TablePays.getContentValuesUpdata(pays),
                "Pays.view_id = ?",
                new String[]{pays.getId()});

        if (inTable == -1) {
            InfoUtil.showErrorAlertDialog(mContext.getString(R.string.eror_save_cap_doc), mContext.getString(R.string.eror_add_new_order), mContext);
            mDB.endTransaction();
            return false;
        }

         /* табличная часть*/
        /*чистим табличную часть*/
        long inTableLines = mDB.delete(
                TablePaysLines.TABLE_NAME,
                "PaysLines.doc_id = ?",
                new String[]{pays.getId()});

        /*обновляем позиции заказа (создаем новые)*/
        for (final Pays.PaysLines pay : pays.getPaysLines()) {
            if (pay.getSum_nat() != 0 || pay.getSum_usd() != 0) {
                long inTableLinesNew = mDB.insert(TablePaysLines.TABLE_NAME,
                        null,
                        TablePaysLines.getContentValues(pay));

                if (inTableLines == -1 || inTableLinesNew == -1) {
                    InfoUtil.Tost(mContext.getString(R.string.error_position) + pays.getId() + ")", mContext);
                    mDB.endTransaction();
                    return false;
                }
            }
        }
        /* заканчиваем транзакцию */
        mDB.setTransactionSuccessful();
        mDB.endTransaction();
        return true;
    }

    @Override
    public boolean delete(Object object) {
        return false;
    }
}
