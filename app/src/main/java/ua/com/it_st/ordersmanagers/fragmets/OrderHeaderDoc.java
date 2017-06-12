package ua.com.it_st.ordersmanagers.fragmets;

/*Класс предназначен для отображения и заполнения шапки документа заказа
* Все поля кроме коммнтария являются обязательными для заполнения*/

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.UUID;

import ua.com.it_st.ordersmanagers.Adapters.HeaderDocAdapter;
import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.enums.DocTypeOperation;
import ua.com.it_st.ordersmanagers.models.Agents;
import ua.com.it_st.ordersmanagers.models.Companies;
import ua.com.it_st.ordersmanagers.models.Counteragents;
import ua.com.it_st.ordersmanagers.models.Orders;
import ua.com.it_st.ordersmanagers.models.Stores;
import ua.com.it_st.ordersmanagers.models.TypePrices;
import ua.com.it_st.ordersmanagers.utils.ConstantsUtil;
import ua.com.it_st.ordersmanagers.utils.GlobalCursorLoader;
import ua.com.it_st.ordersmanagers.utils.InfoUtil;
import ua.com.it_st.ordersmanagers.utils.SQLQuery;

public class OrderHeaderDoc extends HeaderDoc {

    private Orders orders;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            orders = new Orders();

            ((MainActivity) getActivity()).setmCurrentOrder(orders);

            setListDataHeader(orders.getListDataHeader());
            setDocuments(orders);

        /* создаем адаптер */
            setmAdapter(new HeaderDocAdapter(getActivity(), getListDataHeader(),
                    R.layout.order_new_header_list_item,
                    new String[]{"title", "imageAvatar"},
                    new int[]{R.id.order_header_list_item_text, R.id.order_header_list_item_image_avatar}, this));

            super.onCreateView(inflater, container, savedInstanceState);

            orders.setTypeOperation(getDocTypeOperation());

        }
        return rootView;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {

            case R.id.order_new_header_list_image_arrow_right:
                /* проверка шапки*/
                if (!onRecord()) {
                    final OrderHeaderDoc.onEventListener someEventListener = (OrderHeaderDoc.onEventListener) getActivity();
                    someEventListener.onOpenFragmentClass(OrderCatalogGoodsFragment.class);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {

        return new GlobalCursorLoader(getActivity(), getQuery(), getParamsQuery(), sDb);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {

            /*переходим к первой строке*/
        if (data.moveToFirst()) {
            fillHeaderFromCursor(data);
        } else {
            InfoUtil.Tost(getString(R.string.no_data_on_number_order) + id_order, getActivity());
        }
    }

    @Override
    public void setHeaderSelection(int position, Object item) {

        switch (position) {
            case 0:
                orders.setCompany((Companies) item);
                break;
            case 1:
                orders.setStore((Stores) item);
                break;
            case 2:
                orders.setCounteragent((Counteragents) item);
                break;
            case 3:
                orders.setTypePrices((TypePrices) item);
                break;
            case 4:
                orders.setNote(item.toString());
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
        orders.setCompany(new Companies(cKodCompanies, cNameCompanies));
        orders.setStore(new Stores(cKodStores, cNameStores));
        orders.setCounteragent(new Counteragents(KodCounteragents, cNameCounteragents, cCounteragentsAddress));
        orders.setTypePrices(new TypePrices(cKodPrices, cNamePrices));
        orders.setNote(cComent == null ? "" : cComent);
        orders.getAgent().setKod(cAgent);

        setItemHeader(orders.getCompany(), "0");
        setItemHeader(orders.getStore(), "1");
        setItemHeader(orders.getCounteragent(), "2");
        setItemHeader(orders.getTypePrices(), "3");
        setItemHeader(orders.getNote(), "4");

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
                orders.setId(String.valueOf(uniqueKey));
                /*номер документа*/
                numberDoc = bundle.getString(PayDocSelectOrders.LoaderDocFragment.NUMBER_DOC);
                /*нтекущая дата*/
                dateDoc = ConstantsUtil.getDate();
                /*устанавливаем мод. корзины*/
                orders.setClickModifitsirovannoiCart(false);
                break;

            case EDIT:
                setCountLoad((byte) 1);
                /*получаем ID дока и подставляем в запрос*/
                id_order = bundle.getString(OrderListDocFragment.ID_ORDER);

                setQuery(SQLQuery.queryOrdersHeader("Orders.view_id = ?"));
                setParamsQuery(new String[]{id_order});

                orders.setId(id_order);
               /*номер документа*/
                numberDoc = bundle.getString(PayDocSelectOrders.LoaderDocFragment.NUMBER_DOC);
                /*дата док*/
                dateDoc = bundle.getString(PayDocSelectOrders.LoaderDocFragment.DATE_DOC);
                break;

            case COPY:
                setCountLoad((byte) 1);
                id_order = bundle.getString(OrderListDocFragment.ID_ORDER);

                setQuery(SQLQuery.queryOrdersHeader("Orders.view_id = ?"));
                setParamsQuery(new String[]{id_order});

                /*номер документа*/
                numberDoc = bundle.getString(PayDocSelectOrders.LoaderDocFragment.NUMBER_DOC);
                 /*нтекущая дата*/
                dateDoc = ConstantsUtil.getDate();
                /*создаем новый заказ*/
                /* сгениророваный номер документа заказа ИД для 1с */
                uniqueKey = UUID.randomUUID();
                orders.setId(String.valueOf(uniqueKey));
                break;
        }

        orders.setAgent(new Agents(kodAgent, kodAgent));
        orders.setDocDate(dateDoc);
        orders.setDocNumber(numberDoc);
    }

    @Override
    public boolean onRecord() {

        boolean bCheck = false;
         /* проверяем на обязательные поля шапки документа*/
        if (orders.getDocNumber() == null
                || orders.getDocDate() == null
                || orders.getAgent().getKod() == null
                || orders.getCompany().getKod() == null
                || orders.getTypePrices().getKod() == null
                || orders.getCounteragent().getKod() == null) {

            bCheck = true;
            InfoUtil.Tost(getActivity().getString(R.string.not_all_cap_mandatory_filled), getActivity());
        }
        return bCheck;
    }


}
