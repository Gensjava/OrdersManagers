package ua.com.it_st.ordersmanagers.fragmets;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

import java.util.LinkedHashSet;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.interfaces.implems.OrderListAction;
import ua.com.it_st.ordersmanagers.utils.ConstantsUtil;
import ua.com.it_st.ordersmanagers.utils.InfoUtil;
import ua.com.it_st.ordersmanagers.utils.SQLiteOpenHelperUtil;


public class FilesFragment extends Fragment implements View.OnClickListener {

    private static SQLiteDatabase mDb;
    private final String TEG = FilesLoadFragment.class.getSimpleName();
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
        /*Круглый общий прогресс загрузки*/
        mProgressPieView = (ProgressPieView) rootView.findViewById(R.id.load_files_progressPieView);
        mProgressPieView.setText(getString(R.string.zero_procent));
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

        mTextProgress.setText(getString(R.string.zero_procent));
        mProgressPieView.setProgress(0);
        mProgressPieView.setText(getString(R.string.zero_procent));
        mButtonOrderList.setVisibility(View.INVISIBLE);
        ui_bar.setVisibility(View.VISIBLE);

        /*чистим док заказ и редактируем док*/
        ((MainActivity) getActivity()).setmCurrentOrder(null);
        /* ТЧ заказа */
        OrderListAction.mCart = new LinkedHashSet<>();
        //
        nOSeek = 0;
        mProgress = 0;
        /*чистим список для лога*/
        InfoUtil.mLogLineList.clear();
        /*чистим ошибок нет*/
        InfoUtil.isErrors = false;
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

}
