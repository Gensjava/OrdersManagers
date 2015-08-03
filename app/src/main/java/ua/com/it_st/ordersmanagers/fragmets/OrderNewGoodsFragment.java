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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.AbstractList;
import java.util.ArrayList;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.sqlTables.TableCounteragents;
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
    private int counter = 0;
    private Loader ld;

    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(TreeNode node, Object value) {
            IconTreeItemHolder.IconTreeItem item = (IconTreeItemHolder.IconTreeItem) value;
            statusBar.setText("Last clicked: " + item.text);
            mSelectionArgs = "";
            // new MyCursorLoader(getActivity()).loadInBackground();
            // создаем лоадер для чтения данных
            //  db.addRec("sometext " + (scAdapter.getCount() + 1), R.drawable.ic_launcher);

            //обновляем курсор
            getActivity().getSupportLoaderManager().getLoader(1).forceLoad();
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


        // открываем подключение к БД
        DB = SQLiteOpenHelperUtil.getInstance().getDatabase();
        // создаем лоадер для чтения данных
        ld = getActivity().getSupportLoaderManager().initLoader(1, null, this);

        statusBar = (TextView) rootView.findViewById(R.id.status_bar);


        TreeNode root = TreeNode.root();
        TreeNode computerRoot = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_laptop, "Корень", ""));

        TreeNode myDocuments = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Каталог товаров", ""));

        TreeNode downloads = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Селектив", "1"));
        TreeNode downloads1 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Косметика", "2"));
        TreeNode downloads2 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Масмаркет", "3"));
        TreeNode downloads3 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Schwarzkopf", "4"));

        fillDownloadsFolderMyDocuments(downloads);

        AbstractList<TreeNode> treeNodes = new ArrayList<TreeNode>();
        treeNodes.add(downloads);
        treeNodes.add(downloads1);
        treeNodes.add(downloads2);
        treeNodes.add(downloads3);

        myDocuments.addChildren(treeNodes);
        computerRoot.addChildren(myDocuments);
        root.addChildren(myDocuments);

        tView = new AndroidTreeView(getActivity(), root);
        tView.setDefaultAnimation(true);
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

    private void fillDownloadsFolder(TreeNode node) {
        TreeNode downloads = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Downloads" + (counter++), "" + (counter++)));
        node.addChild(downloads);
        if (counter < 5) {
            fillDownloadsFolder(downloads);
        }
    }

    private void fillDownloadsFolderMyDocuments(TreeNode node) {
        TreeNode MyDocuments = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "My Documents" + (counter++), "" + (counter++)));
        node.addChild(MyDocuments);
        if (counter < 5) {
            fillDownloadsFolderMyDocuments(MyDocuments);
        }
    }

    private TreeNode[] fullGoodsTree(byte namberLevel) {

        TreeNode[] arrGoods = new TreeNode[namberLevel];
        for (int i = 0; i < arrGoods.length; i++) {
            // arrGoods[] =
        }

        return arrGoods;
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
            String catName = data.getString(data.getColumnIndex(TableProducts.COLUMN_NAME));
            Log.i("TABLE_NAME", "" + catName);
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
    // TableProducts.COLUMN_ID_CATEGORY + "= ?", // selection
    //     new String[] { "" }, // selectionArgs

    private static class MyCursorLoader extends CursorLoader {
        //String mSelectionArgs;

        public MyCursorLoader(Context context) {
            super(context);
            //  mSelectionArgs  = selectionArgs;
        }

        @Override
        public Cursor loadInBackground() {

            String lSpase = "\"";

            return DB
                    .query(TableProducts.TABLE_NAME, // table name
                            null, // columns
                            TableProducts.COLUMN_ID_CATEGORY + " = ?", // selection
                            new String[]{lSpase}, // selectionArgs
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
