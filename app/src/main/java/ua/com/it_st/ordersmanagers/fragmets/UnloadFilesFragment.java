package ua.com.it_st.ordersmanagers.fragmets;


import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.utils.SQLQuery;
import ua.com.it_st.ordersmanagers.utils.SQLiteOpenHelperUtil;

public class UnloadFilesFragment extends FilesFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
         /* создаем лоадер для чтения данных */
        getActivity().getSupportLoaderManager().initLoader(0, null, this);

        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.load_files_button:
                /* удаляем все записи из таблиц */
                //   SQLiteOpenHelperUtil.onDeleteValueTables(getDb());
                /*загружаем файлы с сервера*/
                // dowloadFilesOfServer();
                break;
//            case R.id.load_files_image_button_n:
//                /*переходим в журанл заказаов*/
//                final onEventListener someEventListener = (onEventListener) getActivity();
//                someEventListener.onOpenFragmentClass(OrderListFragment.class);
//                break;
            default:
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        /* выходим из загрузчкика*/
        getActivity().getSupportLoaderManager().destroyLoader(0);
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        return new MyCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {

        switch (loader.getId()) {
            case 0:
                  /*добавляем элемент к дереву*/
                // onAddTree(data);
                break;
            case 1:/*режим редактирование*/
                   /*Заполняем корзину*/
                //  onFillCart(data);
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {

    }

    /* создаем класс для загрузки данных в дерево товаров из БД
               * загрузка происходит в фоне */
    private class MyCursorLoader extends CursorLoader {

        public MyCursorLoader(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {

            return getDb()
                    .rawQuery(SQLQuery.queryOrders("Orders._id  <> ?"), new String[]{"null"});
        }
    }

    public class UnloadAsyncFile extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(final String... params) {
            return null;
        }
    }

}
