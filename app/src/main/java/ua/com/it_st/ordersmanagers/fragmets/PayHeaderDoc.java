package ua.com.it_st.ordersmanagers.fragmets;

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
import ua.com.it_st.ordersmanagers.models.Pays;
import ua.com.it_st.ordersmanagers.utils.ConstantsUtil;
import ua.com.it_st.ordersmanagers.utils.GlobalCursorLoader;
import ua.com.it_st.ordersmanagers.utils.InfoUtil;
import ua.com.it_st.ordersmanagers.utils.SQLQuery;

/**
 * Created by Gena on 2017-05-14.
 */

public class PayHeaderDoc extends HeaderDoc {

    private Pays pays;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {

            pays = new Pays();
            // Gen<Pays> gen = new Gen<>(pays);

            ((MainActivity) getActivity()).setmCurrentPay(pays);
            setListDataHeader(pays.getListDataHeader());

        /* создаем адаптер */
            setmAdapter(new HeaderDocAdapter(getActivity(), pays.getListDataHeader(),
                    R.layout.order_new_header_list_item,
                    new String[]{"title", "imageAvatar"},
                    new int[]{R.id.order_header_list_item_text, R.id.order_header_list_item_image_avatar}, this));

            super.onCreateView(inflater, container, savedInstanceState);
        }

        return rootView;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {

            case R.id.order_new_header_list_image_arrow_right:
                /* проверка шапки*/
                if (!onRecord()) {
                    final PayHeaderDoc.onEventListener someEventListener = (PayHeaderDoc.onEventListener) getActivity();
                    someEventListener.onOpenFragmentClass(PayDocSelectOrders.class);
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
                pays.setCompany((Companies) item);
                break;
            case 1:
                pays.setCounteragent((Counteragents) item);
                break;
            case 2:
                pays.setNote(item.toString());
                break;
            default:
                break;
        }
    }

    @Override
    public void fillHeaderFromCursor(Cursor data) {
        /*имя*/
        String cNameCompanies = data.getString(data.getColumnIndex("name_comp"));
        String cNameCounteragents = data.getString(data.getColumnIndex("name_contr"));
        String cAgent = data.getString(data.getColumnIndex("agent_id"));
            /*код*/
        String cKodCompanies = data.getString(data.getColumnIndex("kod_comp"));
        String KodCounteragents = data.getString(data.getColumnIndex("kod_contr"));
            /*адресс контрагента*/
        String cCounteragentsAddress = data.getString(data.getColumnIndex("address_contr"));
        String cComent = data.getString(data.getColumnIndex("note"));

         /*заполняем док заказ*/
        pays.setCompany(new Companies(cKodCompanies, cNameCompanies));
        pays.setCounteragent(new Counteragents(KodCounteragents, cNameCounteragents, cCounteragentsAddress));
        pays.setNote(cComent == null ? "" : cComent);
        pays.getAgent().setKod(cAgent);

        setItemHeader(pays.getCompany(), "0");
        setItemHeader(pays.getCounteragent(), "1");
        setItemHeader(pays.getNote(), "2");

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
                pays.setId(String.valueOf(uniqueKey));
                /*номер документа*/
                numberDoc = bundle.getString(PayDocSelectOrders.LoaderDocFragment.NUMBER_DOC);
                /*нтекущая дата*/
                dateDoc = ConstantsUtil.getDate();
                break;

            case EDIT:
                setCountLoad((byte) 1);
                /*получаем ID дока и подставляем в запрос*/
                id_order = bundle.getString(OrderListDocFragment.ID_ORDER);

                setQuery(SQLQuery.queryPaysHeader("Pays.view_id = ?"));
                setParamsQuery(new String[]{id_order});

                pays.setId(id_order);
               /*номер документа*/
                numberDoc = bundle.getString(PayDocSelectOrders.LoaderDocFragment.NUMBER_DOC);
                /*дата док*/
                dateDoc = bundle.getString(PayDocSelectOrders.LoaderDocFragment.DATE_DOC);
                break;

            case COPY:
                setCountLoad((byte) 1);
                id_order = bundle.getString(OrderListDocFragment.ID_ORDER);

                setQuery(SQLQuery.queryPaysHeader("Pays.view_id = ?"));
                setParamsQuery(new String[]{id_order});
                pays.setId(id_order);

                /*номер документа*/
                numberDoc = bundle.getString(PayDocSelectOrders.LoaderDocFragment.NUMBER_DOC);
                 /*нтекущая дата*/
                dateDoc = ConstantsUtil.getDate();
                break;
        }

        pays.setAgent(new Agents(kodAgent, kodAgent));
        pays.setDocDate(dateDoc);
        pays.setDocNumber(numberDoc);
        pays.setTypeOperation(getDocTypeOperation());
    }

    @Override
    public boolean onRecord() {
        boolean bCheck = false;

        if (pays.getDocNumber() == null
                || pays.getDocDate() == null
                || pays.getAgent().getKod() == null
                || pays.getCompany().getKod() == null
                || pays.getCounteragent().getKod() == null
                || pays.getCompany().getKod() == null) {

            bCheck = true;
            InfoUtil.Tost(getActivity().getString(R.string.not_all_cap_mandatory_filled), getActivity());
        }
        return bCheck;
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
}
