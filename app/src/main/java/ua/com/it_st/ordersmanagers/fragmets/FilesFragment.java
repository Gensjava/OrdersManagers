package ua.com.it_st.ordersmanagers.fragmets;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.filippudak.ProgressPieView.ProgressPieView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Timer;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.enums.DocTypeOperation;
import ua.com.it_st.ordersmanagers.sqlTables.TableCompanies;
import ua.com.it_st.ordersmanagers.sqlTables.TableCounteragents;
import ua.com.it_st.ordersmanagers.sqlTables.TableCounteragentsDebt;
import ua.com.it_st.ordersmanagers.sqlTables.TableGoodsByStores;
import ua.com.it_st.ordersmanagers.sqlTables.TablePrices;
import ua.com.it_st.ordersmanagers.sqlTables.TableProducts;
import ua.com.it_st.ordersmanagers.sqlTables.TableTypePrices;
import ua.com.it_st.ordersmanagers.sqlTables.TableTypeStores;
import ua.com.it_st.ordersmanagers.utils.AsyncHttpClientUtil;
import ua.com.it_st.ordersmanagers.utils.ConstantsUtil;
import ua.com.it_st.ordersmanagers.utils.InfoUtil;
import ua.com.it_st.ordersmanagers.utils.SQLiteOpenHelperUtil;
import ua.com.it_st.ordersmanagers.utils.WorkSharedPreferences;


public class FilesFragment extends Fragment implements View.OnClickListener {

    private static SQLiteDatabase mDb;
    private final String TEG = FilesLoadFragment.class.getSimpleName();
    public SharedPreferences mSettings;
    private ProgressPieView mProgressPieView;
    private DiscreteSeekBar mDiscreteSeekBar;
    private ImageView mButtonOrderList;
    private TextView mTextProgress;
    private TextView mLoadFiles;
    private double mProgress;
    private double mProgressDiscrete;
    private int nOSeek;
    private Button BHost;
    private ImageView ImageViewInfo;
    private Timer mTimer;
    private View ui_bar;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {

        final MenuItem customBar = menu.add(0, R.id.menu_bar, 0, "");
        customBar.setActionView(R.layout.progress_bar);
        customBar.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        View menu_hotlistBar = menu.findItem(R.id.menu_bar).getActionView();

        ui_bar = menu_hotlistBar.findViewById(R.id.main_progress_bar);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.files_load, container,
                false);

        BHost = (Button) rootView.findViewById(R.id.load_files_button);
        BHost.setBackgroundResource(R.drawable.roundbutton);
        BHost.setOnClickListener(this);
        /*кнопка информации*/
        ImageViewInfo = (ImageView) rootView.findViewById(R.id.load_files_imageView);
        ImageViewInfo.setOnClickListener(this);
        /* открываем подключение к БД */
        mDb = SQLiteOpenHelperUtil.getInstance().getDatabase();
        mSettings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
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
        /*процент бара при обмене файлами */
        mTextProgress = (TextView) rootView.findViewById(R.id.load_files_text_progress);
            /*инфо при обмене*/
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
        ui_bar.setVisibility(View.VISIBLE);

        /*чистим док заказ и редактируем заказ*/
        ConstantsUtil.clearOrderHeader(DocTypeOperation.NEW);
        /* ТЧ заказа */
        ConstantsUtil.mCart = new LinkedHashSet<>();
       /*текущий номер заказа*/
        ConstantsUtil.sCurrentNumber = 0;
        //
        nOSeek = 0;
        mProgress = 0;
        /*чистим список для лога*/
        InfoUtil.mLogLineList.clear();
        /*чистим ошибок нет*/
        InfoUtil.isErrors = false;
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
        lTableNameInsert.put(TableCounteragentsDebt.FILE_NAME, TableCounteragentsDebt.INSERT_VALUES);

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
        lTableName.put(TableCounteragentsDebt.FILE_NAME, TableCounteragentsDebt.HEADER_NAME);

        return lTableName;
    }

    public String getTEG() {
        return TEG;
    }

    public ProgressPieView getProgressPieView() {
        return mProgressPieView;
    }

    public DiscreteSeekBar getDiscreteSeekBar() {
        return mDiscreteSeekBar;
    }

    public ImageView getButtonOrderList() {
        return mButtonOrderList;
    }

    public TextView getTextProgress() {
        return mTextProgress;
    }

    public TextView getLoadFiles() {
        return mLoadFiles;
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

    public Button getBHost() {
        return BHost;
    }

    public ImageView getImageViewInfo() {
        return ImageViewInfo;
    }

    public View getUi_bar() {
        return ui_bar;
    }

    public void setUi_bar(final View ui_bar) {
        this.ui_bar = ui_bar;
    }

    /* создаем класс - интефейс для открытия фрагментов */
    public interface onEventListener {
        void onOpenFragmentClass(Class<?> fClass);
    }

    /*клас для подключения к серверу 1с*/
    /* подключаемся через HTTP к базе и загужаем данные */
    public class ConnectServer {

        private boolean mlConnect;
        private AsyncHttpClientUtil mAsyncHttpClientUtil;
        private SharedPreferences mSettings;
        private Context mContext;

        public ConnectServer(Context context, byte tWay) {

            mContext = context;
             /*вызываем менеджера настроек*/
            mSettings = ConstantsUtil.mSettings;

            /*проверка есть интерент или нет*/
            boolean isInternet = ConstantsUtil.isInternetAvailable(mContext);
            if (!isInternet) {
                InfoUtil.showErrorAlertDialog(context.getString(R.string.error_inet), context.getString(R.string.updata), getActivity());
                return;
            }

            //класс работает с настройками программы
            WorkSharedPreferences lWorkSharedPreferences = new WorkSharedPreferences(mSettings, mContext);

              /* список шаблонов пути к серверу  */
            String[] templateWay = mContext.getResources().getStringArray(R.array.template_way);

            String loginServer = lWorkSharedPreferences.getMloginServer();
            String passwordServer = lWorkSharedPreferences.getPasswordServer();
            String IdServer = lWorkSharedPreferences.getIdServer();

            AsyncHttpClientUtil utilAsyncHttpClient = null;
            try {
                if (mContext.getClass().equals(MainActivity.class)) {//выгрузка загрузка файлов
                    utilAsyncHttpClient = new AsyncHttpClientUtil((MainActivity) mContext, IdServer + templateWay[tWay]);
                } else {
                    utilAsyncHttpClient = new AsyncHttpClientUtil(IdServer + templateWay[tWay]);//обмен данными GPS
                }

                utilAsyncHttpClient.setBasicAuth(loginServer, passwordServer);
                utilAsyncHttpClient.setTimeout(50000);
                utilAsyncHttpClient.setConnectTimeout(50000);
                utilAsyncHttpClient.setResponseTimeout(50000);

                setMlConnect(true);

            } catch (Exception e) {
                e.printStackTrace();
                setMlConnect(false);
                //Log
                InfoUtil.setmLogLine(mContext.getString(R.string.action_conect_base), true, TEG + ": " + e.toString());
            }
            setAsyncHttpClientUtil(utilAsyncHttpClient);
        }

        public AsyncHttpClientUtil getAsyncHttpClientUtil() {
            return mAsyncHttpClientUtil;
        }

        public void setAsyncHttpClientUtil(final AsyncHttpClientUtil asyncHttpClientUtil) {
            mAsyncHttpClientUtil = asyncHttpClientUtil;
        }

        public boolean isMlConnect() {
            return mlConnect;
        }

        public void setMlConnect(final boolean mlConnect) {
            this.mlConnect = mlConnect;
        }

    }
}
