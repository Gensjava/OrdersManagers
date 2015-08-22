package ua.com.it_st.ordersmanagers.fragmets;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import ua.com.it_st.ordersmanagers.utils.ConstantsUtil;
import ua.com.it_st.ordersmanagers.utils.Dialogs;

/*Класс предназначен для отображения и заполнения шапки документа заказа
* Все поля кроме коммнтария являются обязательными для заполнения*/

public class OrderNewHeaderFragment extends Fragment implements View.OnClickListener {

    public static final String NAME_TABLE = "NAME_TABLE";
    ListView lv;
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
            /*чистим док заказ*/
            ConstantsUtil.clearOrderHeader();
            /* сгениророваный номер документа заказа ИД для 1с */
            UUID uniqueKey = UUID.randomUUID();
            ConstantsUtil.mCurrentOrder.setId(String.valueOf(uniqueKey));
            /*устанавливаем дату документа и номер*/
            ConstantsUtil.mCurrentOrder.setDocDate(ConstantsUtil.getDate());
            ConstantsUtil.mCurrentOrder.setDocNumber(String.valueOf(ConstantsUtil.sCurrentNumber));
            /*выводим данные дату и номер в шапку*/
            TextView period = (TextView) rootView.findViewById(R.id.order_new_heander_period);
            period.setText(getString(R.string.rNumber) + ConstantsUtil.getsCurrentNumber() + " " + getString(R.string.rOf) + " " + ConstantsUtil.getDate());

            /*массив принимает выбранные занчения шапки и передает их в адаптер*/
            mItemsHeader = new String[5][3];

            /* создаем адаптер */
            mAdapter = new MySimpleAdapter(getActivity(), createList(),
                    R.layout.order_new_header_list_item,
                    new String[]{"title", "imageAvatar"},
                    new int[]{R.id.order_header_list_item_text, R.id.order_header_list_item_image_avatar});
            /* список шапка заказа*/
            lv = (ListView) rootView.findViewById(R.id.order_new_header_list_position);
            lv.setAdapter(mAdapter);

            /*кнопка далее к следующему этапу*/
            ImageView imViewAdd = (ImageView) rootView.findViewById(R.id.order_new_header_list_image_arrow_right);
            imViewAdd.setOnClickListener(this);

        }

        return rootView;
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

            Map<String, Object> map = new HashMap<String, Object>();
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

    public interface onEventListener {

        void onOpenFragmentClass(Class<?> fClass);
        void onOpenFragmentClassBundle(Class<?> fClass, Bundle bundleItem);
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
                        Dialogs dialogs = new Dialogs(getActivity());
                        dialogs.showCustomAlertDialogEditComment(getString(R.string.enter_your_coment));

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
