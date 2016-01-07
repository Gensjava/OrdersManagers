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
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.sqlTables.TableOrders;
import ua.com.it_st.ordersmanagers.sqlTables.TableOrdersLines;
import ua.com.it_st.ordersmanagers.utils.AsyncHttpClientUtil;
import ua.com.it_st.ordersmanagers.utils.ConnectServer;
import ua.com.it_st.ordersmanagers.utils.InfoUtil;
import ua.com.it_st.ordersmanagers.utils.SQLQuery;
import ua.com.it_st.ordersmanagers.utils.WorkSharedPreferences;

/* Класс предназначен для отправки данных (файлы в формате csv) на сервер*/
public class FilesUnloadFragment extends FilesFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String TEG = FilesUnloadFragment.class.getSimpleName();
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
        getBHost().setText(R.string.btUnLoad);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.load_files_button:
               /* выгружаем файлы на сервер*/
                UnloadFilesOfServer();
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
    /* выгружаем файлы на сервер*/
    public void UnloadFilesOfServer() {
       /*чистим все параметры */
        nullableValues();
        mProgress = 0;
        pProgressPie = 0;
        //Log
        InfoUtil.setmLogLine(getString(R.string.start_on_load));
                /*подключаемся к серверу*/
        ConnectServer connectData = new ConnectServer(getActivity(), (byte) 0);
        //класс работает с настройками программы
        WorkSharedPreferences lWorkSharedPreferences = new WorkSharedPreferences(mSettings, getActivity());

                /*подключились к базе или нет*/
        boolean lConnect = connectData.isMlConnect();
        if (!lConnect) {
            //Log
            InfoUtil.setmLogLine(getString(R.string.action_conect_base), true, TEG + getString(R.string.error_login_password_inet));
            InfoUtil.getFleshImage(R.mipmap.ic_info_red, R.anim.scale_image, getImageViewInfo(), (MainActivity) getActivity());
            getUi_bar().setVisibility(View.INVISIBLE);
            return;
        }

        mUtilAsyncHttpClient = connectData.getAsyncHttpClientUtil();
        mWayCatalog = lWorkSharedPreferences.getWayCatalog();

        for (byte i = 0; i < 3; i++) {
            getActivity().getSupportLoaderManager().destroyLoader(i);
            getActivity().getSupportLoaderManager().initLoader(i, null, this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        /* выходим из загрузчкика*/
        for (byte i = 0; i < 3; i++) {
            getActivity().getSupportLoaderManager().destroyLoader(i);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        switch (id) {
            case 0:
                return new MyCursorLoaderLinesAmount(getActivity());
            case 1:
                return new MyCursorLoaderHeader(getActivity());
            case 2:
                return new MyCursorLoaderLines(getActivity());
            default:
                return null;
        }
    }

    /*получаем количество строк двух таблиц*/
    private void getCountLineTamle(final Cursor data) {

        data.moveToNext();
                /*получаем количество строк двух таблиц*/
        mAcuont = data.getInt(data.getColumnIndex(getString(R.string.sum)));
        if (mAcuont > 0) {
                    /*устанавливаем максимальное количество бара*/
            getProgressPieView().setMax(mAcuont);
        } else {
            InfoUtil.setmLogLine(getString(R.string.unload_file), getString(R.string.order_unload_no), true, getTEG());
                    /*информаци о выгрузке*/
            getLoadFiles().setText(getString(R.string.order_unload_no));
            getUi_bar().setVisibility(View.INVISIBLE);
                    /*мигаем иконкой для вывода лога*/
            if (InfoUtil.isErrors) {
                InfoUtil.getFleshImage(R.mipmap.ic_info_red, R.anim.scale_image, getImageViewInfo(), (MainActivity) getActivity());
            } else {
                InfoUtil.getFleshImage(R.mipmap.ic_info_ok, R.anim.scale_image, getImageViewInfo(), (MainActivity) getActivity());
            }
        }
    }

    /*формируем документ заказ шапку*/
    private void NewCreateDocOrder(final Cursor data, final String nameFile, final String[] headerLines, String headerName) {

        /*формируем документ заказ шапку*/
        if (mAcuont > 0) {
            /*информаци о выгрузке*/
            getLoadFiles().setText(getString(R.string.unload) + headerName);
            try {
                getAllOrdersHeaderLines(data, nameFile, headerLines);
                //Log
                InfoUtil.setmLogLine(getString(R.string.unload_file), nameFile);
            } catch (IOException e) {
                e.printStackTrace();
                //Log
                InfoUtil.setmLogLine(getString(R.string.unload_file), nameFile, true, getTEG() + " " + e.toString());
                         /*информаци о выгрузке*/
                getLoadFiles().setText(R.string.unload_eror);
                getUi_bar().setVisibility(View.INVISIBLE);
                        /*мигаем иконкой для вывода лога*/
                if (InfoUtil.isErrors) {
                    InfoUtil.getFleshImage(R.mipmap.ic_info_red, R.anim.scale_image, getImageViewInfo(), (MainActivity) getActivity());
                } else {
                    InfoUtil.getFleshImage(R.mipmap.ic_info_ok, R.anim.scale_image, getImageViewInfo(), (MainActivity) getActivity());
                }
            }
        }
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {

        final String nameFile;
        final String[] headerLines;
        final String headerName;

        switch (loader.getId()) {
            case 0:
                getCountLineTamle(data);
                break;
            case 1:
                nameFile = TableOrders.FILE_NAME;
                headerLines = TableOrders.sHeader.split(",");
                headerName = TableOrders.HEADER_NAME;

                NewCreateDocOrder(data, nameFile, headerLines, headerName);
                break;
            case 2:

                nameFile = TableOrdersLines.FILE_NAME;
                headerLines = TableOrdersLines.sHeader.split(",");
                headerName = TableOrdersLines.HEADER_NAME;

                NewCreateDocOrder(data, nameFile, headerLines, headerName);

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
            //Log
            InfoUtil.setmLogLine(getString(R.string.unload_file), nameFile, true, getTEG() + " " + e.toString());
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
            params.put(mWayCatalog + "received\\" + nameFile, new File(path));
            mUtilAsyncHttpClient.postUnloadFiles(params, nameFile);
        } catch (Exception e) {
            e.printStackTrace();
            //Log
            InfoUtil.setmLogLine(getString(R.string.unload_file), nameFile, true, getTEG() + " " + e.toString());
                        /*информаци о выгрузке*/
            getLoadFiles().setText(getString(R.string.unload_eror));
            getUi_bar().setVisibility(View.INVISIBLE);
            /*мигаем иконкой для вывода лога*/
            if (InfoUtil.isErrors) {
                InfoUtil.getFleshImage(R.mipmap.ic_info_red, R.anim.scale_image, getImageViewInfo(), (MainActivity) getActivity());
            } else {
                InfoUtil.getFleshImage(R.mipmap.ic_info_ok, R.anim.scale_image, getImageViewInfo(), (MainActivity) getActivity());
            }
        }
    }

    /*двигаем бар и вычисляем процент выполнения */
    private void onProgressBar(int DprogressSeekBar, final int sizeBar) {

        getDiscreteSeekBar().setProgress(DprogressSeekBar);
        pProgressDiscrete += (100 / (double) sizeBar);
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
                    .rawQuery(SQLQuery.queryOrdersHeaderFilesCsv("Orders.type  <> ?"), new String[]{"NO_HELD"});
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
                    .rawQuery(SQLQuery.queryOrdersLinesFilesCsv("Orders.type  <> ?"), new String[]{"NO_HELD"});
        }
    }

    /* создаем класс для выгрузки данных считаем сколько стирок в двух таблицах
             * загрузка происходит в фоне */
    private static class MyCursorLoaderLinesAmount extends CursorLoader {

        public MyCursorLoaderLinesAmount(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {

            return getDb()
                    .rawQuery(SQLQuery.queryOrdersLinesAmount(), null);
        }
    }
}
