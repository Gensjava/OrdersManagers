package ua.com.it_st.ordersmanagers.fragmets;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.sqlTables.TableCounteragents;
import ua.com.it_st.ordersmanagers.sqlTables.TableOrders;
import ua.com.it_st.ordersmanagers.utils.SQLQuery;
import ua.com.it_st.ordersmanagers.utils.SQLiteOpenHelperUtil;

/**
 * Created by Gens on 30.07.2015.
 */
public class OrderListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    public static final int IDM_A = 101;
    public static final int IDM_B = 102;
    private static SQLiteDatabase DB;
    private SimpleCursorAdapter scAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.main_header_list, container,
                false);

        // открываем подключение к БД
        DB = SQLiteOpenHelperUtil.getInstance().getDatabase();

        // формируем столбцы сопоставления
        String[] from = new String[]{
                TableCounteragents.COLUMN_NAME,
                TableCounteragents.COLUMN_ADDRESS,
                TableOrders.COLUMN_DATE,
                TableOrders.COLUMN_TOTAL};

        int[] to = new int[]{
                R.id.main_list_item_text_client,
                R.id.main_list_item_text_sub_client,
                R.id.main_list_item_text_date,
                R.id.main_list_item_sum};

        // создааем адаптер и настраиваем список
        scAdapter = new MySimpleCursorAdapter(getActivity(), R.layout.main_list_item, null, from, to, 0);
        /**/
        ListView lvData = (ListView) rootView.findViewById(R.id.main_heander_list_position);

        lvData.setAdapter(scAdapter);
        // добавляем контекстное меню к списку
        // final ImageView lvImageView = (ImageView) rootView.findViewById(R.id.main_heander_image_plus);


        // создаем лоадер для чтения данных
        getActivity().getSupportLoaderManager().initLoader(0, null, this);
        //
        ImageView imViewAdd = (ImageView) rootView.findViewById(R.id.main_heander_image_plus);
        //registerForContextMenu(imViewAdd);
        imViewAdd.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, IDM_A, Menu.NONE, "Menu A");
        menu.add(Menu.NONE, IDM_B, Menu.NONE, "Menu B");


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case IDM_A:
                Toast.makeText(getActivity(), "Выбран пункт А", Toast.LENGTH_LONG)
                        .show();
                return true;
            case IDM_B:
                Toast.makeText(getActivity(), "Выбран пункт B", Toast.LENGTH_LONG)
                        .show();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        return new MyCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {

        if (data.isClosed()) {
            // error
        } else {
            scAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {

    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {

            case R.id.main_heander_image_plus:
                final onEventListener someEventListener = (onEventListener) getActivity();
                someEventListener.onOpenFragmentClass(OrderNewHeaderFragment.class);
                // view.showContextMenu();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().getSupportLoaderManager().destroyLoader(0);
    }

    public interface onEventListener {
        void onOpenFragmentClass(Class<?> fClass);
    }

    private static class MyCursorLoader extends CursorLoader {

        public MyCursorLoader(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {
            return DB
                    .rawQuery(SQLQuery.queryOrders("Orders.number = ?"), new String[]{"0"});
        }

    }

    public static class MenuCustomAdapter extends ArrayAdapter {
        public static boolean flag = false;
        private Context context;
        private int textViewResourceId;
        private String[] objects;
        private LayoutInflater mLInflater;

        public MenuCustomAdapter(Context context, int textViewResourceId,
                                 String[] objects) {
            super(context, textViewResourceId, objects);
            this.context = context;
            this.textViewResourceId = textViewResourceId;
            this.objects = objects;
            mLInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }


        public View getCustomView(int position, View convertView, ViewGroup parent) {


            // if (convertView == null)

            //convertView = View.inflate(context, textViewResourceId, null);

            convertView = mLInflater.inflate(R.layout.spinner_row, parent, false);

            TextView label = (TextView) convertView.findViewById(R.id.spinner_row_item_text);
            label.setText(objects[position]);

            ImageView icon = (ImageView) convertView.findViewById(R.id.spinner_row_icon);

            switch (position) {
                case 0:
                    icon.setImageResource(R.mipmap.ic_edit);
                    break;
                case 1:
                    icon.setImageResource(R.mipmap.ic_held);
                    break;
                case 2:
                    icon.setImageResource(R.mipmap.ic_no_held);
                    break;
                default:
                    break;
            }

//            if (flag != false) {
//                TextView tv = (TextView) convertView;
//                tv.setText("");
//                ImageView icon2 = (ImageView)convertView;
//                icon2.setImageResource(R.mipmap.ic_clik_menu);
//            }


            return convertView;
        }
    }

    class MySimpleCursorAdapter extends SimpleCursorAdapter {

        private LayoutInflater mLInflater;

        public MySimpleCursorAdapter(final Context context, final int layout, final Cursor c, final String[] from, final int[] to, final int flags) {
            super(context, layout, c, from, to, flags);
            mLInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            convertView = mLInflater.inflate(R.layout.main_list_item, parent, false);

            Cursor itemCursor = (Cursor) getItem(position);

            String cDate = null;
            String cClient = null;
            String cAdress = null;
            String cTotal = null;

            if (itemCursor != null) {
                cDate = itemCursor.getString(itemCursor.getColumnIndex(TableOrders.COLUMN_DATE));
                cClient = itemCursor.getString(itemCursor.getColumnIndex(TableCounteragents.COLUMN_NAME));
                cAdress = itemCursor.getString(itemCursor.getColumnIndex(TableCounteragents.COLUMN_ADDRESS));
                cTotal = itemCursor.getString(itemCursor.getColumnIndex(TableOrders.COLUMN_TOTAL));
            }
            /*дата */
            final TextView date = (TextView) convertView.findViewById(R.id.main_list_item_text_date);
            date.setText(cDate);
            /*клиент*/
            final TextView client = (TextView) convertView.findViewById(R.id.main_list_item_text_client);
            client.setText(cClient);
            /*адресс*/
            final TextView adress = (TextView) convertView.findViewById(R.id.main_list_item_text_sub_client);
            adress.setText(cAdress);
            /*сумма*/
            final TextView total = (TextView) convertView.findViewById(R.id.main_list_item_sum);
            total.setText(cTotal);
            /*menu*/
            final Spinner spinner = (Spinner) convertView.findViewById(R.id.main_list_item_image_menu);
            // Настраиваем адаптер
            String[] cats = getResources().getStringArray(R.array.spinner_orders_menu);
            final MenuCustomAdapter adapter = new MenuCustomAdapter(getActivity(), R.layout.spinner_row, cats);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Вызываем адаптер
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent,
                                           View itemSelected, int selectedItemPosition, long selectedId) {


                    String[] choose = getResources().getStringArray(R.array.spinner_orders_menu);
                    Toast toast = Toast.makeText(getActivity(),
                            "Ваш выбор: " + choose[selectedItemPosition], Toast.LENGTH_SHORT);
                    toast.show();
                    MenuCustomAdapter.flag = true;
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });


            return convertView;
        }
    }

}
