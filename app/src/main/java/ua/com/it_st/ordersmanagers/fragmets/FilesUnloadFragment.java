package ua.com.it_st.ordersmanagers.fragmets;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;

import com.loopj.android.http.RequestParams;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.utils.AsyncHttpClientUtil;
import ua.com.it_st.ordersmanagers.utils.ConnectServer;
import ua.com.it_st.ordersmanagers.utils.GlobalCursorLoader;
import ua.com.it_st.ordersmanagers.utils.InfoUtil;
import ua.com.it_st.ordersmanagers.utils.WorkFiles;
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
    private int fileSize;

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
                someEventListener.onOpenFragmentClass(OrderListDocFragment.class);
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
        WorkSharedPreferences lWorkSharedPreferences = new WorkSharedPreferences(getActivity());

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

        fileSize = WorkFiles.getFileUnloadName().size();

        for (byte i = 0; i < WorkFiles.getFileUnloadName().size(); i++) {
            getActivity().getSupportLoaderManager().initLoader(i, null, this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        /* выходим из загрузчкика*/
        for (byte i = 0; i < fileSize; i++) {
            getActivity().getSupportLoaderManager().destroyLoader(i);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {

        ArrayList objectList = WorkFiles.getFileUnloadName().get(id);

        String query = (String) objectList.get(3);
        String[] paramsQuery = (String[]) objectList.get(4);

        return new GlobalCursorLoader(getActivity(), query, paramsQuery, mDb);
    }

    /*формируем документ заказ шапку*/
    private void unloadFiles(final Cursor data, final String nameFile, final String[] headerLines, String headerName) {

        /*формируем документ заказ шапку*/
        if (data.getCount() > 0) {

            getProgressPieView().setMax(getProgressPieView().getMax() + data.getCount());
            /*информаци о выгрузке*/
            getLoadFiles().setText(getString(R.string.unload) + headerName);
            try {

                String path = createTempFiles(data, nameFile, headerLines);
                sendFileToServer(path, nameFile);

            } catch (Exception e) {
                error(nameFile, e);
            }
        }
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {

        ArrayList objectList = WorkFiles.getFileUnloadName().get(loader.getId());

        String nameFile = (String) objectList.get(0);
        String[] headerLines = (String[]) objectList.get(1);
        String headerName = (String) objectList.get(2);

        unloadFiles(data, nameFile, headerLines, headerName);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {

    }

    /*получаем все заказы (шапку заказов)*/
    private String createTempFiles(final Cursor itemCursor, String nameFile, final String[] headerLines) throws IOException {

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

        return path;
    }

    private void sendFileToServer(String path, String nameFile) {

        final RequestParams params = new RequestParams();

        try {
            params.put(mWayCatalog + "received\\" + nameFile, new File(path));
            mUtilAsyncHttpClient.postUnloadFiles(params, nameFile);
            //Log
            InfoUtil.setmLogLine(getString(R.string.unload_file), nameFile);
        } catch (Exception e) {
            e.printStackTrace();
            //Log
            error(nameFile, e);
        }
    }

    private void error(String nameFile, Exception e) {
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

}
