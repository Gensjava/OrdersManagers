package ua.com.it_st.ordersmanagers.activiteies;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.fragmets.FilesLoadFragment;
import ua.com.it_st.ordersmanagers.fragmets.FilesUnloadFragment;
import ua.com.it_st.ordersmanagers.fragmets.MainPreferenceFragment;
import ua.com.it_st.ordersmanagers.fragmets.OrderListFragment;
import ua.com.it_st.ordersmanagers.fragmets.OrderNewCartFragment;
import ua.com.it_st.ordersmanagers.fragmets.OrderNewGoodsFragment;
import ua.com.it_st.ordersmanagers.fragmets.OrderNewHeaderFragment;
import ua.com.it_st.ordersmanagers.fragmets.OrderNewSelectHeaderFragment;
import ua.com.it_st.ordersmanagers.utils.ConstantsUtil;
import ua.com.it_st.ordersmanagers.utils.Dialogs;
import ua.com.it_st.ordersmanagers.utils.WorkFragment;

public class MainActivity extends AppCompatActivity

        implements
        OrderListFragment.onEventListener,
        OrderNewHeaderFragment.onEventListener,
        OrderNewGoodsFragment.onEventListener,
        OrderNewSelectHeaderFragment.OnFragmentSelectListener,
        OrderNewCartFragment.onEventListener,
        FilesLoadFragment.onEventListener

{

    public static boolean chickMainFragment;
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Set a Toolbar to replace the ActionBar.
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mToolbar.setNavigationIcon(R.mipmap.ic_drawer);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Find our drawer view
        NavigationView nvDrawerN = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawerN);
        ActionBarDrawerToggle drawerToggle = setupDrawerToggle();
        mDrawer.setDrawerListener(drawerToggle);

        chickMainFragment = true;//поумолчанию открывается ящик
        //Disables onClick toggle listener (onClick)

        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chickMainFragment) {
                    mDrawer.openDrawer(GravityCompat.START);
                } else {
                    onBackPressed();
                }
            }
        });

        /*вызываем менеджера настроек*/
        ConstantsUtil.mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        //Открываем главный фрагмент
        WorkFragment.onNewInstanceFragment(OrderListFragment.class, this);

    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on
        // position

        Class fragmentClass = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                fragmentClass = OrderListFragment.class;
                break;
            case R.id.nav_second_fragment:
                fragmentClass = FilesLoadFragment.class;
                break;
            case R.id.unload_files_fragment:
                fragmentClass = FilesUnloadFragment.class;
                break;
            case R.id.nav_third_setings:
                fragmentClass = MainPreferenceFragment.class;
                break;
            case R.id.nav_third_exit:
                finish();
                break;
            default:

        }
        if (fragmentClass != null) {
            //Открываем фрагмент
            WorkFragment.onNewInstanceFragment(fragmentClass, this);

            // Highlight the selected item, update the title, and close the drawer
            menuItem.setChecked(true);
            setTitle(menuItem.getTitle());
            mDrawer.closeDrawers();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public void setToolbar(final Toolbar toolbar) {
        mToolbar = toolbar;
    }

    @Override
    public void onOpenFragmentClass(final Class<?> fClass) {
        //Открываем фрагмент
        WorkFragment.onNewInstanceFragment(fClass, this);
    }

    @Override
    public void onOpenFragmentClassBundle(final Class<?> fClass, final Bundle bundleItem) {
        //Открываем фрагмент
        WorkFragment.onNewInstanceFragment(fClass, bundleItem, this);
    }

    @Override
    public void OnFragmentSelectListener(final String[] link) {

        OrderNewHeaderFragment fragment = (OrderNewHeaderFragment) getSupportFragmentManager().findFragmentByTag(OrderNewHeaderFragment.class.toString());
        if (fragment != null) {
            //Открываем фрагмент
            fragment.setSelectUpdate(link);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    /* текущий новый заказ */
        ConstantsUtil.mCurrentOrder = null;
    /* ТЧ заказа */
        ConstantsUtil.mCart.clear();
    }

    /*Метод для ввода количества в заказе*/
    public void OnClickNamber(View view) {

        switch (view.getId()) {
            case R.id.but_del:

                Dialogs.editNumber.setText(Dialogs.editNumber.getText().delete(
                        Dialogs.editNumber.getText().length() - 1,
                        Dialogs.editNumber.getText().length()));

                if (Dialogs.editNumber.getText().toString().trim().length() == 0) {
                    Dialogs.editNumber.setText("0");
                }

                break;
            case R.id.but_dumping:
                Dialogs.editNumber.setText("0");
                break;
            case R.id.but_point:

                if (!Dialogs.editNumber.getText().toString().contains(".")) {
                    Dialogs.editNumber.setText(Dialogs.editNumber.getText() + ".");
                }
                break;
            default:
                if (Dialogs.editNumber.getText().toString().equals("0")
                        || Dialogs.editNumber.getText().toString().equals("0.0")
                        || Dialogs.editNumber.getText().toString().trim().length() == 6) {
                    Dialogs.editNumber.setText(view.getContentDescription().toString());
                } else {
                    Dialogs.editNumber.setText(Dialogs.editNumber.getText() + view.getContentDescription().toString());
                }

                break;
        }
        Dialogs.calculationSum(Double.parseDouble(String.valueOf(Dialogs.editNumber.getText())), Dialogs.product.getPrice(), view, Dialogs.animScale);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        OrderListFragment mainFragment = (OrderListFragment) getSupportFragmentManager().findFragmentByTag(OrderListFragment.class.toString());

        if (checkFragment(mainFragment)) {
            chickMainFragment = true;
            mToolbar.setNavigationIcon(R.mipmap.ic_drawer);
        } else {
            chickMainFragment = false;
        }
    }

    /*делаем проверку на текущий фрагмент*/
    private boolean checkFragment(Fragment fragment) {
        boolean checkMain = false;

        if (fragment != null) {
            checkMain = fragment.isVisible();
        }
        return checkMain;
    }

    @Override
    public void onStart() {
        super.onStart();
        /*Запускаем монитор GPS*/
        // startService(new Intent(this, GPSMonitor.class));
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}