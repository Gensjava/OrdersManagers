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

import java.util.List;
import java.util.Map;
import java.util.UUID;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.enums.DocTypeOperation;
import ua.com.it_st.ordersmanagers.interfaces.implems.UpdateDocDB;
import ua.com.it_st.ordersmanagers.models.Documents;
import ua.com.it_st.ordersmanagers.utils.ConstantsUtil;
import ua.com.it_st.ordersmanagers.utils.InfoUtil;
import ua.com.it_st.ordersmanagers.utils.LoaderDocFragment;
import ua.com.it_st.ordersmanagers.utils.SQLQuery;
import ua.com.it_st.ordersmanagers.utils.SQLiteOpenHelperUtil;

/**
 * Created by Gena on 2017-05-14.
 */

public abstract class HeaderDoc extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String NAME_TABLE = "NAME_TABLE";
    public static final String NAME_TAG = "NAME_TAG";
    public static final String NAME_CLASS = "NAME_CLASS";
    public static final String ID_POSITION = "ID_POSITION";

    public static String id_order;
    public static Documents CurrentNewDog;
    private static SQLiteDatabase sDb;
    protected View rootView;
    private SimpleAdapter mAdapter;
    private String numberDoc;
    private String dateDoc;
    private DocTypeOperation docTypeOperation;
    private UUID uniqueKey;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {

            rootView = inflater.inflate(R.layout.order_new_header_list, container,
                    false);

            /*расчет  позиции кнопки далее к следующему этапу*/
            TextView header = (TextView) rootView.findViewById(R.id.order_new_header_list_header_root);
             /*кнопка далее к следующему этапу*/
            ImageView imViewAdd = (ImageView) rootView.findViewById(R.id.order_new_header_list_image_arrow_right);
            imViewAdd.setOnClickListener(this);

            Bundle bundle = getArguments();
            /*редактируем документ*/
            setHeader(bundle, header);

            /*выводим данные дату и номер в шапку*/
            TextView period = (TextView) rootView.findViewById(R.id.order_new_heander_period);
            period.setText(getString(R.string.rNumber) + numberDoc + " " + getString(R.string.rOf) + " " + dateDoc);

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
        if (docTypeOperation == DocTypeOperation.EDIT
                || docTypeOperation == DocTypeOperation.COPY) {
           /* выходим из загрузчкика*/
            getActivity().getSupportLoaderManager().destroyLoader(0);
        }
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {

            case R.id.order_new_header_list_image_arrow_right:
                /* проверка шапки*/
                if (!UpdateDocDB.checkHeaderOrder(getActivity())) {
                    final HeaderOrderDoc.onEventListener someEventListener = (HeaderOrderDoc.onEventListener) getActivity();
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
    public void setSelectUpdate(final Object item, String id) {

        List<Map<String, ?>> listDataHeader = UpdateDocDB.mCurrentOrder.getListDataHeader();
        final Map<String, Object> itemsCatalogs = (Map<String, Object>) listDataHeader.get(Integer.valueOf(id));
        itemsCatalogs.put(id, item);

        mAdapter.notifyDataSetChanged();
    }

    /* заполняем шапку нового заказа */
    public void onfillOrder(int position, String item, final String subItem) {

        switch (position) {
            case 0:
                UpdateDocDB.mCurrentOrder.setFirmId(item);
                break;
            case 1:
                UpdateDocDB.mCurrentOrder.setStoreId(item);
                break;
            case 2:
                UpdateDocDB.mCurrentOrder.setClientId(item);
                UpdateDocDB.mCurrentOrder.setAdress(subItem);
                break;
            case 3:
                UpdateDocDB.mCurrentOrder.setPriceCategoryId(item);
                break;
            case 4:
                UpdateDocDB.mCurrentOrder.setNote(item);
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
                InfoUtil.Tost(getString(R.string.no_data_on_number_order) + id_order, getActivity());
            }
        }
    }

    private void setHeader(Bundle bundle, TextView header) {

        if (bundle == null) return;

        docTypeOperation = DocTypeOperation.valueOf(bundle.getString(OrderListDocFragment.DOC_TYPE_OPERATION));

        switch (docTypeOperation) {
            case NEW:
                 /*создаем новый заказ*/
                /* сгениророваный номер документа заказа ИД для 1с */
                uniqueKey = UUID.randomUUID();
                UpdateDocDB.mCurrentOrder.setId(String.valueOf(uniqueKey));
                /*устанавливаем дату документа и номер*/
                numberDoc = String.valueOf(UpdateDocDB.sCurrentNumber);
                UpdateDocDB.mCurrentOrder.setDocNumber(numberDoc);
                /*нтекущая дата*/
                dateDoc = ConstantsUtil.getDate();
                /**//*вызываем менеджера настроек*/
                SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());
                      /*код на сервере пользователя*/
                String kodAgent = mSettings.getString(getActivity().getString(R.string.id_user), null);
                UpdateDocDB.mCurrentOrder.setAgentId(kodAgent);
                        /*устанавливаем мод. корзины*/
                UpdateDocDB.mCurrentOrder.setClickModifitsirovannoiCart(false);
                break;

            case EDIT:
                /*получаем ID дока и подставляем в запрос*/
                id_order = bundle.getString(OrderListDocFragment.ID_ORDER);
                UpdateDocDB.mCurrentOrder.setId(id_order);
               /*номер документа*/
                numberDoc = bundle.getString(OrderListDocFragment.NUMBER_ORDER);
                /*дата док*/
                dateDoc = bundle.getString(OrderListDocFragment.DATE_ORDER);
                  /* открываем подключение к БД */
                sDb = SQLiteOpenHelperUtil.getInstance().getDatabase();
                 /* создаем лоадер для чтения данных */
                getActivity().getSupportLoaderManager().initLoader(0, null, this);
                break;

            case COPY:
                   /*получаем ID дока и подставляем в запрос*/
                id_order = bundle.getString(OrderListDocFragment.ID_ORDER);
                     /* открываем подключение к БД */
                sDb = SQLiteOpenHelperUtil.getInstance().getDatabase();
                 /* создаем лоадер для чтения данных */
                getActivity().getSupportLoaderManager().initLoader(0, null, this);
                break;
        }

        /*стктус докуента*/
        header.setText(bundle.getString(String.valueOf(LoaderDocFragment.DOC_TYPE_OPERATION)));
        UpdateDocDB.mCurrentOrder.setDocDate(dateDoc);
        UpdateDocDB.mCurrentOrder.setDocNumber(numberDoc);
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

        /*заполняем док заказ*/
        UpdateDocDB.mCurrentOrder.setFirmId(cKodCompanies);
        UpdateDocDB.mCurrentOrder.setStoreId(cKodStores);
        UpdateDocDB.mCurrentOrder.setClientId(KodCounteragents);
        UpdateDocDB.mCurrentOrder.setPriceCategoryId(cNamePrices);
        UpdateDocDB.mCurrentOrder.setAgentId(cAgent);

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

    public void setmAdapter(SimpleAdapter mAdapter) {
        this.mAdapter = mAdapter;
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

}
