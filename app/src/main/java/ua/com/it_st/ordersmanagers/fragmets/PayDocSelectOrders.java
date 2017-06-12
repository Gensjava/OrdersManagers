package ua.com.it_st.ordersmanagers.fragmets;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.UUID;

import ua.com.it_st.ordersmanagers.Adapters.LoaderDocCursorAdapter;
import ua.com.it_st.ordersmanagers.Adapters.SelectPayDocOrdersAdapter;
import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.enums.DocTypeOperation;
import ua.com.it_st.ordersmanagers.interfaces.implems.PayDocBasementAction;
import ua.com.it_st.ordersmanagers.interfaces.implems.PayDocSQLAction;
import ua.com.it_st.ordersmanagers.models.Pays;
import ua.com.it_st.ordersmanagers.utils.ConstantsUtil;
import ua.com.it_st.ordersmanagers.utils.GlobalCursorLoader;
import ua.com.it_st.ordersmanagers.utils.SQLQuery;
import ua.com.it_st.ordersmanagers.utils.SQLiteOpenHelperUtil;

/**
 * Created by Gena on 2017-05-22.
 */

public class PayDocSelectOrders extends CursorLoaderFragment implements View.OnClickListener {

    private SelectPayDocOrdersAdapter scAdapter;
    private Pays pays;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /* макет фрагмента */
        View rootView = inflater.inflate(R.layout.pay_dogs_select_container, container, false);

        TextView totalSum = (TextView) rootView.findViewById(R.id.pay_dogs_sum);

        pays = ((MainActivity) getActivity()).getmCurrentPay();

        setCountLoad((byte) 1);
        selectQuery();

        super.onCreateView(inflater, container, savedInstanceState);

        /* создааем адаптер и настраиваем список */
        scAdapter = new SelectPayDocOrdersAdapter(getActivity(), R.layout.pay_list_item, null, new String[]{}, new int[]{}, 0, pays, totalSum);
         /* сам список */
        ListView lvData = (ListView) rootView.findViewById(R.id.pay_select_dogs);
        lvData.setAdapter(scAdapter);

         /*кнопка далее к следующему этапу*/
        ImageView imViewAdd = (ImageView) rootView.findViewById(R.id.pay_dogs_container_image);
        imViewAdd.setOnClickListener(this);

        return rootView;
    }

    private void selectQuery() {

        switch (pays.getmTypeOperation()) {

            case NEW:
                setQuery(SQLQuery.queryCounteragentsDebtDocs("CounteragentsDebtDocs.ClientId = ?"));
                setParamsQuery(new String[]{pays.getCounteragent().getKod()});
                break;
            case EDIT:
                setQuery(SQLQuery.queryPaysLinesEdit("CounteragentsDebtDocs.ClientId = ?", pays.getId()));
                setParamsQuery(new String[]{pays.getCounteragent().getKod()});
                break;
            case COPY:
                setQuery(SQLQuery.queryPaysLinesEdit("CounteragentsDebtDocs.ClientId = ?", pays.getId()));
                setParamsQuery(new String[]{pays.getCounteragent().getKod()});

                /* сгениророваный номер документа заказа ИД для 1с */
                pays.setId(String.valueOf(UUID.randomUUID()));
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new GlobalCursorLoader(getActivity(), getQuery(), getParamsQuery(), CursorLoaderFragment.sDb);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        scAdapter.swapCursor(data);
        scAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        scAdapter.swapCursor(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay_dogs_container_image:
                if (onRecord()) {
                    final PayHeaderDoc.onEventListener someEventListener = (PayHeaderDoc.onEventListener) getActivity();
                    someEventListener.onOpenFragmentClass(PayDocListDocFragment.class);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onRecord() {
        boolean failure;

        PayDocBasementAction payDocBasementAction = new PayDocBasementAction(pays);
        payDocBasementAction.sum();

        PayDocSQLAction payDocSQLAction = new PayDocSQLAction(getActivity());

        if (pays.getmTypeOperation() == DocTypeOperation.NEW || pays.getmTypeOperation() == DocTypeOperation.COPY) {
            failure = payDocSQLAction.add(pays);
        } else {
            failure = payDocSQLAction.update(pays);
        }
        return failure;
    }

    /**
     * Created by Gena on 2017-05-13.
     */

    public abstract static class LoaderDocFragment extends CursorLoaderFragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

        public static final String NUMBER_DOC = "NUMBER_DOC";
        public static final String DATE_DOC = "DATE_DOC";
        public static final String ID_DOC = "ID_DOC";
        public static final String TYPE_OPERATION_DOC = "TYPE_OPERATION_DOC";
        public static SQLiteDatabase sDb;

        protected TextView summaDoc;
        protected TextView header_journal;
        protected TextView periodDoc;
        protected View rootView;

        private String mQuerySum;
        private String mQueryList;
        private SimpleCursorAdapter scAdapter;
        private short nextNumberDoc;
        private Class aClass;
        private String tableName;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            /* макет фрагмента */
            rootView = inflater.inflate(R.layout.main_header_list, container,
                    false);
            sDb = SQLiteOpenHelperUtil.getInstance().getDatabase();
            /* открываем подключение к БД */
            setCountLoad((byte) 2);

            /*устанавливаем период журнала*/
            periodDoc = (TextView) rootView.findViewById(R.id.main_heander_period);
            setPeriodDoc(getString(R.string.with) + ConstantsUtil.getDate() + getString(R.string.on) + ConstantsUtil.getDate());
            //
            header_journal = (TextView) rootView.findViewById(R.id.main_heander_text_Jurnal);
              /*подвал журнал заказов */
            summaDoc = (TextView) rootView.findViewById(R.id.main_header_list_velue_text);
           /* кнопка далее переход на следующий этап*/
            ImageView imViewAdd = (ImageView) rootView.findViewById(R.id.main_heander_image_plus);
            /* слушатель кнопки далее */
            imViewAdd.setOnClickListener(this);

           /* создааем адаптер и настраиваем список */
            scAdapter = new LoaderDocCursorAdapter(getActivity(), R.layout.main_list_item, null, new String[]{}, new int[]{}, 0, this);
            /* сам список */
            ListView lvData = (ListView) rootView.findViewById(R.id.main_heander_list_position);
            lvData.setAdapter(scAdapter);

            /**/
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_drawer);
            MainActivity.chickMainFragment = true;

            return rootView;
        }

        //установка периода в журнале
        public void setPeriodDoc(String pPeriodDoc) {
            periodDoc.setText(pPeriodDoc);
        }

        /* функция
            * отрабатывает при создании */
        @Override
        public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
            switch (id) {
                case 0:/*получаем все заазы*/
                    return new GlobalCursorLoader(getActivity(), mQueryList, getParamsQuery(), sDb);
                case 1:/*получаем сумму всех заазов*/
                    return new GlobalCursorLoader(getActivity(), mQuerySum, getParamsQuery(), sDb);
                default:
                    return null;
            }
        }

        /*функция отрабатывает после выполнения*/
        @Override
        public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {

            switch (loader.getId()) {
                case 0:
                    scAdapter.swapCursor(data);
                    /*следующий номер заказа*/
                    setNextNumberDoc((short) (data.getCount() + 1));
                    break;
                case 1:
                    final int cSumIndex = data.getColumnIndex("sum_orders");
                    data.moveToFirst();
                    final String cSum = data.getString(cSumIndex);
                    updataSumOrders(cSum);
                    break;
                default:
                    break;
            }
        }

        /* функция перезапуск */
        @Override
        public void onLoaderReset(final Loader<Cursor> loader) {
            scAdapter.swapCursor(null);
        }

        /*Обновляем подвал сумму заказов*/
        public void updataSumOrders(String sumOrders) {

            if (sumOrders == null) {
                sumOrders = getString(R.string.zero_point_text);
            }
            summaDoc.setText(sumOrders);
        }

        public short getNextNumberDoc() {
            return nextNumberDoc;
        }

        public void setNextNumberDoc(short nextNumberDoc) {
            this.nextNumberDoc = nextNumberDoc;
        }


        public void setmQuerySum(String mQuerySum) {
            this.mQuerySum = mQuerySum;
        }

        public void setmQueryList(String mQueryList) {
            this.mQueryList = mQueryList;
        }

        public Class getaClass() {
            return aClass;
        }

        public void setaClass(Class aClass) {
            this.aClass = aClass;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        /* создаем класс - интефейс для открытия фрагментов */
        public interface onLoaderDocListener {
            void onOpenFragmentClassBundle(Class<?> fClass, Bundle bundleItem);
        }
    }
}
