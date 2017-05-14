package ua.com.it_st.ordersmanagers.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedHashSet;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.enums.DocType;
import ua.com.it_st.ordersmanagers.enums.DocTypeOperation;
import ua.com.it_st.ordersmanagers.fragmets.OrderListFragment;
import ua.com.it_st.ordersmanagers.fragmets.OrderNewHeaderFragment;
import ua.com.it_st.ordersmanagers.interfaces.implems.UpDateOrderList;
import ua.com.it_st.ordersmanagers.interfaces.implems.UpdateOrderDB;
import ua.com.it_st.ordersmanagers.sqlTables.TableCounteragents;
import ua.com.it_st.ordersmanagers.sqlTables.TableOrders;
import ua.com.it_st.ordersmanagers.utils.ConstantsUtil;
import ua.com.it_st.ordersmanagers.utils.LoaderFragment;

/**
 * Created by Gena on 2017-05-14.
 */

public class MySimpleCursorAdapter extends SimpleCursorAdapter {
    private LayoutInflater mLInflater;
    private LoaderFragment loaderFragment;

    public MySimpleCursorAdapter(final Context context, final int layout, final Cursor c, final String[] from, final int[] to, final int flags, LoaderFragment loaderFragment) {
        super(context, layout, c, from, to, flags);
        mLInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.loaderFragment = loaderFragment;

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

            /*устанвливаем период */

        loaderFragment.setPeriodDoc(mContext.getString(R.string.with) + cDate + mContext.getString(R.string.on) + ConstantsUtil.getDate());

            /* Настраиваем адаптер */
        String[] spinnerMenu = mContext.getResources().getStringArray(R.array.spinner_orders_menu);
            /**/
        final MenuCustomAdapter adapter = new MenuCustomAdapter(mContext, R.layout.spinner_row, spinnerMenu);
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
                                    UpdateOrderDB.clearOrderHeader(DocTypeOperation.EDIT);
                                    bundleItem.putString(LoaderFragment.DOC_TYPE_OPERATION, loaderFragment.getString(R.string.edit_order));
                                    break;
                                case 3:
                                  /*чистим док заказ и копируем заказ*/
                                    UpdateOrderDB.clearOrderHeader(DocTypeOperation.COPY);
                                    bundleItem.putString(LoaderFragment.DOC_TYPE_OPERATION, loaderFragment.getString(R.string.copy_order));
                                    break;
                            }
                                        /* ТЧ заказа */
                            UpDateOrderList.mCart = new LinkedHashSet<>();
                                /*редактируем док*/

                            bundleItem.putString(LoaderFragment.ID_ORDER, cId);
                            bundleItem.putString(LoaderFragment.NUMBER_ORDER, cNumber);
                            bundleItem.putString(LoaderFragment.DATE_ORDER, cDate);

                            final OrderListFragment.onEventListener someEventListener = (OrderListFragment.onEventListener) mContext;
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

                        LoaderFragment.sDb.update(TableOrders.TABLE_NAME, data, "view_id = ?", new String[]{cId});
                        String[] choose = loaderFragment.getResources().getStringArray(R.array.spinner_orders_menu);

                        Toast toast = Toast.makeText(mContext,
                                R.string.operation_completed + choose[selectedItemPosition], Toast.LENGTH_SHORT);
                        toast.show();

                        loaderFragment.getActivity().getSupportLoaderManager().getLoader(0).forceLoad();
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

