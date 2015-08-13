package ua.com.it_st.ordersmanagers.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;

public class WorkFragment {

    public static void onNewInstanceFragment(final Class<?> fragmentClass, final Bundle bundleItem, final MainActivity context) {

        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            fragment.setArguments(bundleItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        onOpenFragment(fragment, context);
    }

    public static void onNewInstanceFragment(final Class<?> fragmentClass, final MainActivity context) {

        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        onOpenFragment(fragment, context);
    }

    public static void onOpenFragment(final Fragment fragment, MainActivity context) {

        if (fragment != null) {
            FragmentManager fragmentManager = context.getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            ft.addToBackStack(fragment.getClass().toString());

            ft.replace(R.id.flContent, fragment, fragment.getClass().toString());
            ft.commit();
        } else {
            // Error
            Log.e(context.getClass().getName(), "Error. Fragment is not created");
        }
    }

}
