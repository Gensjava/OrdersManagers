package ua.com.it_st.ordersmanagers.fragmets;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
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
import ua.com.it_st.ordersmanagers.models.Documents;

/**
 * Created by Gena on 2017-05-14.
 */

public abstract class HeaderDoc extends CursorLoaderFragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String NAME_TABLE = "NAME_TABLE";
    public static final String NAME_TAG = "NAME_TAG";
    public static final String NAME_CLASS = "NAME_CLASS";
    public static final String ID_POSITION = "ID_POSITION";

    public static String id_order;
    protected View rootView;
    protected SimpleAdapter mAdapter;
    protected String numberDoc;
    protected String dateDoc;
    protected TextView period;
    protected DocTypeOperation docTypeOperation;
    protected UUID uniqueKey;
    protected String kodAgent;
    private List<Map<String, ?>> listDataHeader;
    private Documents documents;


    public abstract void setHeaderSelection(int position, Object item);
    public abstract void fillHeaderFromCursor(Cursor data);
    public abstract void onCreateHeader(Bundle bundle);

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

            /**//*вызываем менеджера настроек*/
            SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());
                      /*код на сервере пользователя*/
            kodAgent = mSettings.getString(getActivity().getString(R.string.id_user), null);

            Bundle bundle = getArguments();

            /*редактируем документ*/
            onCreateHeader(bundle);

            /*стктус докуента*/
            header.setText(docTypeOperation.toString());

            /*выводим данные дату и номер в шапку*/
            period = (TextView) rootView.findViewById(R.id.order_new_heander_period);
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
    }

    public void setItemHeader(final Object item, String id) {
        List<Map<String, ?>> listDataHeader = getListDataHeader();
        final Map<String, Object> itemsCatalogs = (Map<String, Object>) listDataHeader.get(Integer.valueOf(id));
        itemsCatalogs.put(id, item);
    }

    /*
    записываем и обновляем выбранные данные
    заполняем шапку
    */
    public void setSelectUpdate(final Object item, String id) {

        setItemHeader(item, id);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
    }

    public DocTypeOperation getDocTypeOperation() {
        return docTypeOperation;
    }

    public List<Map<String, ?>> getListDataHeader() {
        return listDataHeader;
    }

    public void setListDataHeader(List<Map<String, ?>> listDataHeader) {
        this.listDataHeader = listDataHeader;
    }

    public void setmAdapter(SimpleAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    public Documents getDocuments() {
        return documents;
    }

    public void setDocuments(Documents documents) {
        this.documents = documents;
    }

    /* создаем класс - интефейс для открытия фрагментов */
    public interface onEventListener {
        void onOpenFragmentClass(Class<?> fClass);
        void onOpenFragmentClassBundle(Class<?> fClass, Bundle bundleItem);
    }
}
