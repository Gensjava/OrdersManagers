package ua.com.it_st.ordersmanagers.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

import ua.com.it_st.ordersmanagers.Adapters.LoaderDocCursorAdapter;
import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.fragmets.CursorLoaderFragment;

/**
 * Created by Gena on 2017-05-13.
 */

public abstract class LoaderDocFragment extends CursorLoaderFragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

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

    /* создаем класс - интефейс для открытия фрагментов */
    public interface onLoaderDocListener {
        void onOpenFragmentClassBundle(Class<?> fClass, Bundle bundleItem);
    }
}
