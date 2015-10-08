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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import java.util.TimerTask;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.enums.DocTypeOperation;
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
import ua.com.it_st.ordersmanagers.utils.InfoUtil;
import ua.com.it_st.ordersmanagers.utils.SQLiteOpenHelperUtil;


public class FilesFragment extends Fragment implements View.OnClickListener {

    private static SQLiteDatabase mDb;
    private final String TEG = FilesLoadFragment.class.getSimpleName();
    private SharedPreferences mSettings;
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

    /*делаем чтоб картинка мигала*/
    public void getFleshImage(final int icon, final int anim, final ImageView iv) {

        final Animation animScale = AnimationUtils.loadAnimation(getActivity(), anim);
        /*проверяем если уже работает то не запускаем*/
        if (animScale.hasStarted() & !animScale.hasEnded()) {
            return;
        }
       /*обнуляем таймер*/
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
         /*устанавливаем иконку*/
        iv.setImageResource(icon);

        //Вкл.таймер
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            //мигаем
                            iv.startAnimation(animScale);
                        }
                    });
                } else {
                  /*обнуляем таймер*/
                    if (mTimer != null) {
                        mTimer.cancel();
                        mTimer = null;
                    }
                }

            }
        }, 2000, 1000); //
    }

    /*обнуляем значения перед загрузкой*/
    public void nullableValues() {

        ConstantsUtil.nPieViewProgress = 0;
        ConstantsUtil.nPieViewdProgress = 0;

        mTextProgress.setText("0%");
        mProgressPieView.setProgress(0);
        mProgressPieView.setText("0%");
        mButtonOrderList.setVisibility(View.INVISIBLE);

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

    /* создаем класс - интефейс для открытия фрагментов */
    public interface onEventListener {
        void onOpenFragmentClass(Class<?> fClass);
    }

    /*клас для подключения к серверу 1с*/
    /* подключаемся через HTTP к базе и загужаем данные */
    public class ConnectServer {

        private boolean mlConnect;
        private AsyncHttpClientUtil mAsyncHttpClientUtil;

        public ConnectServer() {

            String loginServer = getMloginServer();
            String passwordServer = getPasswordServer();

            AsyncHttpClientUtil utilAsyncHttpClient = null;
            try {
                utilAsyncHttpClient = new AsyncHttpClientUtil((MainActivity) getActivity(), getIdServer());
                utilAsyncHttpClient.setBasicAuth(loginServer, passwordServer);
                utilAsyncHttpClient.setTimeout(30000);
                utilAsyncHttpClient.setConnectTimeout(30000);
                utilAsyncHttpClient.setResponseTimeout(30000);


                setMlConnect(true);
            } catch (java.net.SocketTimeoutException e) {

                try {
                    utilAsyncHttpClient = new AsyncHttpClientUtil((MainActivity) getActivity(), getIdServer());
                    utilAsyncHttpClient.setBasicAuth(loginServer, passwordServer);
                    utilAsyncHttpClient.setTimeout(30000);
                    utilAsyncHttpClient.setConnectTimeout(30000);
                    utilAsyncHttpClient.setResponseTimeout(30000);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    //Log
                    InfoUtil.setmLogLine(getString(R.string.action_conect_base), true, TEG + ": " + e.toString());
                }

                setMlConnect(true);

            } catch (Exception e) {
                e.printStackTrace();
                setMlConnect(false);
                //Log
                InfoUtil.setmLogLine(getString(R.string.action_conect_base), true, TEG + ": " + e.toString());
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

        public String getMloginServer() {

            String loginServer = mSettings.getString(getActivity().getString(R.string.login_server), null);
            /*проверка*/
            if (loginServer == null) {
                InfoUtil.Tost("Введите в настройках логин!", getActivity());
                //Log
                InfoUtil.setmLogLine("Введите в настройках логин!", true, TEG);
                return null;
            }
            return loginServer;
        }

        public String getPasswordServer() {

            String passwordServer = mSettings.getString(getActivity().getString(R.string.password_server), null);

            if (passwordServer == null) {
                InfoUtil.Tost("Введите в настройках пароль!", getActivity());
                //Log
                InfoUtil.setmLogLine("Введите в настройках пароль!", true, TEG);
                return null;
            }
            return passwordServer;
        }

        public String getModeServer() {
         /*режим сервера*/
            String modeServer = mSettings.getString(getActivity().getString(R.string.mode_server), null);

            if (modeServer == null) {
                InfoUtil.Tost("Установите в настройках режим сервера!", getActivity());
                //Log
                InfoUtil.setmLogLine("Установите в настройках режим сервера!", true, TEG);
                return null;
            }
            return modeServer;
        }

        public String getIdServer() {

        /* ид сервер удаленны или локальный*/
            final String idServer;
            if (getModeServer().equals(getString(R.string.remoteServer))) {
                idServer = mSettings.getString(getActivity().getString(R.string.id_remote), null);
            } else {
                idServer = mSettings.getString(getActivity().getString(R.string.id_local), null);
            }
            if (idServer == null || idServer.equals("")) {
                InfoUtil.Tost("Введите в настройках путь к серверу!", getActivity());
                //Log
                InfoUtil.setmLogLine("Введите в настройках путь к серверу!", true, TEG);
            }
            return idServer;
        }

        public String getWayCatalog() {

            /*каталог на сервере пользователя*/
            String wayCatalog = mSettings.getString(getActivity().getString(R.string.way_catalog), null);

            if (wayCatalog == null || wayCatalog.equals("")) {
                InfoUtil.Tost("Введите в настройках каталог пользователя!", getActivity());
                //Log
                InfoUtil.setmLogLine("Введите в настройках каталог пользователя!", true, TEG);
            }
            return wayCatalog;
        }
    }
}
