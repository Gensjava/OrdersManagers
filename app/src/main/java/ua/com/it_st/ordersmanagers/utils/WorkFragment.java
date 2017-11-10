package ua.com.it_st.ordersmanagers.utils;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;

public abstract class WorkFragment {

    public static void onNewInstanceFragment(final Class<?> fragmentClass, final Bundle bundleItem, final MainActivity context) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            fragment.setArguments(bundleItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onOpenFragment(fragment, context);
    }

    public static void onNewInstanceFragment(final Class<?> fragmentClass, final MainActivity context) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        onOpenFragment(fragment, context);
    }

    public static void showDialogFragment(Class<?> fragmentClass, final MainActivity context) {
        DialogFragment dialogFragment = getDialogFragment(fragmentClass);
        FragmentManager manager = context.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (dialogFragment != null) {
            dialogFragment.show(transaction, fragmentClass.getSimpleName());
        }
    }

    public static void showDialogFragment(Class<?> fragmentClass, final Bundle bundleItem, final MainActivity context) {
        DialogFragment dialogFragment = getDialogFragment(fragmentClass);
        FragmentManager manager = context.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (dialogFragment != null) {
            dialogFragment.setArguments(bundleItem);
            dialogFragment.show(transaction, fragmentClass.getSimpleName());
        }
    }

    private static DialogFragment getDialogFragment(Class<?> fragmentClass) {
        DialogFragment dialogFragment = null;
        try {
            dialogFragment = (DialogFragment) fragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return dialogFragment;
    }

    private static void onOpenFragment(final Fragment fragment, MainActivity context) {
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
