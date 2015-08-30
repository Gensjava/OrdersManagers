package ua.com.it_st.ordersmanagers.activiteies;

import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.fragmets.LoadFilesFragment;
import ua.com.it_st.ordersmanagers.fragmets.MainPreferenceFragment;
import ua.com.it_st.ordersmanagers.fragmets.OrderListFragment;
import ua.com.it_st.ordersmanagers.fragmets.OrderNewCartFragment;
import ua.com.it_st.ordersmanagers.fragmets.OrderNewGoodsFragment;
import ua.com.it_st.ordersmanagers.fragmets.OrderNewHeaderFragment;
import ua.com.it_st.ordersmanagers.fragmets.OrderNewSelectHeaderFragment;
import ua.com.it_st.ordersmanagers.utils.SQLiteOpenHelperUtil;
import ua.com.it_st.ordersmanagers.utils.WorkFragment;

public class MainActivity extends AppCompatActivity

        implements
        OrderListFragment.onEventListener,
        OrderNewHeaderFragment.onEventListener,
        OrderNewGoodsFragment.onEventListener,
        OrderNewSelectHeaderFragment.OnFragmentSelectListener,
        OrderNewCartFragment.onEventListener,
        LoadFilesFragment.onEventListener

{

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
        // mToolbar.setSubtitle("Sub");
        // mToolbar.setLogo(R.drawable.abc_btn_borderless_material);
        setSupportActionBar(mToolbar);

//        if (mToolbar != null) {
//            setSupportActionBar(mToolbar);
//            mToolbar.setNavigationIcon(R.drawable.ic_action_back);
//            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onBackPressed();
//                }
//            });
//        }


        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        // Set the menu icon instead of the launcher icon.

        // Find our drawer view
        NavigationView nvDrawerN = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawerN);
        ActionBarDrawerToggle drawerToggle = setupDrawerToggle();
        mDrawer.setDrawerListener(drawerToggle);

        //drawerToggle.setDrawerIndicatorEnabled(true);

        //final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.mipmap.ic_drawer);
        // ab.setDisplayHomeAsUpEnabled(true);

        //Открываем фрагмент
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
                fragmentClass = LoadFilesFragment.class;
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
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

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

        SQLiteDatabase DB = SQLiteOpenHelperUtil.getInstance().getDatabase();
        DB.close();

    }

}