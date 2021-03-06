package ua.com.it_st.ordersmanagers.activiteies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import ua.com.it_st.ordersmanagers.Adapters.SelectPayDocOrdersAdapter;
import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.fragmets.FilesLoadFragment;
import ua.com.it_st.ordersmanagers.fragmets.FilesUnloadFragment;
import ua.com.it_st.ordersmanagers.fragmets.HeaderDoc;
import ua.com.it_st.ordersmanagers.fragmets.LoaderDocFragment;
import ua.com.it_st.ordersmanagers.fragmets.MainPreferenceFragment;
import ua.com.it_st.ordersmanagers.fragmets.OrderCartFragment;
import ua.com.it_st.ordersmanagers.fragmets.OrderCatalogGoodsFragment;
import ua.com.it_st.ordersmanagers.fragmets.OrderHeaderDoc;
import ua.com.it_st.ordersmanagers.fragmets.OrderListDocFragment;
import ua.com.it_st.ordersmanagers.fragmets.OrderSelectHeaderFragment;
import ua.com.it_st.ordersmanagers.fragmets.PayDocListDocFragment;
import ua.com.it_st.ordersmanagers.fragmets.PayDocSelectOrders;
import ua.com.it_st.ordersmanagers.fragmets.dialogs.DetailsPays;
import ua.com.it_st.ordersmanagers.interfaces.implems.OrderListAction;
import ua.com.it_st.ordersmanagers.models.Catalogs;
import ua.com.it_st.ordersmanagers.models.Orders;
import ua.com.it_st.ordersmanagers.models.Pays;
import ua.com.it_st.ordersmanagers.services.GPSMonitor;
import ua.com.it_st.ordersmanagers.utils.Dialogs;
import ua.com.it_st.ordersmanagers.utils.WorkFragment;

public class MainActivity extends AppCompatActivity implements
        LoaderDocFragment.onLoaderDocListener,
        OrderHeaderDoc.onEventListener,
        OrderCatalogGoodsFragment.onEventListener,
        OrderSelectHeaderFragment.OnFragmentSelectListener,
        OrderCartFragment.onEventListener,
        FilesLoadFragment.onEventListener,
        SelectPayDocOrdersAdapter.OnItemClickListener,
        DetailsPays.OnDetailsPaysFragmentListener {

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_COUNTER = "counter";
    public static boolean chickMainFragment;
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    private Orders mCurrentOrder;
    private Pays mCurrentPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        int mCounter = mSettings.getInt(APP_PREFERENCES_COUNTER, 0);

        if (mCounter == 0) {
            deleteDatabase("db_courier_orders.db");

            SharedPreferences.Editor editor = mSettings.edit();
            editor.putInt(APP_PREFERENCES_COUNTER, 1);
            editor.apply();
        }

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

        //Открываем главный фрагмент
        WorkFragment.onNewInstanceFragment(OrderListDocFragment.class, this);
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
                fragmentClass = OrderListDocFragment.class;
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
            case R.id.nav_pay_doc_fragment:
                fragmentClass = PayDocListDocFragment.class;
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
    protected void onDestroy() {
        super.onDestroy();
    /* текущий новый заказ */
        mCurrentOrder = null;
    /* ТЧ заказа */
        OrderListAction.mCart.clear();
    }

    /*Метод для ввода количества в заказе*/
    public void OnClickNumber(View view) {

        switch (view.getId()) {
            case R.id.but_del:

                Dialogs.editNumber.setText(Dialogs.editNumber.getText().delete(
                        Dialogs.editNumber.getText().length() - 1,
                        Dialogs.editNumber.getText().length()));

                if (Dialogs.editNumber.getText().toString().trim().length() == 0) {
                    Dialogs.editNumber.setText(R.string.zero_text);
                }
                break;
            case R.id.but_dumping:
                Dialogs.editNumber.setText(R.string.zero_text);
                break;
            case R.id.but_point:

                if (!Dialogs.editNumber.getText().toString().contains(getString(R.string.point))) {
                    Dialogs.editNumber.setText(Dialogs.editNumber.getText() + getString(R.string.point));
                }
                break;
            default:
                if (Dialogs.editNumber.getText().toString().equals(R.string.zero_text)
                        || Dialogs.editNumber.getText().toString().equals(getString(R.string.zero_point_text))
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
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            /*Запускаем монитор GPS*/
            startService(new Intent(getApplicationContext(), GPSMonitor.class));
        } catch (Exception e) {

        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void OnFragmentSelectListener(Catalogs link, String nameTegFragment, String id) {
        HeaderDoc fragment = (HeaderDoc) getSupportFragmentManager().findFragmentByTag(nameTegFragment);
        if (fragment != null) {
            fragment.setSelectUpdate(link, id);
        }
    }

    public Orders getmCurrentOrder() {
        return mCurrentOrder;
    }

    public void setmCurrentOrder(Orders mCurrentOrder) {
        this.mCurrentOrder = mCurrentOrder;
    }

    public Pays getmCurrentPay() {
        return mCurrentPay;
    }

    public void setmCurrentPay(Pays mCurrentPay) {
        this.mCurrentPay = mCurrentPay;
    }

    @Override
    public void onItemClick(Bundle bundle) {
        WorkFragment.showDialogFragment(DetailsPays.class, bundle, this);
    }

    @Override
    public void onDetailsPaysOKListener() {
        PayDocSelectOrders fragment = (PayDocSelectOrders) getSupportFragmentManager().findFragmentByTag(PayDocSelectOrders.class.toString());
        if (fragment != null) {
            fragment.updateAdapter();
        }
    }
}