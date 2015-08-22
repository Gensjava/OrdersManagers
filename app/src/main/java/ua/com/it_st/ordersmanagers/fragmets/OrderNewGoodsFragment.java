package ua.com.it_st.ordersmanagers.fragmets;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.sqlTables.TableGoodsByStores;
import ua.com.it_st.ordersmanagers.sqlTables.TablePrices;
import ua.com.it_st.ordersmanagers.sqlTables.TableProducts;
import ua.com.it_st.ordersmanagers.models.TreeProductCategoryHolder;
import ua.com.it_st.ordersmanagers.utils.ConstantsUtil;
import ua.com.it_st.ordersmanagers.utils.Dialogs;
import ua.com.it_st.ordersmanagers.utils.SQLQuery;
import ua.com.it_st.ordersmanagers.utils.SQLiteOpenHelperUtil;

import static ua.com.it_st.ordersmanagers.models.TreeProductCategoryHolder.*;

/* Класс предназначен для подбора товара к заказу
* при выборе если нехватает кв-ко товара на складе
* будет выдавать сообщения, что нехватает на складе товара*/

public class OrderNewGoodsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private static TextView ui_cart;
    private static SQLiteDatabase sDb;
    private static String mSelectionArgs;
    private static TextView tSumCart;
    private AndroidTreeView tView;
    private TreeNode mNode;
    private View rootView;

    /*обрабытывам клик на позиции дерева*/
    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(final TreeNode node, final Object value) {

            final TreeItem item = (TreeItem) value;
            /* Текущая позиция дерева */
            mNode = node;
            if (item.isCategory()) {
                if (!item.isClick()) {
                    /* записываем клик на позиции чтоб два раза не строить ветки */
                    item.setClick(true);
                    /* параметр для запроса */
                    mSelectionArgs = item.getId();
                    /* обновляем курсор */
                    getActivity().getSupportLoaderManager().getLoader(0).forceLoad();
                }
            } else {
                Dialogs dialogs = new Dialogs(getActivity());
                dialogs.showCustomAlertDialogEnterNumber(getString(R.string.addCart), item, mNode);
            }
        }
    };

    /*
    определяем показывать к-во товара в корзине или нет
    если больше 0 тогда показываем
    */
    public static void updateCartCount() {

        if (ui_cart == null) return;

         /*Показываем сумму заказа в подвале*/
        String tSum = ConstantsUtil.getTotalOrder() == 0.0 ? "0.00" : String.valueOf(ConstantsUtil.getTotalOrder());
        tSumCart.setText(tSum + " грн.");

        if (ConstantsUtil.mCart.size() == 0) {
            ui_cart.setVisibility(View.INVISIBLE);
        } else {
            ui_cart.setVisibility(View.VISIBLE);
            ui_cart.setText(Integer.toString(ConstantsUtil.mCart.size()));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if ((rootView == null)) {
            rootView = inflater.inflate(R.layout.order_new_goods_container, null, false);
            final ViewGroup containerView = (ViewGroup) rootView.findViewById(R.id.container);

        /* Это корень */
            TreeNode root = TreeNode.root();
            TreeNode myRoot = new TreeNode(new TreeItem(R.string.ic_folder, getString(R.string.root), "", true, true));
            TreeNode myCatalog = new TreeNode(new TreeItem(R.string.ic_folder, "Каталог товаров", "", true, true));
        /* создаем ветки */
            myRoot.addChildren(myCatalog);
            root.addChildren(myCatalog);
            mNode = myCatalog;

        /* создаем древо */
            tView = new AndroidTreeView(getActivity(), root);
            // tView.setDefaultAnimation(true);
            tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
            tView.setDefaultViewHolder(TreeProductCategoryHolder.class);
            tView.setDefaultNodeClickListener(nodeClickListener);

        /* загружаем дерево */
            containerView.addView(tView.getView());

            if (savedInstanceState != null) {
                String state = savedInstanceState.getString("tState");
                if (!TextUtils.isEmpty(state)) {
                    tView.restoreState(state);
                }
            }
        /* у корня дерева ИД = "" */
            mSelectionArgs = "";
        /* открываем подключение к БД */
            sDb = SQLiteOpenHelperUtil.getInstance().getDatabase();
       
        /* кнопка далее переход на следующий этап*/
            ImageView imViewAdd = (ImageView) rootView.findViewById(R.id.order_new_goods_container_image);
        /* слушатель кнопки далее */
            imViewAdd.setOnClickListener(this);
        /*Отображаем сумму заказа в подвале*/
            tSumCart = (TextView) rootView.findViewById(R.id.order_new_goods_container_sum_cart);
        }
        
        /* создаем лоадер для чтения данных */
        getActivity().getSupportLoaderManager().initLoader(0, null, this);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        /* корзина */
        final MenuItem customCart = menu.add(0, R.id.menu_cart, 0, "");
        customCart.setActionView(R.layout.main_tool_bar_cart);
        customCart.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        final View menu_cart = menu.findItem(R.id.menu_cart).getActionView();
        /* клик на корзине */
        menu_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final onEventListener someEventListener = (onEventListener) getActivity();
                someEventListener.onOpenFragmentClass(OrderNewCartFragment.class);
            }
        });
        /*картинка корзины*/
        ui_cart = (TextView) menu_cart.findViewById(R.id.main_tool_bar_cart_text);
        /*обновляем корзину*/
        updateCartCount();

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.expandAll:
                /*раскрыть дерево*/
                tView.expandAll();
                break;
            case R.id.collapseAll:
                /*свернуть все дерево*/
                tView.collapseAll();
                break;
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tState", tView.getSaveState());
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        return new MyCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {

        while (data.moveToNext()) {
            String cName = data.getString(data.getColumnIndex(TableProducts.COLUMN_NAME));
            String isCategory = data.getString(data.getColumnIndex(TableProducts.COLUMN_IS_CATEGORY));
            String cKod = data.getString(data.getColumnIndex(TableProducts.COLUMN_KOD));

            final TreeNode newTreeItem;

            switch (isCategory) {
                case "true":
                    newTreeItem = new TreeNode(new TreeItem(R.string.ic_folder, cName, cKod, false, true));
                    break;
                default:
                    String sBalance = data.getString(data.getColumnIndex(TableGoodsByStores.COLUMN_AMOUNT));
                    String sPrice = data.getString(data.getColumnIndex(TablePrices.COLUMN_PRICE));

                    double cBalance = Double.parseDouble(String.valueOf(sBalance == null ? 0 : sBalance));
                    double cPrice = Double.parseDouble(String.valueOf(sPrice == null ? 0 : sPrice));

                    newTreeItem = new TreeNode(new TreeItem(cName, cKod, false, cBalance, 0, false, cPrice));
            }

            tView.addNode(mNode, newTreeItem);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        getActivity().getSupportLoaderManager().destroyLoader(0);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        new MyCursorLoader(getActivity());
    }

    @Override
    public void onClick(final View view) {

        switch (view.getId()) {

            case R.id.order_new_goods_container_image:
                final onEventListener someEventListener = (onEventListener) getActivity();
                someEventListener.onOpenFragmentClass(OrderNewCartFragment.class);
                break;
            default:
                break;
        }
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

            return sDb
                    .rawQuery(SQLQuery.queryGoods("Products.id_category = ?"
                    ), new String[]{mSelectionArgs

                    });
        }
    }

}
