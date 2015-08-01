package ua.com.it_st.ordersmanagers.activiteies;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import ua.com.it_st.ordersmanagers.BlankFragment;
import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.fragmets.ExchangeFragment;
import ua.com.it_st.ordersmanagers.fragmets.MainFragment;
import ua.com.it_st.ordersmanagers.fragmets.OrderNewHeaderFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.onEventListener {
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        Fragment fragment = null;

        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                fragmentClass = MainFragment.class;
                break;
            case R.id.nav_second_fragment:
                fragmentClass = ExchangeFragment.class;
                break;
            case R.id.nav_third_fragment:
                fragmentClass = BlankFragment.class;
                break;
            default:
                fragmentClass = BlankFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
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
    public void someEvent(final String tagAction) {

        Fragment fragment = null;
        Class fragmentClass = null;

        switch (tagAction) {
            case MainFragment.PLUS_ORDER:
                fragmentClass = OrderNewHeaderFragment.class;
                break;
            default:
                break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        onOpenFragment(fragment);
    }

    void onOpenFragment(Fragment fragment) {

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            ft.addToBackStack(fragment.getClass().toString());

            ft.replace(R.id.flContent, fragment, fragment.getClass().toString());
            ft.commit();
        } else {
            // Error
            Log.e(this.getClass().getName(), "Error. Fragment is not created");
        }
    }
}