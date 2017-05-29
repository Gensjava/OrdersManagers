package ua.com.it_st.ordersmanagers.fragmets;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
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

import java.math.BigDecimal;
import java.math.RoundingMode;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.enums.DocTypeOperation;
import ua.com.it_st.ordersmanagers.interfaces.implems.DocListOrderAction;
import ua.com.it_st.ordersmanagers.models.Orders;
import ua.com.it_st.ordersmanagers.models.Products;
import ua.com.it_st.ordersmanagers.models.TreeProductCategoryHolder;
import ua.com.it_st.ordersmanagers.sqlTables.TableGoodsByStores;
import ua.com.it_st.ordersmanagers.sqlTables.TableOrdersLines;
import ua.com.it_st.ordersmanagers.sqlTables.TablePrices;
import ua.com.it_st.ordersmanagers.sqlTables.TableProducts;
import ua.com.it_st.ordersmanagers.utils.ConnectServer;
import ua.com.it_st.ordersmanagers.utils.ConstantsUtil;
import ua.com.it_st.ordersmanagers.utils.Dialogs;
import ua.com.it_st.ordersmanagers.utils.InfoUtil;
import ua.com.it_st.ordersmanagers.utils.SQLQuery;
import ua.com.it_st.ordersmanagers.utils.SQLiteOpenHelperUtil;
import ua.com.it_st.ordersmanagers.utils.WorkFragment;

import static ua.com.it_st.ordersmanagers.models.TreeProductCategoryHolder.TreeItem;

/* Класс предназначен для подбора товара к заказу
* при выборе если нехватает кв-ко товара на складе
* будет выдавать сообщения, что нехватает на складе товара*/

public class OrderCatalogGoodsFragment extends FilesFragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    public static String GOODS_KOD = "GOODS_KOD";
    private static TextView ui_cart;
    private static SQLiteDatabase sDb;
    private static String mSelectionArgs;
    private static TextView tSumCart;
    private AndroidTreeView tView;
    private TreeNode mNode;
    private ImageView ui_dialog;
    private ImageView ui_picture;
    private TextView ui_dialog_auont;
    private boolean bModePicture;
    private Bundle outState;
    private View rootView;
    private Orders mCurrentOrder;
    /*обрабытывам клик на позиции дерева*/
    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(final TreeNode node, final Object value) {

            final TreeItem item = (TreeItem) value;
            outState = null;
            /* Текущая позиция дерева */
            mNode = node;
            if (item.getProduct().isCategory()) {
                if (!item.isClick()) {
                    /* записываем клик на позиции чтоб два раза не строить ветки */
                    item.setClick(true);
                    /* параметр для запроса */
                    mSelectionArgs = item.getProduct().getKod();

                    Loader lLoader = getActivity().getSupportLoaderManager().getLoader(0);
                    /* обновляем курсор */
                    if (lLoader != null) {
                        lLoader.forceLoad();
                    }
                }

            } else {

                if (Dialogs.openDialog) {//обычный режим набираем в корзину

                    Dialogs.product = item;
                    double numberInDialog = Dialogs.numberD;
                    double sumInDialog = Dialogs.product.getPrice() * Dialogs.numberD;
                    setDialogAmount(numberInDialog, sumInDialog, (TreeProductCategoryHolder.TreeItem) Dialogs.product);

                } else if (bModePicture) {//показываем картинки
                    Bundle bundle = new Bundle();
                    bundle.putString(GOODS_KOD, item.getProduct().getKod());
                    WorkFragment.onNewInstanceFragment(PictureFragment.class, bundle, (MainActivity) getActivity());

                } else {//повтор к-во товаров
                    Dialogs.showCustomAlertDialogEnterNumber(getActivity(), getString(R.string.addCart), item, OrderCatalogGoodsFragment.class.toString());
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);


        } else {
            rootView = inflater.inflate(R.layout.order_new_goods_container, null, false);
            final ViewGroup containerView = (ViewGroup) rootView.findViewById(R.id.container);
     /* Это корень */
            TreeNode root = TreeNode.root();

            TreeNode myRoot = new TreeNode(new TreeItem(R.string.ic_folder, new Products("", getString(R.string.root), true), true));
            TreeNode myCatalog = new TreeNode(new TreeItem(R.string.ic_folder, new Products("", "Каталог товаров", true), true));
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

            mCurrentOrder = (Orders) ((MainActivity) getActivity()).getmCurrentOrder();

            if (mCurrentOrder != null) {
                if (mCurrentOrder.getTypeOperation().equals(DocTypeOperation.EDIT)
                        || mCurrentOrder.getTypeOperation().equals(DocTypeOperation.COPY)) {

                    if (!mCurrentOrder.isClickModifitsirovannoiCart()) {
                /* создаем лоадер для чтения данных */
                        getActivity().getSupportLoaderManager().initLoader(1, null, this);
                    }
                }
                if (mCurrentOrder.getStore().getKod() != null) {
           /* создаем лоадер для чтения данных */
                    getActivity().getSupportLoaderManager().initLoader(0, null, this);
                }
            }
        }

        return rootView;
    }

    /*
    определяем показывать к-во товара в корзине или нет
    если больше 0 тогда показываем
    */
    public void updateCartCount() {

        if (ui_cart == null) return;

        DocListOrderAction lUpDateOrderList = new DocListOrderAction();
        double sum = lUpDateOrderList.sum();

         /*Показываем сумму заказа в подвале*/
        String tSum = sum == 0.0 ? getString(R.string.zero_point_text) : String.valueOf(sum);
        tSumCart.setText(tSum + getString(R.string.grn));

        if (DocListOrderAction.mCart.size() == 0) {
            ui_cart.setVisibility(View.INVISIBLE);
        } else {
            ui_cart.setVisibility(View.VISIBLE);
            ui_cart.setText(Integer.toString(DocListOrderAction.mCart.size()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        /* открываем подключение к БД */
        if (sDb == null) {
            sDb = SQLiteOpenHelperUtil.getInstance().getDatabase();
        }

        if (mCurrentOrder.getTypeOperation().equals(DocTypeOperation.EDIT)
                || mCurrentOrder.getTypeOperation().equals(DocTypeOperation.COPY)) {

            if (!mCurrentOrder.isClickModifitsirovannoiCart()) {
                /* создаем лоадер для чтения данных */
                getActivity().getSupportLoaderManager().initLoader(1, null, this);
                getActivity().getSupportLoaderManager().getLoader(1).forceLoad();
            }
        }
        if (mCurrentOrder.getStore().getKod() != null) {
           /* создаем лоадер для чтения данных */
            getActivity().getSupportLoaderManager().initLoader(0, null, this);
            getActivity().getSupportLoaderManager().getLoader(0).forceLoad();
        }

    }

    /*обновление после выбора к-во товара в списке товаров*/
    public void setDialogAmount(final double amount, final double sum, final TreeProductCategoryHolder.TreeItem product) {

        TreeProductCategoryHolder.TreeItem treeItem = new TreeItem(
                product.getProduct(),
                amount,
                product.getPrice(),
                sum,
                product.getBalance(), true
        );

        /* к-во заказа */
        final TextView orderTvValue = (TextView) mNode.getViewHolder().getView().findViewById(R.id.order_new_goods_node_item_order_value);

        /* делаем проверку товара на остатке */
        if (product.getBalance() >= amount) {

            DocListOrderAction lUpDateOrderList = new DocListOrderAction();

            if (amount > 0) {
                orderTvValue.setVisibility(View.VISIBLE);
                orderTvValue.setText(String.valueOf(amount));
                /* добавляем в табличную часть заказа */
                lUpDateOrderList.add(treeItem);
            } else {
                orderTvValue.setVisibility(View.INVISIBLE);
                /* удаляем из табличной части заказа */
                lUpDateOrderList.delete(treeItem);
            }
        } else {
            //
            InfoUtil.Tost(getString(R.string.not_goods_store_number), getActivity());
        }

        /* обновляем корзину */
        updateCartCount();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        /* домой */
        final MenuItem customНоме = menu.add(0, R.id.collapseAll, 0, "");
        customНоме.setActionView(R.layout.main_tool_bar_home);
        customНоме.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        final View menu_home = menu.findItem(R.id.collapseAll).getActionView();
        /* клик на домой */
        menu_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                /*свернуть все дерево*/
                tView.collapseAll();
            }
        });

        /* показать изображение товара */
        final MenuItem showPicture = menu.add(0, R.id.menu_picture, 0, "");
        showPicture.setActionView(R.layout.main_tool_bar_picture);
        showPicture.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        final View menu_picture = menu.findItem(R.id.menu_picture).getActionView();
        /*количество для выбора в заказ*/
        ui_picture = (ImageView) menu_picture.findViewById(R.id.main_tool_bar_picture_image);
        /*обновляем*/
        modePicture();
        /* клик на кнопке */
        menu_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (Dialogs.openDialog) return;
                bModePicture = !bModePicture;
                /*показываем*/
                modePicture();
            }
        });

        /* фиксация диалога заказов выбора по количеству */
        final MenuItem customDialogAmuont = menu.add(0, R.id.menu_dialog_amuont, 0, "");
        customDialogAmuont.setActionView(R.layout.main_tool_bar_chick_dialog);
        customDialogAmuont.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        final View menu_dialog_amuont = menu.findItem(R.id.menu_dialog_amuont).getActionView();

        /*количество для выбора в заказ*/
        ui_dialog = (ImageView) menu_dialog_amuont.findViewById(R.id.main_tool_bar_chick_dialog_image);
         /*количество для выбора в заказ*/
        ui_dialog_auont = (TextView) menu_dialog_amuont.findViewById(R.id.main_tool_bar_chick_dialog_text);
        /*обновляем выбор*/
        updateSelectDialog();
        /* клик для выбора в заказ */
        menu_dialog_amuont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (bModePicture) return;
                Dialogs.openDialog = !Dialogs.openDialog;
                // меняем фон
                updateSelectDialog();
            }
        });
        /* корзина */
        final MenuItem customCart = menu.add(0, R.id.menu_cart, 0, "");
        customCart.setActionView(R.layout.main_tool_bar_cart);
        customCart.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        final View menu_cart = menu.findItem(R.id.menu_cart).getActionView();
        /* клик на корзине */
        menu_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                /*проверяем пустая корзина или нет*/
                openCart();
            }
        });
        /*картинка корзины*/
        ui_cart = (TextView) menu_cart.findViewById(R.id.main_tool_bar_cart_text);
        /*обновляем корзину*/
        updateCartCount();

        super.onCreateOptionsMenu(menu, inflater);
    }

    // меняем фон для режима изображения
    private void modePicture() {
        if (ui_picture == null) return;
        if (Dialogs.openDialog) return;

        // меняем фон на кнопке
        if (!bModePicture) {
            ConstantsUtil.sConnectData = null;
            ui_picture.setBackgroundResource(R.color.main_grey);
        } else {
            ui_picture.setBackgroundResource(R.color.main_grey_select);

            if (ConstantsUtil.sConnectData == null) {
                 /*подключаемся к серверу*/
                ConstantsUtil.sConnectData = new ConnectServer(getActivity(), (byte) 2);
            }
        }
    }

    // меняем фон для выбранного количества в диалоге
    private void updateSelectDialog() {
        if (ui_dialog == null) return;
        if (bModePicture) return;

        // меняем фон на кнопке
        if (!Dialogs.openDialog) {
            ui_dialog.setBackgroundResource(R.color.main_grey);
            ui_dialog_auont.setVisibility(View.INVISIBLE);
            Dialogs.numberD = 1;
        } else {
            ui_dialog.setBackgroundResource(R.color.main_grey_select);
            ui_dialog_auont.setVisibility(View.VISIBLE);
            ui_dialog_auont.setText(String.valueOf((int) Dialogs.numberD));
        }
    }

    /*открываем корзину*/
    protected void openCart() {

        DocListOrderAction lUpDateOrderList = new DocListOrderAction();
             /*проверяем пустая корзина или нет*/
        if (!lUpDateOrderList.isEmpty()) {
            final onEventListener someEventListener = (onEventListener) getActivity();
            someEventListener.onOpenFragmentClass(OrderCartFragment.class);
        } else {
            InfoUtil.showErrorAlertDialog(getString(R.string.car_empty), getString(R.string.order), getActivity());
        }
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
            case R.id.menu_cart:
                /*проверяем пустая корзина или нет*/
                openCart();
                break;
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (tView != null) {
            outState.putString("tState", tView.getSaveState());
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {

        switch (id) {
            case 0:
                return new MyCursorLoader(getActivity(), mCurrentOrder);
            case 1:/*режим редактирование*/
                return new MyCursorLoaderCart(getActivity());
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {

        switch (loader.getId()) {
            case 0:
                  /*добавляем элемент к дереву*/
                onAddTree(data);
                break;
            case 1:/*режим редактирование*/
                   /*Заполняем корзину*/
                onFillCart(data);
                break;
            default:
                break;
        }

    }

    /*Заполняем корзину*/
    private void onFillCart(Cursor data) {

        final int cIDIndex = data.getColumnIndex(TableOrdersLines.COLUMN_GOODS_ID);
        final int isAmountIndex = data.getColumnIndex(TableOrdersLines.COLUMN_AMOUNT);
        final int cPriceIndex = data.getColumnIndex(TableOrdersLines.COLUMN_PRICE);
        final int cNameIndex = data.getColumnIndex(TableProducts.COLUMN_NAME);
        final int cAmountStoresIndex = data.getColumnIndex(TableProducts.AMOUNT_STORES);

        while (data.moveToNext()) {

            final String cID = data.getString(cIDIndex);
            final double isAmount = data.getDouble(isAmountIndex);
            final double cPrice = data.getDouble(cPriceIndex);
            final String cName = data.getString(cNameIndex);
            final double cAmountStores = data.getDouble(cAmountStoresIndex);

            Products products = new Products(cID, cName);

            final double newSum = new BigDecimal(isAmount * cPrice).setScale(2, RoundingMode.UP).doubleValue();

            Orders.OrdersLines tableOrders = new Orders.OrdersLines(
                    mCurrentOrder.getId(),
                    products,
                    isAmount,
                    cPrice,
                    newSum
            );

            DocListOrderAction lUpDateOrderList = new DocListOrderAction();
            lUpDateOrderList.add(tableOrders);
        }
                 /*обновляем корзину*/
        updateCartCount();
    }

    /*добавляем элемент к дереву*/
    private void onAddTree(Cursor data) {

        if (outState != null) {
            String state = outState.getString("tState");
            if (!TextUtils.isEmpty(state)) {
                tView.restoreState(state);
            }
        } else {
            final int cNameIndex = data.getColumnIndex(TableProducts.COLUMN_NAME);
            final int isCategoryIndex = data.getColumnIndex(TableProducts.COLUMN_IS_CATEGORY);
            final int cKodIndex = data.getColumnIndex(TableProducts.COLUMN_KOD);

            while (data.moveToNext()) {

                final String cName = data.getString(cNameIndex);
                final String isCategory = data.getString(isCategoryIndex);
                final String cKod = data.getString(cKodIndex);

                Products product = new Products(cKod, cName, isCategory.equals("true"));

                final TreeNode newTreeItem;

                if (product.isCategory()) {
                    newTreeItem = new TreeNode(new TreeItem(R.string.ic_folder, product, false));
                } else {
                    double sBalance = data.getDouble(data.getColumnIndex(TableGoodsByStores.COLUMN_AMOUNT));
                    double sPrice = data.getDouble(data.getColumnIndex(TablePrices.COLUMN_PRICE));

                    newTreeItem = new TreeNode(new TreeItem(product, 0, sPrice, 0, sBalance, false));
                }

                tView.addNode(mNode, newTreeItem);
            }
        }
    }
    @Override
    public void onPause() {
        super.onPause();

        if (mCurrentOrder.getTypeOperation().equals(DocTypeOperation.EDIT)
                || mCurrentOrder.getTypeOperation().equals(DocTypeOperation.COPY)) {

            if (!mCurrentOrder.isClickModifitsirovannoiCart()) {
                /* создаем лоадер для чтения данных */
                getActivity().getSupportLoaderManager().destroyLoader(1);
            }
        }
        if (mCurrentOrder.getStore().getKod() != null) {
           /* создаем лоадер для чтения данных */
            getActivity().getSupportLoaderManager().destroyLoader(0);
        }
        outState = new Bundle();
        outState.putString("tState", tView.getSaveState());
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        new MyCursorLoader(getActivity(), mCurrentOrder);
    }

    @Override
    public void onClick(final View view) {

        switch (view.getId()) {

            case R.id.order_new_goods_container_image:
                DocListOrderAction lUpDateOrderList = new DocListOrderAction();

                /*прповеряем корзину пустая или нет*/
                if (!lUpDateOrderList.isEmpty()) {
                    final onEventListener someEventListener = (onEventListener) getActivity();
                    someEventListener.onOpenFragmentClass(OrderCartFragment.class);
                } else {
                    InfoUtil.showErrorAlertDialog(getString(R.string.car_empty), getString(R.string.order), getActivity());
                }
                break;
            default:
                break;
        }
    }

    /* создаем класс - интефейс для открытия фрагментов */
    public interface onEventListener {
        void onOpenFragmentClass(Class<?> fClass);
    }

    /* создаем класс для загрузки данных в дерево товаров из БД
           * загрузка происходит в фоне */
    private static class MyCursorLoader extends CursorLoader {

        private Orders mCurrentOrder;

        public MyCursorLoader(Context context, Orders mCurrentOrder) {
            super(context);
            this.mCurrentOrder = mCurrentOrder;
        }

        @Override
        public Cursor loadInBackground() {

            return sDb
                    .rawQuery(SQLQuery.queryGoods("Products.id_category = ? " +
                                    "and GoodsByStores.kod_stores = ?" +
                                    "and Prices.price_category_kod = ? " +
                                    "or Products.is_category = ? " +
                                    "and Products.id_category = ?"
                    ), new String[]{mSelectionArgs, mCurrentOrder.getStore().getKod(), mCurrentOrder.getTypePrices().getKod(), "true", mSelectionArgs
                    });
        }
    }

    /* создаем класс для загрузки данных таблтчной части дока из БД
           * загрузка происходит в фоне */
    private static class MyCursorLoaderCart extends CursorLoader {

        public MyCursorLoaderCart(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {

            return sDb
                    .rawQuery(SQLQuery.queryOrdersLinesEdit("OrdersLines.doc_id = ?"
                    ), new String[]{OrderHeaderDoc.id_order

                    });
        }
    }
}
