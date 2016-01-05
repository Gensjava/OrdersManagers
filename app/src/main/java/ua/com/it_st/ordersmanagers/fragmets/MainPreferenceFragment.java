package ua.com.it_st.ordersmanagers.fragmets;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.github.machinarius.preferencefragment.PreferenceFragment;

import ua.com.it_st.ordersmanagers.R;


public class MainPreferenceFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
    private SharedPreferences mSettings;

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {

    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        mSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // Регистрируем этот OnSharedPreferenceChangeListener
        Context context = getActivity().getApplicationContext();
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(context);
        prefs.registerOnSharedPreferenceChangeListener(this);

    }
}
