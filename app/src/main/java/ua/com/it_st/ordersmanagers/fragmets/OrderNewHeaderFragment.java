package ua.com.it_st.ordersmanagers.fragmets;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;


import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.ListView;

import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.enums.DocTypeOperation;
import ua.com.it_st.ordersmanagers.utils.ConstantsUtil;
import ua.com.it_st.ordersmanagers.utils.Dialogs;
import ua.com.it_st.ordersmanagers.utils.InfoUtil;
import ua.com.it_st.ordersmanagers.utils.SQLQuery;
import ua.com.it_st.ordersmanagers.utils.SQLiteOpenHelperUtil;

/*Класс предназначен для отображения и заполнения шапки документа заказа
* Все поля кроме коммнтария являются обязательными для заполнения*/

public class OrderNewHeaderFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String NAME_TABLE = "NAME_TABLE";
    public static String id_order;
    private static SQLiteDatabase sDb;
    private SimpleAdapter mAdapter;
    private View rootView;
    private String[][] mItemsHeader;
    private int mPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.order_new_header_list, container,
                    false);

            String numberDoc;
            String dateDoc;

            /*расчет  позиции кнопки далее к следующему этапу*/
            TextView header = (TextView) rootView.findViewById(R.id.order_new_header_list_header_root);
             /*кнопка далее к следующему этапу*/
            ImageView imViewAdd = (ImageView) rootView.findViewById(R.id.order_new_header_list_image_arrow_right);
            imViewAdd.setOnClickListener(this);

            Bundle bundle = getArguments();
            /*редактируем документ*/
            if (bundle != null &
                    ConstantsUtil.mCurrentOrder != null &
                    ConstantsUtil.mCurrentOrder.getTypeOperation().equals(DocTypeOperation.EDIT)) {

               /*получаем ID дока и подставляем в запрос*/
                id_order = bundle.getString(OrderListFragment.ID_ORDER);
                ConstantsUtil.mCurrentOrder.setId(id_order);
               /*номер документа*/
                numberDoc = bundle.getString(OrderListFragment.NUMBER_ORDER);
                ConstantsUtil.mCurrentOrder.setDocNumber(numberDoc);
                /*дата док*/
                dateDoc = bundle.getString(OrderListFragment.DATE_ORDER);
                ConstantsUtil.mCurrentOrder.setDocDate(dateDoc);
                /*стктус докуента*/
                header.setText(bundle.getString(OrderListFragment.DOC_TYPE_OPERATION));
                  /* открываем подключение к БД */
                sDb = SQLiteOpenHelperUtil.getInstance().getDatabase();
                 /* создаем лоадер для чтения данных */
                getActivity().getSupportLoaderManager().initLoader(0, null, this);

            } else {
                /*создаем новый заказ*/
                /* сгениророваный номер документа заказа ИД для 1с */
                UUID uniqueKey = UUID.randomUUID();
                ConstantsUtil.mCurrentOrder.setId(String.valueOf(uniqueKey));
                /*устанавливаем дату документа и номер*/
                numberDoc = String.valueOf(ConstantsUtil.sCurrentNumber);
                ConstantsUtil.mCurrentOrder.setDocNumber(numberDoc);
                /*нтекущая дата*/
                dateDoc = ConstantsUtil.getDate();
                ConstantsUtil.mCurrentOrder.setDocDate(dateDoc);
                /**/
                        /*вызываем менеджера настроек*/
                SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());
                      /*код на сервере пользователя*/
                String kodAgent = mSettings.getString(getActivity().getString(R.string.id_user), null);
                ConstantsUtil.mCurrentOrder.setAgentId(kodAgent);
               /* так заполняем при операции дока копирвоать */
                if (bundle != null & ConstantsUtil.mCurrentOrder.getTypeOperation().equals(DocTypeOperation.COPY)) {
                       /*получаем ID дока и подставляем в запрос*/
                    id_order = bundle.getString(OrderListFragment.ID_ORDER);
                     /*стктус докуента*/
                    header.setText(bundle.getString(OrderListFragment.DOC_TYPE_OPERATION));
                     /* открываем подключение к БД */
                    sDb = SQLiteOpenHelperUtil.getInstance().getDatabase();
                 /* создаем лоадер для чтения данных */
                    getActivity().getSupportLoaderManager().initLoader(0, null, this);
                }
                        /*устанавливаем мод. корзины*/
                ConstantsUtil.mCurrentOrder.setClickModifitsirovannoiCart(false);
            }

            /*выводим данные дату и номер в шапку*/
            TextView period = (TextView) rootView.findViewById(R.id.order_new_heander_period);
            period.setText(getString(R.string.rNumber) + numberDoc + " " + getString(R.string.rOf) + " " + dateDoc);

            /*массив принимает выбранные занчения шапки и передает их в адаптер*/
            mItemsHeader = new String[5][3];

            /* создаем адаптер */
            mAdapter = new MySimpleAdapter(getActivity(), createList(),
                    R.layout.order_new_header_list_item,
                    new String[]{"title", "imageAvatar"},
                    new int[]{R.id.order_header_list_item_text, R.id.order_header_list_item_image_avatar});
            /* список шапка заказа*/
            final ListView lv = (ListView) rootView.findViewById(R.id.order_new_header_list_position);
            lv.setAdapter(mAdapter);
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrow_left);
        MainActivity.chickMainFragment = false;

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ConstantsUtil.mCurrentOrder.getTypeOperation().equals(DocTypeOperation.EDIT)
                || ConstantsUtil.mCurrentOrder.getTypeOperation().equals(DocTypeOperation.COPY)) {
           /* выходим из загрузчкика*/
            getActivity().getSupportLoaderManager().destroyLoader(0);
        }
    }

    /* создаем список - шапку  для адаптера
    * Иконки и заголовки*/
    private List<Map<String, ?>> createList() {
        /* иконки к шапке заказа */
        Integer[] mPictures = new Integer[]
                {R.mipmap.ic_organization,
                        R.mipmap.ic_stores,
                        R.mipmap.ic_client,
                        R.mipmap.ic_tipe_price,
                        R.mipmap.ic_coment
                };
        /*массив заголовков шапки заказа*/
        String[] headerOrders = getResources().getStringArray(R.array.header_orders);
        /* список параметров шапки заказа */
        List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();
        /*заполняем шапку заказа*/
        for (byte x = 0; x < headerOrders.length; x++) {

            Map<String, Object> map = new HashMap<>();
            map.put(getString(R.string.title), headerOrders[x]);
            map.put(getString(R.string.imageAvatar), mPictures[x]);
            items.add(map);

        }
        return items;
    }
    @Override
    public void onClick(final View view) {
        switch (view.getId()) {

            case R.id.order_new_header_list_image_arrow_right:
                /* проверка шапки*/
                if (!ConstantsUtil.checkHeaderOrder(getActivity())) {
                    final onEventListener someEventListener = (onEventListener) getActivity();
                    someEventListener.onOpenFragmentClass(OrderNewGoodsFragment.class);
                }
                break;
            default:
                break;
        }
    }
    /*
    записываем и обновляем выбранные данные
    заполняем шапку
    */
    public void setSelectUpdate(final String[] item) {
        mItemsHeader[mPosition] = item;
        mAdapter.notifyDataSetChanged();
    }
    /* заполняем шапку нового заказа */
    private void onfillOrder(int position, String item, final String subItem) {

        switch (position) {
            case 0:
                ConstantsUtil.mCurrentOrder.setFirmId(item);
                break;
            case 1:
                ConstantsUtil.mCurrentOrder.setStoreId(item);
                break;
            case 2:
                ConstantsUtil.mCurrentOrder.setClientId(item);
                ConstantsUtil.mCurrentOrder.setAdress(subItem);
                break;
            case 3:
                ConstantsUtil.mCurrentOrder.setPriceCategoryId(item);
                break;
            case 4:
                ConstantsUtil.mCurrentOrder.setNote(item);
                break;
            default:
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        return new MyCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {

        if (data.isClosed()) {
            //err

        } else {
            /*переходим к первой строке*/
            if (data.moveToFirst()) {
                fillHeader(data);
            } else {
                InfoUtil.Tost("Нет данных по номеру заказа " + id_order, getActivity());
            }

        }
    }

    private void fillHeader(Cursor data) {
     /*имя*/
        String cNameCompanies = data.getString(data.getColumnIndex("name_comp"));
        String cNameStores = data.getString(data.getColumnIndex("name_type_stores"));
        String cNameCounteragents = data.getString(data.getColumnIndex("name_contr"));
        String cNamePrices = data.getString(data.getColumnIndex("name_type_price"));
        String cAgent = data.getString(data.getColumnIndex("agent_id"));
            /*код*/
        String cKodCompanies = data.getString(data.getColumnIndex("kod_comp"));
        String cKodStores = data.getString(data.getColumnIndex("kod_type_stores"));
        String KodCounteragents = data.getString(data.getColumnIndex("kod_contr"));
        String cKodPrices = data.getString(data.getColumnIndex("kod_type_price"));
            /*адресс контрагента*/
        String cCounteragentsAddress = data.getString(data.getColumnIndex("address_contr"));
        String cComent = data.getString(data.getColumnIndex("note"));
            /*создаем массив шапку*/
            /* создаем массив для передачи в шапку заказа*/
            /*фирма*/
        String[] cData = new String[2];
        cData[0] = cNameCompanies;
        cData[1] = cKodCompanies;
        mItemsHeader[0] = cData;
             /*склад*/
        cData = new String[2];
        cData[0] = cNameStores;
        cData[1] = cKodStores;
        mItemsHeader[1] = cData;
             /*контрагент*/
        cData = new String[3];
        cData[0] = cNameCounteragents;
        cData[1] = KodCounteragents;
        cData[2] = cCounteragentsAddress;
        mItemsHeader[2] = cData;
             /*прайс*/
        cData = new String[2];
        cData[0] = cNamePrices;
        cData[1] = cKodPrices;
        mItemsHeader[3] = cData;
         /*комент*/
        cData = new String[2];
        cData[0] = cComent;
        cData[1] = cComent;
        mItemsHeader[4] = cData;
        /*заполняем док заказ*/
        ConstantsUtil.mCurrentOrder.setFirmId(cKodCompanies);
        ConstantsUtil.mCurrentOrder.setStoreId(cKodStores);
        ConstantsUtil.mCurrentOrder.setClientId(KodCounteragents);
        ConstantsUtil.mCurrentOrder.setPriceCategoryId(cNamePrices);
        ConstantsUtil.mCurrentOrder.setAgentId(cAgent);

        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {

    }

    @Override
    public void onResume() {
        super.onResume();
        /* открываем подключение к БД */
        if (sDb == null) {
            sDb = SQLiteOpenHelperUtil.getInstance().getDatabase();
        }
    }

    /* создаем класс - интефейс для открытия фрагментов */
    public interface onEventListener {

        void onOpenFragmentClass(Class<?> fClass);
        void onOpenFragmentClassBundle(Class<?> fClass, Bundle bundleItem);
    }

    /* создаем класс для загрузки данных из БД
        * загрузка происходит в фоне */
    private static class MyCursorLoader extends CursorLoader {

        public MyCursorLoader(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {
            return sDb
                    .rawQuery(SQLQuery.queryOrdersHeader("Orders.view_id = ?"), new String[]{id_order});
        }
    }

    private class MySimpleAdapter extends SimpleAdapter {

        private String[] mHeaderOrdersNameTable = getResources().getStringArray(R.array.header_orders_table);
        private LayoutInflater mInflater;
        private ArrayList mHeaderOrders;

        public MySimpleAdapter(final Context context, final List<? extends Map<String, ?>> data, final int resource, final String[] from, final int[] to) {
            super(context, data, resource, from, to);
            mInflater = LayoutInflater.from(context);
            mHeaderOrders = (ArrayList) data;
        }

        @Override
        public Object getItem(final int position) {
            return super.getItem(position);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            /*получаем заголовки шапки*/
            final Map<String, ?> items = (Map<String, ?>) mHeaderOrders.get(position);
            convertView = mInflater.inflate(R.layout.order_new_header_list_item, parent, false);

            /* заголовок*/
            final TextView header = (TextView) convertView.findViewById(R.id.order_header_list_item_text);
            /* позиция шапки */
            String itemP[] = mItemsHeader[position];
            /*если параметр шапки не заполнен тогда устанвливаем заголовок*/
            if (itemP[0] == null) {
                header.setHint(items.get(getString(R.string.title)).toString());
            } else {

                header.setText(itemP[0]);
                /**/
                String sub_text = null;
                /*заполняем шапку заказа*/
                if (position == 2) {
                    sub_text = itemP[2];

                    TextView sub_header = (TextView) convertView.findViewById(R.id.order_header_list_item_sub_text);
                    sub_header.setVisibility(View.VISIBLE);
                    sub_header.setText(sub_text);
                }

                onfillOrder(position, itemP[1], sub_text);
                /* суб заголовок*/
            }

            /*клик на любом месте поля вызываем список занчений*/
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    mPosition = position;
                    /* если это не поле коментарий
                    * если комент вызываем диалог*/
                    if (mPosition != 4) {
                        Bundle bundleItem = new Bundle();
                        bundleItem.putString(NAME_TABLE, mHeaderOrdersNameTable[position]);
                        /*открываем окно для выбора значения*/
                        final onEventListener someEventListener = (onEventListener) getActivity();
                        someEventListener.onOpenFragmentClassBundle(OrderNewSelectHeaderFragment.class, bundleItem);
                    } else {
                        /*вызывваем диалог для ввода комента*/
                        Dialogs.showCustomAlertDialogEditComment(getActivity(), getString(R.string.enter_your_coment));
                    }
                }
            });
            /*устанвливаем аватар для каждого параметра шапки*/
            ImageView imageView = (ImageView) convertView.findViewById(R.id.order_header_list_item_image_avatar);
            imageView.setImageResource((Integer) items.get(getString(R.string.imageAvatar)));

            return convertView;
        }
    }
}
