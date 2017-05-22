package ua.com.it_st.ordersmanagers.fragmets;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.com.it_st.ordersmanagers.Adapters.HeaderDocAdapter;
import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.models.Pays;
import ua.com.it_st.ordersmanagers.utils.InfoUtil;

/**
 * Created by Gena on 2017-05-14.
 */

public class HeaderPayDoc extends HeaderDoc {

    private Pays CurrentNewDog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            CurrentNewDog = new Pays();
            ((MainActivity) getActivity()).setmCurrentNewDog(new Pays());
        /* создаем адаптер */
            setmAdapter(new HeaderDocAdapter(getActivity(), CurrentNewDog.fillListHeaders(),
                    R.layout.order_new_header_list_item,
                    new String[]{"title", "imageAvatar"},
                    new int[]{R.id.order_header_list_item_text, R.id.order_header_list_item_image_avatar}, this));

            super.onCreateView(inflater, container, savedInstanceState);
        }
        CurrentNewDog.setTypeOperation(getDocTypeOperation());
        return rootView;
    }

    @Override
    public boolean checkHeader(Context context) {
        boolean bCheck = false;

        if (CurrentNewDog.getDocNumber() == null
                || CurrentNewDog.getDocDate() == null
                || CurrentNewDog.getAgentId() == null
                || CurrentNewDog.getFirmId() == null) {

            bCheck = true;
            InfoUtil.Tost(context.getString(R.string.not_all_cap_mandatory_filled), context);
        }
        return bCheck;
    }

    @Override
    public void setHeaderSelection(int position, String item, String subItem) {

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
        CurrentNewDog.setFirmId(cKodCompanies);
        CurrentNewDog.setAgentId(cAgent);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateHeader(Bundle bundle) {

    }


}
