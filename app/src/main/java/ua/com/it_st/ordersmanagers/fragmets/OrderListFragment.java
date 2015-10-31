package ua.com.it_st.ordersmanagers.fragmets;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedHashSet;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.enums.DocType;
import ua.com.it_st.ordersmanagers.enums.DocTypeOperation;
import ua.com.it_st.ordersmanagers.sqlTables.TableCounteragents;
import ua.com.it_st.ordersmanagers.sqlTables.TableOrders;
import ua.com.it_st.ordersmanagers.utils.ConstantsUtil;
import ua.com.it_st.ordersmanagers.utils.SQLQuery;
import ua.com.it_st.ordersmanagers.utils.SQLiteOpenHelperUtil;

/* Класс отображает список заказов сделанные курьером
*  В списке есть контекстное меню построенное на  Spinner */

public class OrderListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    public static final String NUMBER_ORDER = "NUMBER_ORDER";
    public static final String DATE_ORDER = "DATE_ORDER";
    public static final String ID_ORDER = "ID_ORDER";
    public static final String DOC_TYPE_OPERATION = "DOC_TYPE_OPERATION";
    private static SQLiteDatabase sDb;
    private SimpleCursorAdapter scAdapter;
    private TextView ordersSum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* макет фрагмента */
        View rootView = inflater.inflate(R.layout.main_header_list, container,
                false);
        /* открываем подключение к БД */
        sDb = SQLiteOpenHelperUtil.getInstance().getDatabase();
        /* формируем столбцы сопоставления */
        String[] from = new String[]{};
        int[] to = new int[]{};
        /* создааем адаптер и настраиваем список */
        scAdapter = new MySimpleCursorAdapter(getActivity(), R.layout.main_list_item, null, from, to, 0);
        /* сам список */
        ListView lvData = (ListView) rootView.findViewById(R.id.main_heander_list_position);
        lvData.setAdapter(scAdapter);
        /* создаем лоадер для чтения данных */
        getActivity().getSupportLoaderManager().initLoader(0, null, this);
        getActivity().getSupportLoaderManager().initLoader(2, null, this);
        /* кнопка далее переход на следующий этап*/
        ImageView imViewAdd = (ImageView) rootView.findViewById(R.id.main_heander_image_plus);
        /* слушатель кнопки далее */
        imViewAdd.setOnClickListener(this);
        /*устанавливаем период журнала*/
        TextView period = (TextView) rootView.findViewById(R.id.main_heander_period);
        period.setText("c " + ConstantsUtil.getDate() + " по " + ConstantsUtil.getDate());
        /*подвал журнал заказов */
        ordersSum = (TextView) rootView.findViewById(R.id.main_header_list_velue_text);
        /**/
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_drawer);
        MainActivity.chickMainFragment = true;
        return rootView;
    }
    /* функция
    * отрабатывает при создании */
    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        switch (id) {
            case 0:/*получаем все заазы*/
                return new MyCursorLoader(getActivity());
            case 2:/*получаем сумму всех заазов*/
                return new MyCursorLoaderOrdersSum(getActivity());
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
                ConstantsUtil.setsCurrentNumber((short) data.getCount());
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
        ordersSum.setText(sumOrders);
    }
    /* обработка кликов на кнопки */
    @Override
    public void onClick(final View view) {
        switch (view.getId()) {

            case R.id.main_heander_image_plus:
                final onEventListener someEventListener = (onEventListener) getActivity();
                someEventListener.onOpenFragmentClass(OrderNewHeaderFragment.class);
                /*чистим док заказ и содаем новый заказ*/
                ConstantsUtil.clearOrderHeader(DocTypeOperation.NEW);
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
               /* открываем подключение к БД */
        if (sDb == null) {
            sDb = SQLiteOpenHelperUtil.getInstance().getDatabase();
        }

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

    /* создаем класс для загрузки данных из БД Журнала заказов
    * загрузка происходит в фоне */
    private static class MyCursorLoader extends CursorLoader {

        public MyCursorLoader(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {
            return sDb
                    .rawQuery(SQLQuery.queryOrders("Orders._id  <> ?"), new String[]{"null"});
        }
    }

    /* создаем класс для загрузки данных из БД суммы заказов
    * загрузка происходит в фоне */
    private static class MyCursorLoaderOrdersSum extends CursorLoader {

        public MyCursorLoaderOrdersSum(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {
            return sDb
                    .rawQuery(SQLQuery.queryOrdersSum("Orders.type  <> ?"), new String[]{"NO_HELD"});
        }
    }

    /* создаем свой адаптер к Spinner т.к по умолчанию нет возможности
     добалять свои изображения в меню
      */
    private class MenuCustomAdapter extends ArrayAdapter {

        private String[] objects;
        private LayoutInflater mLInflater;

        public MenuCustomAdapter(Context context, int textViewResourceId,
                                 String[] objects) {
            super(context, textViewResourceId, objects);
            this.objects = objects;
            mLInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(final int position, View convertView, ViewGroup parent) {

            convertView = mLInflater.inflate(R.layout.spinner_row, parent, false);

            TextView label = (TextView) convertView.findViewById(R.id.spinner_row_item_text);
            label.setText(objects[position]);

            ImageView icon = (ImageView) convertView.findViewById(R.id.spinner_row_icon);

            switch (position) {
                case 0:
                    icon.setImageResource(R.mipmap.ic_edit);
                    break;
                case 1:
                    icon.setImageResource(R.mipmap.ic_held);
                    break;
                case 2:
                    icon.setImageResource(R.mipmap.ic_no_held);
                    break;
                case 3:
                    icon.setImageResource(R.mipmap.ic_copy);
                    break;
                default:
                    break;
            }
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount() - 1; // делаем невидимым последний элемент
        }
    }

    /* делаем свой адаптер т.к нужно обрабытывать
     клик на контекстное меню*/
    private class MySimpleCursorAdapter extends SimpleCursorAdapter {

        private LayoutInflater mLInflater;

        public MySimpleCursorAdapter(final Context context, final int layout, final Cursor c, final String[] from, final int[] to, final int flags) {
            super(context, layout, c, from, to, flags);
            mLInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            convertView = mLInflater.inflate(R.layout.main_list_item, parent, false);
            /*позиция*/
            Cursor itemCursor = (Cursor) getItem(position);
            /*получаем колонки*/
            final String cId = itemCursor.getString(itemCursor.getColumnIndex(TableOrders.COLUMN_VIEW_ID));
            final String cDate = itemCursor.getString(itemCursor.getColumnIndex(TableOrders.COLUMN_DATE));
            final String cClient = itemCursor.getString(itemCursor.getColumnIndex(TableCounteragents.COLUMN_NAME));
            final String cAdress = itemCursor.getString(itemCursor.getColumnIndex(TableCounteragents.COLUMN_ADDRESS));
            final String cTotal = itemCursor.getString(itemCursor.getColumnIndex(TableOrders.COLUMN_TOTAL));
            final String cNumber = itemCursor.getString(itemCursor.getColumnIndex(TableOrders.COLUMN_NUMBER));
            final String cStatus = itemCursor.getString(itemCursor.getColumnIndex(TableOrders.COLUMN_TYPE));

            /*menu*/
            final Spinner spinner = (Spinner) convertView.findViewById(R.id.main_list_item_image_menu);

            /*дата */
            final TextView date = (TextView) convertView.findViewById(R.id.main_list_item_text_date);
            date.setText(cDate);
            /*клиент*/
            final TextView client = (TextView) convertView.findViewById(R.id.main_list_item_text_client);
            client.setText(cClient);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    spinner.performClick();
                }
            });
            /*адресс*/
            final TextView adress = (TextView) convertView.findViewById(R.id.main_list_item_text_sub_client);
            adress.setText(cAdress);
            /*сумма*/
            final TextView total = (TextView) convertView.findViewById(R.id.main_list_item_sum);
            total.setText(cTotal);
             /*номер*/
            final TextView number = (TextView) convertView.findViewById(R.id.main_list_item_text_number);
            number.setText(cNumber);
            /*статус*/
            final ImageView status = (ImageView) convertView.findViewById(R.id.main_list_item_image_status);
            /* проведен */
            if (cStatus.equals(DocType.HELD.toString())) {
                status.setImageResource(R.mipmap.ic_held);
            } else {
                status.setImageResource(R.mipmap.ic_no_held);/* не проведен */
            }

            /* Настраиваем адаптер */
            String[] spinnerMenu = getResources().getStringArray(R.array.spinner_orders_menu);
            /**/
            final MenuCustomAdapter adapter = new MenuCustomAdapter(getActivity(), R.layout.spinner_row, spinnerMenu);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            /* Вызываем адаптер */
            spinner.setAdapter(adapter);
            spinner.setSelection(4);/*устанавливаем заглушку*/

            /*костыль для того чтоб при начале открытия списка незаполнялось первая позиция
             и при выборе пользователем позицию в меню нормально обрабатывал
            */
            final int[] iCurrentSelection = {spinner.getSelectedItemPosition()};

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent,
                                           View itemSelected, int selectedItemPosition, long selectedId) {

                    if (iCurrentSelection[0] != selectedItemPosition) {

                        final ContentValues data = new ContentValues();

                        switch (selectedItemPosition) {
                            case 0:
                            case 3:

                                Bundle bundleItem = new Bundle();
                                switch (selectedItemPosition) {
                                    case 0:
                                  /*чистим док заказ и редактируем заказ*/
                                        ConstantsUtil.clearOrderHeader(DocTypeOperation.EDIT);
                                        bundleItem.putString(DOC_TYPE_OPERATION, getString(R.string.edit_order));
                                        break;
                                    case 3:
                                  /*чистим док заказ и копируем заказ*/
                                        ConstantsUtil.clearOrderHeader(DocTypeOperation.COPY);
                                        bundleItem.putString(DOC_TYPE_OPERATION, getString(R.string.copy_order));
                                        break;
                                }
                                        /* ТЧ заказа */
                                ConstantsUtil.mCart = new LinkedHashSet<>();
                                /*редактируем док*/

                                bundleItem.putString(ID_ORDER, cId);
                                bundleItem.putString(NUMBER_ORDER, cNumber);
                                bundleItem.putString(DATE_ORDER, cDate);

                                final onEventListener someEventListener = (onEventListener) getActivity();
                                someEventListener.onOpenFragmentClassBundle(OrderNewHeaderFragment.class, bundleItem);
                            case 1:
                                /*проводим док*/
                                data.put(TableOrders.COLUMN_TYPE, DocType.HELD.toString());
                                break;
                            case 2:
                                /*помечаем на удаления док*/
                                data.put(TableOrders.COLUMN_TYPE, DocType.NO_HELD.toString());
                                break;
                            default:
                                break;
                        }
                        /*меняем статус у документов (проведен, не проведен)*/
                        if (!cId.equals("") & selectedItemPosition == 1 || selectedItemPosition == 2) {

                            sDb.update(TableOrders.TABLE_NAME, data, "view_id = ?", new String[]{cId});
                            String[] choose = getResources().getStringArray(R.array.spinner_orders_menu);

                            Toast toast = Toast.makeText(getActivity(),
                                    "Операция выполнена: " + choose[selectedItemPosition], Toast.LENGTH_SHORT);
                            toast.show();

                            getActivity().getSupportLoaderManager().getLoader(0).forceLoad();
                        }
                    } else {
                        iCurrentSelection[0] = selectedItemPosition;
                    }
                }

                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            return convertView;
        }
    }

}
