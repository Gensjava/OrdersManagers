package ua.com.it_st.ordersmanagers.fragmets;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.filippudak.ProgressPieView.ProgressPieView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.apache.http.auth.AuthScope;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.models.OrderDoc;
import ua.com.it_st.ordersmanagers.sqlTables.TableCompanies;
import ua.com.it_st.ordersmanagers.sqlTables.TableCounteragents;
import ua.com.it_st.ordersmanagers.sqlTables.TableGoodsByStores;
import ua.com.it_st.ordersmanagers.sqlTables.TablePrices;
import ua.com.it_st.ordersmanagers.sqlTables.TableProducts;
import ua.com.it_st.ordersmanagers.sqlTables.TableTypePrices;
import ua.com.it_st.ordersmanagers.sqlTables.TableTypeStores;
import ua.com.it_st.ordersmanagers.utils.AsyncHttpClientUtil;
import ua.com.it_st.ordersmanagers.utils.ConstantsUtil;
import ua.com.it_st.ordersmanagers.utils.ErrorInfo;
import ua.com.it_st.ordersmanagers.utils.SQLiteOpenHelperUtil;


public class FilesFragment extends Fragment implements View.OnClickListener {

    private static SQLiteDatabase mDb;
    private final String TEG = LoadFilesFragment.class.getSimpleName();
    private SharedPreferences mSettings;
    private ProgressPieView mProgressPieView;
    private DiscreteSeekBar mDiscreteSeekBar;
    private ImageView mButtonOrderList;
    private TextView mTextProgress;
    private TextView mLoadFiles;
    private double mProgress;
    private double mProgressDiscrete;
    private int nOSeek;
    private int AcountNameFile;
    private Button BHost;

    protected static SQLiteDatabase getDb() {
        return mDb;
    }

    public void setDb(final SQLiteDatabase db) {
        mDb = db;
    }

    public static String getDataDir(Context context) throws Exception {
        return context.getPackageManager()
                .getPackageInfo(context.getPackageName(), 0)
                .applicationInfo.dataDir;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.load_files, container,
                false);

        BHost = (Button) rootView.findViewById(R.id.load_files_button);
        BHost.setBackgroundResource(R.drawable.roundbutton);
        BHost.setOnClickListener(this);
        /* открываем подключение к БД */
        mDb = SQLiteOpenHelperUtil.getInstance().getDatabase();
        /*вызываем менеджера настроек*/
        mSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        /*Круглый общий прогресс загрузки*/
        mProgressPieView = (ProgressPieView) rootView.findViewById(R.id.load_files_progressPieView);
        mProgressPieView.setText("0%");
        mProgressPieView.setTextColor(getResources().getColor(R.color.main_sub_grey));
        mProgressPieView.setProgressColor(getResources().getColor(R.color.main_grey));
        mProgressPieView.setStrokeColor(getResources().getColor(R.color.main_grey));
        /*прогресс каждого файла*/
        mDiscreteSeekBar = (DiscreteSeekBar) rootView.findViewById(R.id.load_files_seekBar);
        /**/
        mButtonOrderList = (ImageView) rootView.findViewById(R.id.load_files_image_button_n);
        mButtonOrderList.setOnClickListener(this);
        /**/
        mTextProgress = (TextView) rootView.findViewById(R.id.load_files_text_progress);
            /**/
        mLoadFiles = (TextView) rootView.findViewById(R.id.load_files_text);

        return rootView;
    }

    @Override
    public void onClick(final View v) {

    }

    /*обнуляем значения перед загрузкой*/
    public void nullableValues() {

        ConstantsUtil.nPieViewProgress = 0;
        ConstantsUtil.nPieViewdProgress = 0;

        mTextProgress.setText("0%");
        mProgressPieView.setProgress(0);
        mProgressPieView.setText("0%");
        mButtonOrderList.setVisibility(View.INVISIBLE);

        /* текущий новый заказ */
        ConstantsUtil.mCurrentOrder = new OrderDoc();
        /* ТЧ заказа */
        ConstantsUtil.mCart = new LinkedHashSet<>();
       /*текущий номер заказа*/
        ConstantsUtil.sCurrentNumber = 0;
       /*режим заказа новый или не новый*/
        ConstantsUtil.modeNewOrder = true;
        //
        nOSeek = 0;
        mProgress = 0;
    }

    /*подключаемся к серверу*/
    /* подключаемся через HTTP к базе и загужаем данные */
    public Object[] connectServer() {

        Object[] data = new Object[6];

        AsyncHttpClientUtil utilAsyncHttpClient = null;
        boolean lConnect;
        /*данные настроек*/
        String loginServer = mSettings.getString(getActivity().getString(R.string.login_server), null);
        String passwordServer = mSettings.getString(getActivity().getString(R.string.password_server), null);
        /*проверка*/
        if (loginServer == null) {
            ErrorInfo.Tost("Введите логин!", getActivity());
            return null;
        }
        if (passwordServer == null) {
            ErrorInfo.Tost("Введите пароль!", getActivity());
            return null;
        }
        /*режим сервера*/
        String modeServer = mSettings.getString(getActivity().getString(R.string.mode_server), null);
        /* ид сервер удаленны или локальный*/
        final String idServer;
        if (modeServer.equals(getString(R.string.remoteServer))) {
            idServer = mSettings.getString(getActivity().getString(R.string.id_remote), null);
        } else {
            idServer = mSettings.getString(getActivity().getString(R.string.id_local), null);
        }
        /*каталог на сервере пользователя*/
        String wayCatalog = mSettings.getString(getActivity().getString(R.string.way_catalog), null);

        try {
            utilAsyncHttpClient = new AsyncHttpClientUtil((MainActivity) getActivity(), idServer);
            utilAsyncHttpClient.setBasicAuth(loginServer, passwordServer, AuthScope.ANY);
            utilAsyncHttpClient.setMaxRetriesAndTimeout(3, 5);
            lConnect = true;
        } catch (Exception e) {
            e.printStackTrace();
            lConnect = false;
            //Log
            ErrorInfo.setmLogLine(getString(R.string.action_conect_base), true, TEG + ": " + e.toString());
        }
        data[0] = lConnect;
        data[1] = utilAsyncHttpClient;
        data[2] = loginServer;
        data[3] = passwordServer;
        data[4] = wayCatalog;
        data[5] = idServer;

        return data;
    }

    /*получаем кол-во строк в файле*/
    public int getCountFileLines(File mFile) {
        int n = 0;
        BufferedReader input = null;
        try {
            input = new BufferedReader(new FileReader(mFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            input.readLine();
            while ((input.readLine()) != null) {
                n++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return n;
    }

    public Map<String, String> getFileNameInsert() {

        Map<String, String> lTableNameInsert = new HashMap<>();

        lTableNameInsert.put(TableCompanies.FILE_NAME, TableCompanies.INSERT_VALUES);
        lTableNameInsert.put(TableCounteragents.FILE_NAME, TableCounteragents.INSERT_VALUES);
        lTableNameInsert.put(TablePrices.FILE_NAME, TablePrices.INSERT_VALUES);
        lTableNameInsert.put(TableProducts.FILE_NAME, TableProducts.INSERT_VALUES);
        lTableNameInsert.put(TableTypePrices.FILE_NAME, TableTypePrices.INSERT_VALUES);
        lTableNameInsert.put(TableTypeStores.FILE_NAME, TableTypeStores.INSERT_VALUES);
        lTableNameInsert.put(TableGoodsByStores.FILE_NAME, TableGoodsByStores.INSERT_VALUES);

        return lTableNameInsert;
    }

    public Map<String, String> getFileNameHeader() {

        Map<String, String> lTableName = new HashMap<>();

        lTableName.put(TableCompanies.FILE_NAME, TableCompanies.HEADER_NAME);
        lTableName.put(TableCounteragents.FILE_NAME, TableCounteragents.HEADER_NAME);
        lTableName.put(TablePrices.FILE_NAME, TablePrices.HEADER_NAME);
        lTableName.put(TableProducts.FILE_NAME, TableProducts.HEADER_NAME);
        lTableName.put(TableTypePrices.FILE_NAME, TableTypePrices.HEADER_NAME);
        lTableName.put(TableTypeStores.FILE_NAME, TableTypeStores.HEADER_NAME);
        lTableName.put(TableGoodsByStores.FILE_NAME, TableGoodsByStores.HEADER_NAME);

        return lTableName;
    }

    public String getTEG() {
        return TEG;
    }

    public SharedPreferences getSettings() {
        return mSettings;
    }

    public void setSettings(final SharedPreferences settings) {
        mSettings = settings;
    }

    public ProgressPieView getProgressPieView() {
        return mProgressPieView;
    }

    public void setProgressPieView(final ProgressPieView progressPieView) {
        mProgressPieView = progressPieView;
    }

    public DiscreteSeekBar getDiscreteSeekBar() {
        return mDiscreteSeekBar;
    }

    public void setDiscreteSeekBar(final DiscreteSeekBar discreteSeekBar) {
        mDiscreteSeekBar = discreteSeekBar;
    }

    public ImageView getButtonOrderList() {
        return mButtonOrderList;
    }

    public void setButtonOrderList(final ImageView buttonOrderList) {
        mButtonOrderList = buttonOrderList;
    }

    public TextView getTextProgress() {
        return mTextProgress;
    }

    public void setTextProgress(final TextView textProgress) {
        mTextProgress = textProgress;
    }

    public TextView getLoadFiles() {
        return mLoadFiles;
    }

    public void setLoadFiles(final TextView loadFiles) {
        mLoadFiles = loadFiles;
    }

    public double getProgress() {
        return mProgress;
    }

    public void setProgress(final double progress) {
        mProgress = progress;
    }

    public double getProgressDiscrete() {
        return mProgressDiscrete;
    }

    public void setProgressDiscrete(final double progressDiscrete) {
        mProgressDiscrete = progressDiscrete;
    }

    public int getnOSeek() {
        return nOSeek;
    }

    public void setnOSeek(final int nOSeek) {
        this.nOSeek = nOSeek;
    }

    public int getAcountNameFile() {
        return AcountNameFile;
    }

    public void setAcountNameFile(final int acountNameFile) {
        AcountNameFile = acountNameFile;
    }

    public Button getBHost() {
        return BHost;
    }

    public void setBHost(final Button BHost) {
        this.BHost = BHost;
    }

    /* создаем класс - интефейс для открытия фрагментов */
    public interface onEventListener {
        void onOpenFragmentClass(Class<?> fClass);
    }
}
