package ua.com.it_st.ordersmanagers.fragmets;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.TextView;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.models.Product;
import ua.com.it_st.ordersmanagers.sqlTables.TableGoodsByStores;
import ua.com.it_st.ordersmanagers.sqlTables.TablePrices;
import ua.com.it_st.ordersmanagers.sqlTables.TableProducts;
import ua.com.it_st.ordersmanagers.models.TreeProductCategoryHolder;
import ua.com.it_st.ordersmanagers.utils.Dialogs;
import ua.com.it_st.ordersmanagers.utils.SQLiteOpenHelperUtil;

public class OrderNewGoodsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private static SQLiteDatabase DB;
    private static String mSelectionArgs;
    private TextView statusBar;
    private AndroidTreeView tView;
    private TreeNode mNode;

    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(TreeNode node, Object value) {

            TreeProductCategoryHolder.TreeItem item = (TreeProductCategoryHolder.TreeItem) value;
            statusBar.setText("Last clicked: " + item.text);

            mNode = node;
            if (item.isCategory) {
                if (!item.click) {
                    item.click = true;
                    mSelectionArgs = item.id;
                    //обновляем курсор
                    getActivity().getSupportLoaderManager().getLoader(1).forceLoad();
                }
            } else {
                Product product = new Product();
                Dialogs.showCustomAlertDialogEnterNumber("Добавить в корзину", getActivity(), item);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_default, null, false);
        ViewGroup containerView = (ViewGroup) rootView.findViewById(R.id.container);

        statusBar = (TextView) rootView.findViewById(R.id.status_bar);

        //Это корень
        TreeNode root = TreeNode.root();
        TreeNode myRoot = new TreeNode(new TreeProductCategoryHolder.TreeItem(R.string.ic_folder, getString(R.string.root), "", true, true));
        TreeNode myCatalog = new TreeNode(new TreeProductCategoryHolder.TreeItem(R.string.ic_folder, "Каталог товаров", "", true, true));

        myRoot.addChildren(myCatalog);
        root.addChildren(myCatalog);
        mNode = myCatalog;

        //создаем древо
        tView = new AndroidTreeView(getActivity(), root);
        // tView.setDefaultAnimation(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
        tView.setDefaultViewHolder(TreeProductCategoryHolder.class);
        tView.setDefaultNodeClickListener(nodeClickListener);

        containerView.addView(tView.getView());

        if (savedInstanceState != null) {
            String state = savedInstanceState.getString("tState");
            if (!TextUtils.isEmpty(state)) {
                tView.restoreState(state);
            }
        }
        //у корня дерева ИД = ""
        mSelectionArgs = "";
        // открываем подключение к БД
        DB = SQLiteOpenHelperUtil.getInstance().getDatabase();
        // создаем лоадер для чтения данных
        getActivity().getSupportLoaderManager().initLoader(1, null, this);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.expandAll:
                tView.expandAll();
                break;
            case R.id.collapseAll:
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
                    newTreeItem = new TreeNode(new TreeProductCategoryHolder.TreeItem(R.string.ic_folder, cName, cKod, false, true));
                    break;
                default:
                    String cBalance = data.getString(data.getColumnIndex(TableGoodsByStores.COLUMN_AMOUNT));
                    double cPrice = Double.parseDouble(data.getString(data.getColumnIndex(TablePrices.COLUMN_PRICE)));

                    newTreeItem = new TreeNode(new TreeProductCategoryHolder.TreeItem(cName, cKod, false, cBalance, null, false, cPrice));

            }

            tView.addNode(mNode, newTreeItem);
        }
        //data.close();
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        new MyCursorLoader(getActivity());
    }

    @Override
    public void onClick(final View view) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // закрываем подключение при выходе
        DB.close();
    }

    private static class MyCursorLoader extends CursorLoader {

        public MyCursorLoader(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {

            return DB
                    .rawQuery("Select Products.name, Products.kod, Products.id_category, Products.is_category," +
                            "GoodsByStores.Amount, GoodsByStores.kod_stores, Prices.price\n" +
                            "FROM Products\n" +
                            "LEFT OUTER JOIN GoodsByStores ON Products.kod = GoodsByStores.kod_coods\n" +
                            "LEFT OUTER JOIN Prices ON Products.kod = Prices.kod\n" +
                            "WHERE Products.id_category = ? \n" +
                            "GROUP by Products.kod", new String[]{mSelectionArgs});

        }

        @Override
        public void onCanceled(final Cursor cursor) {
            super.onCanceled(cursor);
            cursor.close();
        }
    }
}
