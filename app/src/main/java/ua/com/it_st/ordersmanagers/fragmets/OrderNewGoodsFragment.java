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
import android.util.Log;
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
import ua.com.it_st.ordersmanagers.sqlTables.TableProducts;
import ua.com.it_st.ordersmanagers.treeElements.IconTreeItemHolder;
import ua.com.it_st.ordersmanagers.utils.SQLiteOpenHelperUtil;

/**
 * Created by Gens on 30.07.2015.
 */
public class OrderNewGoodsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private static SQLiteDatabase DB;
    private static String mSelectionArgs;
    private TextView statusBar;
    private AndroidTreeView tView;
    private TreeNode mNode;

    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(TreeNode node, Object value) {
            IconTreeItemHolder.IconTreeItem item = (IconTreeItemHolder.IconTreeItem) value;
            statusBar.setText("Last clicked: " + item.text);

            mNode = node;

            if (!item.click) {
                item.click = true;
                mSelectionArgs = item.id;
                //обновляем курсор
                getActivity().getSupportLoaderManager().getLoader(1).forceLoad();
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

        TreeNode root = TreeNode.root();
        TreeNode myRoot = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_laptop, "Корень", "", true));
        TreeNode myCatalog = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Каталог товаров", "", true));

        myRoot.addChildren(myCatalog);
        root.addChildren(myCatalog);
        mNode = myCatalog;

        tView = new AndroidTreeView(getActivity(), root);
        //tView.setDefaultAnimation(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
        tView.setDefaultViewHolder(IconTreeItemHolder.class);
        tView.setDefaultNodeClickListener(nodeClickListener);

        containerView.addView(tView.getView());

        if (savedInstanceState != null) {
            String state = savedInstanceState.getString("tState");
            if (!TextUtils.isEmpty(state)) {
                tView.restoreState(state);
            }
        }
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

            Integer ic_icon = isCategory.equals("true") ? R.string.ic_folder : null;
            TreeNode newFolder = new TreeNode(new IconTreeItemHolder.IconTreeItem(ic_icon, cName, cKod, false));
            tView.addNode(mNode, newFolder);
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
                    .query(TableProducts.TABLE_NAME, // table name
                            null, // columns
                            TableProducts.COLUMN_ID_CATEGORY + " = ?", // selection
                            new String[]{mSelectionArgs}, // selectionArgs
                            null, // groupBy
                            null, // having
                            null);// orderBy
        }

        @Override
        public void onCanceled(final Cursor cursor) {
            super.onCanceled(cursor);
            cursor.close();
        }
    }
}
