package ua.com.it_st.ordersmanagers.fragmets;

import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by Gena on 2017-05-04.
 */
public class DocListFragment extends Fragment {

    private static SQLiteDatabase sDb;
    private SimpleCursorAdapter scAdapter;
    private TextView summaDoc;
    private TextView periodDoc;

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        /* макет фрагмента */
//        View rootView = inflater.inflate(R.layout.main_header_list, container,
//                false);
//        /* открываем подключение к БД */
//        sDb = SQLiteOpenHelperUtil.getInstance().getDatabase();
//        /* формируем столбцы сопоставления */
//        String[] from = new String[]{};
//        int[] to = new int[]{};
//        /* создааем адаптер и настраиваем список */
//        scAdapter = new LoaderDocCursorAdapter(getActivity(), R.layout.main_list_item, null, from, to, 0);
//        /* сам список */
//        ListView lvData = (ListView) rootView.findViewById(R.id.main_heander_list_position);
//        lvData.setAdapter(scAdapter);
//        /* создаем лоадер для чтения данных */
//        getActivity().getSupportLoaderManager().initLoader(0, null, this);
//        getActivity().getSupportLoaderManager().initLoader(2, null, this);
//        /* кнопка далее переход на следующий этап*/
//        ImageView imViewAdd = (ImageView) rootView.findViewById(R.id.main_heander_image_plus);
//        /* слушатель кнопки далее */
//        imViewAdd.setOnClickListener(this);
//        /*устанавливаем период журнала*/
//        periodDoc = (TextView) rootView.findViewById(R.id.main_heander_period);
//        periodDoc.setText(getString(R.string.with) + ConstantsUtil.getDate() + getString(R.string.on) + ConstantsUtil.getDate());
//        /*подвал журнал заказов */
//        summaDoc = (TextView) rootView.findViewById(R.id.main_header_list_velue_text);
//        /**/
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_drawer);
//        MainActivity.chickMainFragment = true;
//        return rootView;
//    }
//    /* делаем свой адаптер т.к нужно обрабытывать
//    клик на контекстное меню*/
//    private class LoaderDocCursorAdapter extends SimpleCursorAdapter {
//
//        private LayoutInflater mLInflater;
//        private String mPeriod;
//
//        public LoaderDocCursorAdapter(final Context context, final int layout, final Cursor c, final String[] from, final int[] to, final int flags) {
//            super(context, layout, c, from, to, flags);
//            mLInflater = (LayoutInflater) mContext
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        }
//
//        @Override
//        public View getView(final int position, View convertView, final ViewGroup parent) {
//
//            convertView = mLInflater.inflate(R.layout.main_list_item, parent, false);
//            /*позиция*/
//            Cursor itemCursor = (Cursor) getItem(position);
//            /*получаем колонки*/
//            final String cId = itemCursor.getString(itemCursor.getColumnIndex(TableOrders.COLUMN_VIEW_ID));
//            final String cDate = itemCursor.getString(itemCursor.getColumnIndex(TableOrders.COLUMN_DATE));
//            final String cClient = itemCursor.getString(itemCursor.getColumnIndex(TableCounteragents.COLUMN_NAME));
//            final String cAdress = itemCursor.getString(itemCursor.getColumnIndex(TableCounteragents.COLUMN_ADDRESS));
//            final String cTotal = itemCursor.getString(itemCursor.getColumnIndex(TableOrders.COLUMN_TOTAL));
//            final String cNumber = itemCursor.getString(itemCursor.getColumnIndex(TableOrders.COLUMN_NUMBER_DOC));
//            final String cStatus = itemCursor.getString(itemCursor.getColumnIndex(TableOrders.COLUMN_TYPE));
//
//            /*menu*/
//            final Spinner spinner = (Spinner) convertView.findViewById(R.id.main_list_item_image_menu);
//
//            /*дата */
//            final TextView date = (TextView) convertView.findViewById(R.id.main_list_item_text_date);
//            date.setText(cDate);
//            /*клиент*/
//            final TextView client = (TextView) convertView.findViewById(R.id.main_list_item_text_client);
//            client.setText(cClient);
//
//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(final View view) {
//                    spinner.performClick();
//                }
//            });
//            /*адресс*/
//            final TextView adress = (TextView) convertView.findViewById(R.id.main_list_item_text_sub_client);
//            adress.setText(cAdress);
//            /*сумма*/
//            final TextView total = (TextView) convertView.findViewById(R.id.main_list_item_sum);
//            total.setText(cTotal);
//             /*номер*/
//            final TextView number = (TextView) convertView.findViewById(R.id.main_list_item_text_number);
//            number.setText(cNumber);
//            /*статус*/
//            final ImageView status = (ImageView) convertView.findViewById(R.id.main_list_item_image_status);
//            /* проведен */
//            if (cStatus.equals(DocType.HELD.toString())) {
//                status.setImageResource(R.mipmap.ic_held);
//            } else {
//                status.setImageResource(R.mipmap.ic_no_held);/* не проведен */
//            }
//
//            /*устанвливаем период */
//            if (mPeriod == null) {
//                periodDoc.setText(getString(R.string.with) + cDate + getString(R.string.on) + ConstantsUtil.getDate());
//                mPeriod = cDate;
//            }
//
//            /* Настраиваем адаптер */
//            String[] spinnerMenu = getResources().getStringArray(R.array.spinner_orders_menu);
//            /**/
//            final SpinnerMenuAdapter adapter = new SpinnerMenuAdapter(getActivity(), R.layout.spinner_row, spinnerMenu);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//            /* Вызываем адаптер */
//            spinner.setAdapter(adapter);
//            spinner.setSelection(4);/*устанавливаем заглушку*/
//
//            /*костыль для того чтоб при начале открытия списка незаполнялось первая позиция
//             и при выборе пользователем позицию в меню нормально обрабатывал
//            */
//            final int[] iCurrentSelection = {spinner.getSelectedItemPosition()};
//
//            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                public void onItemSelected(AdapterView<?> parent,
//                                           View itemSelected, int selectedItemPosition, long selectedId) {
//
//                    if (iCurrentSelection[0] != selectedItemPosition) {
//
//                        final ContentValues data = new ContentValues();
//
//                        switch (selectedItemPosition) {
//                            case 0:
//                            case 3:
//
//                                Bundle bundleItem = new Bundle();
//                                switch (selectedItemPosition) {
//                                    case 0:
//                                  /*чистим док заказ и редактируем заказ*/
//                                        DocActionOrder.clearOrderHeader(DocTypeOperation.EDIT);
//                                        bundleItem.putString(DOC_TYPE_OPERATION, getString(R.string.edit_doc));
//                                        break;
//                                    case 3:
//                                  /*чистим док заказ и копируем заказ*/
//                                        DocActionOrder.clearOrderHeader(DocTypeOperation.COPY);
//                                        bundleItem.putString(DOC_TYPE_OPERATION, getString(R.string.copy_doc));
//                                        break;
//                                }
//                                        /* ТЧ заказа */
//                                DocCartOrderAction.mCart = new LinkedHashSet<>();
//                                /*редактируем док*/
//
//                                bundleItem.putString(ID_ORDER, cId);
//                                bundleItem.putString(NUMBER_ORDER, cNumber);
//                                bundleItem.putString(DATE_ORDER, cDate);
//
//                                final OrderListDocFragment.onLoaderDocListener someEventListener = (OrderListDocFragment.onLoaderDocListener) getActivity();
//                                someEventListener.onOpenFragmentClassBundle(OrderHeaderDoc.class, bundleItem);
//                            case 1:
//                                /*проводим док*/
//                                data.put(TableOrders.COLUMN_TYPE, DocType.HELD.toString());
//                                break;
//                            case 2:
//                                /*помечаем на удаления док*/
//                                data.put(TableOrders.COLUMN_TYPE, DocType.NO_HELD.toString());
//                                break;
//                            default:
//                                break;
//                        }
//                        /*меняем статус у документов (проведен, не проведен)*/
//                        if (!cId.equals("") & selectedItemPosition == 1 || selectedItemPosition == 2) {
//
//                            sDb.update(TableOrders.TABLE_NAME, data, "view_id = ?", new String[]{cId});
//                            String[] choose = getResources().getStringArray(R.array.spinner_orders_menu);
//
//                            Toast toast = Toast.makeText(getActivity(),
//                                    getString(R.string.operation_completed) + choose[selectedItemPosition], Toast.LENGTH_SHORT);
//                            toast.show();
//
//                            getActivity().getSupportLoaderManager().getLoader(0).forceLoad();
//                        }
//                    } else {
//                        iCurrentSelection[0] = selectedItemPosition;
//                    }
//                }
//
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
//            return convertView;
//        }
//    }
}
