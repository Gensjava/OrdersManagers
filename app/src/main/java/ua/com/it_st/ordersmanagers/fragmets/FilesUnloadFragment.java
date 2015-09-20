package ua.com.it_st.ordersmanagers.fragmets;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;

import com.loopj.android.http.RequestParams;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.sqlTables.TableOrders;
import ua.com.it_st.ordersmanagers.sqlTables.TableOrdersLines;
import ua.com.it_st.ordersmanagers.utils.AsyncHttpClientUtil;
import ua.com.it_st.ordersmanagers.utils.InfoUtil;
import ua.com.it_st.ordersmanagers.utils.SQLQuery;


public class FilesUnloadFragment extends FilesFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private AsyncHttpClientUtil mUtilAsyncHttpClient;
    private String mWayCatalog;
    private int mAcuont;
    private int mProgress;
    private double pProgressDiscrete;
    private double pProgressPie;

    @Override
    public void onResume() {
        super.onResume();
        //красим
        //кнопку
        getBHost().setBackgroundResource(R.drawable.roundbutton_red);
        getBHost().setOnClickListener(this);
        getBHost().setText("ВЫГРУЗИТЬ");
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.load_files_button:
                /*чистим все параметры */
                nullableValues();
                mProgress = 0;
                pProgressPie = 0;
                 /*получаем подключение к серверу*/
                final Object[] ctServer = connectServer();

                mUtilAsyncHttpClient = (AsyncHttpClientUtil) ctServer[1];
                mWayCatalog = (String) ctServer[4];

                for (byte i = 0; i < 3; i++) {
                    getActivity().getSupportLoaderManager().destroyLoader(i);
                    getActivity().getSupportLoaderManager().initLoader(i, null, this);
                }

                break;
            case R.id.load_files_image_button_n:
                /*переходим в журанл заказаов*/
                onEventListener someEventListener = (onEventListener) getActivity();
                someEventListener.onOpenFragmentClass(OrderListFragment.class);
                break;
            case R.id.load_files_imageView:
                /*переходим в список ощибок*/
                someEventListener = (onEventListener) getActivity();
                someEventListener.onOpenFragmentClass(InfoFragment.class);
                break;
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
        switch (id) {
            case 0:
                return new MyCursorLoaderLinesAcuont(getActivity());
            case 1:
                return new MyCursorLoaderHeader(getActivity());
            case 2:
                return new MyCursorLoaderLines(getActivity());
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {

        final String nameFile;
        final String[] headerLines;

        switch (loader.getId()) {
            case 0:
                data.moveToNext();
                /*получаем количество строк двух таблиц*/
                mAcuont = data.getInt(data.getColumnIndex("sum"));
                /*устанавливаем максимальное количество бара*/
                getProgressPieView().setMax(mAcuont);
                break;
            case 1:
                  /*формируем документ заказ шапку*/
                nameFile = TableOrders.FILE_NAME;
                headerLines = TableOrders.sHeader.split(",");
                try {
                    getAllOrdersHeaderLines(data, nameFile, headerLines);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                 /*формируем документ табличную часть*/
                nameFile = TableOrdersLines.FILE_NAME;
                headerLines = TableOrdersLines.sHeader.split(",");
                try {
                    getAllOrdersHeaderLines(data, nameFile, headerLines);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {

    }

    /*получаем все заказы (шапку заказов)*/
    private void getAllOrdersHeaderLines(final Cursor itemCursor, String nameFile, final String[] headerLines) throws IOException {

        String path = null;
        /* получаем количество строк в каждой таблице отдельно*/
        int sizeBar = itemCursor.getCount();
        int n = 0;/*счетчики*/
        pProgressDiscrete = 0;

        /*устанавливаем максимальное количество бара*/
        getDiscreteSeekBar().setProgress(sizeBar);

        try {
            path = getDataDir(getActivity()) + "/" + nameFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        final CSVWriter myFile = new CSVWriter(new FileWriter(path), ',');
        myFile.writeNext(headerLines);

        final String[] arrayColumnNames = itemCursor.getColumnNames();

        while (itemCursor.moveToNext()) {

            final String[] entries = new String[arrayColumnNames.length];

            for (byte i = 0; i < arrayColumnNames.length; i++) {
                final int cIndex = itemCursor.getColumnIndex(arrayColumnNames[i]);
                entries[i] = itemCursor.getString(cIndex);
            }
            myFile.writeNext(entries);
            n++;
            mProgress++;
           /*двигаем бар */
            onProgressBar(n, sizeBar);

        }
        myFile.flush();
        myFile.close();

        RequestParams params = new RequestParams();

        try {
            params.put(mWayCatalog + nameFile, new File(path));
            mUtilAsyncHttpClient.postUnloadFiles(params, nameFile);
        } catch (Exception e) {
            e.printStackTrace();
            //Log
            InfoUtil.setmLogLine("Выгрузка файла ", params.toString(), true, getTEG() + " " + e.toString());
        }
    }

    /*двигаем бар и вычисляем процент выполнения */
    private void onProgressBar(int DprogressSeekBar, final int sizeBar) {

        getDiscreteSeekBar().setProgress(DprogressSeekBar);
        pProgressDiscrete += (int) (100 / (double) sizeBar);
        setProgressDiscrete(pProgressDiscrete);
         /*надпись бара*/
        getTextProgress().setText(String.valueOf((int) getProgressDiscrete()) + "%");
        /* Круг прогресс */
        getProgressPieView().setProgress(mProgress);
        pProgressPie += (100 / (double) mAcuont);
        setProgress(pProgressPie);
        getProgressPieView().setText(String.valueOf((int) getProgress()) + "%");

    }

    /* создаем класс для выгрузки данных заказов шапки из БД
               * загрузка происходит в фоне */
    private static class MyCursorLoaderHeader extends CursorLoader {

        public MyCursorLoaderHeader(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {

            return getDb()
                    .rawQuery(SQLQuery.queryOrdersHeaderFilesCsv("Orders._id  <> ?"), new String[]{"null"});
        }
    }

    /* создаем класс для выгрузки данных заказов табличной части из БД
              * загрузка происходит в фоне */
    private static class MyCursorLoaderLines extends CursorLoader {

        public MyCursorLoaderLines(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {

            return getDb()
                    .rawQuery(SQLQuery.queryOrdersLinesFilesCsv("OrdersLines.goods_id  <> ?"), new String[]{"null"});
        }
    }

    /* создаем класс для выгрузки данных считаем сколько стирок в двух таблицах
             * загрузка происходит в фоне */
    private static class MyCursorLoaderLinesAcuont extends CursorLoader {

        public MyCursorLoaderLinesAcuont(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {

            return getDb()
                    .rawQuery(SQLQuery.queryOrdersLinesAcuont(), null);
        }
    }
}
