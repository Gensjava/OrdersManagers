package ua.com.it_st.ordersmanagers.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import ua.com.it_st.ordersmanagers.Adapters.MySimpleCursorAdapter;
import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.enums.DocTypeOperation;
import ua.com.it_st.ordersmanagers.fragmets.OrderListFragment;
import ua.com.it_st.ordersmanagers.fragmets.OrderNewHeaderFragment;
import ua.com.it_st.ordersmanagers.interfaces.implems.UpdateOrderDB;

/**
 * Created by Gena on 2017-05-13.
 */

public abstract class LoaderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    public static final String NUMBER_ORDER = "NUMBER_ORDER";
    public static final String DATE_ORDER = "DATE_ORDER";
    public static final String ID_ORDER = "ID_ORDER";
    public static final String DOC_TYPE_OPERATION = "DOC_TYPE_OPERATION";
    public static SQLiteDatabase sDb;

    protected TextView summaDoc;
    protected TextView header_journal;
    protected TextView periodDoc;
    protected View rootView;
    protected String mQuerySum;
    protected String mQueryList;
    protected SimpleCursorAdapter scAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* макет фрагмента */
        rootView = inflater.inflate(R.layout.main_header_list, container,
                false);
        /* открываем подключение к БД */
        sDb = SQLiteOpenHelperUtil.getInstance().getDatabase();

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

        /* формируем столбцы сопоставления */
        String[] from = new String[]{};
        int[] to = new int[]{};
        /* создааем адаптер и настраиваем список */
        scAdapter = new MySimpleCursorAdapter(getActivity(), R.layout.main_list_item, null, from, to, 0, this);
        /* сам список */
        ListView lvData = (ListView) rootView.findViewById(R.id.main_heander_list_position);
        lvData.setAdapter(scAdapter);
        /* создаем лоадер для чтения данных */
        getActivity().getSupportLoaderManager().initLoader(0, null, this);
        getActivity().getSupportLoaderManager().initLoader(2, null, this);

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
                return new MyCursorLoader(getActivity(), mQueryList, sDb);
            case 2:/*получаем сумму всех заазов*/
                return new MyCursorLoader(getActivity(), mQuerySum, sDb);
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
                UpdateOrderDB.setsCurrentNumber((short) data.getCount());
                break;
            case 2:
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
    private void updataSumOrders(String sumOrders) {

        if (sumOrders == null) {
            sumOrders = getString(R.string.zero_point_text);
        }
        summaDoc.setText(sumOrders);
    }

    /* обработка кликов на кнопки */
    @Override
    public void onClick(final View view) {
        switch (view.getId()) {

            case R.id.main_heander_image_plus:
                final OrderListFragment.onEventListener someEventListener = (OrderListFragment.onEventListener) getActivity();
                someEventListener.onOpenFragmentClass(OrderNewHeaderFragment.class);
                /*чистим док заказ и содаем новый заказ*/
                UpdateOrderDB.clearOrderHeader(DocTypeOperation.NEW);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        /* выходим из загрузчкика*/
        getActivity().getSupportLoaderManager().destroyLoader(0);
        getActivity().getSupportLoaderManager().destroyLoader(2);
    }

    @Override
    public void onResume() {
        super.onResume();

        /* создаем загрузчик */
        getActivity().getSupportLoaderManager().initLoader(0, null, this);
        getActivity().getSupportLoaderManager().initLoader(2, null, this);
         /* обновляем курсор */
        getActivity().getSupportLoaderManager().getLoader(0).forceLoad();
        getActivity().getSupportLoaderManager().getLoader(2).forceLoad();
    }

    /* создаем класс - интефейс для открытия фрагментов */
    public interface onEventListener {
        void onOpenFragmentClass(Class<?> fClass);

        void onOpenFragmentClassBundle(Class<?> fClass, Bundle bundleItem);
    }
}
