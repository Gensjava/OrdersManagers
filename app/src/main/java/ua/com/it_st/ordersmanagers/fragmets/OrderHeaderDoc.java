package ua.com.it_st.ordersmanagers.fragmets;

/*Класс предназначен для отображения и заполнения шапки документа заказа
* Все поля кроме коммнтария являются обязательными для заполнения*/

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.UUID;

import ua.com.it_st.ordersmanagers.Adapters.HeaderDocAdapter;
import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.enums.DocTypeOperation;
import ua.com.it_st.ordersmanagers.interfaces.implems.DocActionOrder;
import ua.com.it_st.ordersmanagers.models.Agents;
import ua.com.it_st.ordersmanagers.models.Companies;
import ua.com.it_st.ordersmanagers.models.Counteragents;
import ua.com.it_st.ordersmanagers.models.Orders;
import ua.com.it_st.ordersmanagers.models.Stores;
import ua.com.it_st.ordersmanagers.models.TypePrices;
import ua.com.it_st.ordersmanagers.utils.ConstantsUtil;
import ua.com.it_st.ordersmanagers.utils.InfoUtil;
import ua.com.it_st.ordersmanagers.utils.SQLiteOpenHelperUtil;

public class OrderHeaderDoc extends HeaderDoc {

    private Orders CurrentNewDog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            CurrentNewDog = new Orders();

            ((MainActivity) getActivity()).setmCurrentNewDog(CurrentNewDog);
            setListDataHeader(CurrentNewDog.getListDataHeader());

        /* создаем адаптер */
            setmAdapter(new HeaderDocAdapter(getActivity(), getListDataHeader(),
                    R.layout.order_new_header_list_item,
                    new String[]{"title", "imageAvatar"},
                    new int[]{R.id.order_header_list_item_text, R.id.order_header_list_item_image_avatar}, this));

            super.onCreateView(inflater, container, savedInstanceState);

            CurrentNewDog.setTypeOperation(getDocTypeOperation());
        }
        return rootView;
    }

    /* проверяем на обязательные поля шапки документа*/
    public boolean checkHeader(Context context) {

        boolean bCheck = false;

        if (CurrentNewDog.getDocNumber() == null
                || CurrentNewDog.getDocDate() == null
                || CurrentNewDog.getAgent().getKod() == null
                || CurrentNewDog.getCompany().getKod() == null
                || CurrentNewDog.getTypePrices().getKod() == null
                || CurrentNewDog.getCounteragent().getKod() == null) {

            bCheck = true;
            InfoUtil.Tost(context.getString(R.string.not_all_cap_mandatory_filled), context);
        }
        return bCheck;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {

            case R.id.order_new_header_list_image_arrow_right:
                /* проверка шапки*/
                if (!checkHeader(getActivity())) {
                    final OrderHeaderDoc.onEventListener someEventListener = (OrderHeaderDoc.onEventListener) getActivity();
                    someEventListener.onOpenFragmentClass(OrderCatalogGoodsFragment.class);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void setHeaderSelection(int position, Object item) {

        switch (position) {
            case 0:
                CurrentNewDog.setCompany((Companies) item);
                break;
            case 1:
                CurrentNewDog.setStore((Stores) item);
                break;
            case 2:
                CurrentNewDog.setCounteragent((Counteragents) item);
                break;
            case 3:
                CurrentNewDog.setTypePrices((TypePrices) item);
                break;
            case 4:
                CurrentNewDog.setNote(item.toString());
                break;
            default:
                break;
        }
    }

    @Override
    public void fillHeaderFromCursor(Cursor data) {
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
        CurrentNewDog.setCompany(new Companies(cKodCompanies, cNameCompanies));
        CurrentNewDog.setStore(new Stores(cKodStores, cNameStores));
        CurrentNewDog.setCounteragent(new Counteragents(KodCounteragents, cNameCounteragents, cCounteragentsAddress));
        CurrentNewDog.setTypePrices(new TypePrices(cKodPrices, cNamePrices));
        CurrentNewDog.setNote(cComent == null ? "" : cComent);
        CurrentNewDog.getAgent().setKod(cAgent);

        setItemHeader(CurrentNewDog.getCompany(), "0");
        setItemHeader(CurrentNewDog.getStore(), "1");
        setItemHeader(CurrentNewDog.getCounteragent(), "2");
        setItemHeader(CurrentNewDog.getTypePrices(), "3");
        setItemHeader(CurrentNewDog.getNote(), "4");

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateHeader(Bundle bundle) {

        if (bundle == null) return;

        docTypeOperation = DocTypeOperation.valueOf(bundle.getString(OrderListDocFragment.DOC_TYPE_OPERATION));

        switch (docTypeOperation) {
            case NEW:
                 /*создаем новый заказ*/
                /* сгениророваный номер документа заказа ИД для 1с */
                uniqueKey = UUID.randomUUID();
                CurrentNewDog.setId(String.valueOf(uniqueKey));
                /*устанавливаем дату документа и номер*/
                numberDoc = String.valueOf(DocActionOrder.sCurrentNumber);
                CurrentNewDog.setDocNumber(numberDoc);
                /*нтекущая дата*/
                dateDoc = ConstantsUtil.getDate();
                /*устанавливаем мод. корзины*/
                CurrentNewDog.setClickModifitsirovannoiCart(false);
                break;

            case EDIT:
                /*получаем ID дока и подставляем в запрос*/
                id_order = bundle.getString(OrderListDocFragment.ID_ORDER);
                CurrentNewDog.setId(id_order);
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

        CurrentNewDog.setAgent(new Agents(kodAgent, kodAgent));
        CurrentNewDog.setDocDate(dateDoc);
        CurrentNewDog.setDocNumber(numberDoc);
    }


}
