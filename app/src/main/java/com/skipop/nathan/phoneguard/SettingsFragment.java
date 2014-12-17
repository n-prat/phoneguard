package com.skipop.nathan.phoneguard;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    final String tag = "PhoneGuard settings";
    public static final String KEY_ROOT = "setting_root";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        Log.i(tag, "preference changed: " + key);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());

        if (key.equals(KEY_ROOT)) {
            boolean state = prefs.getBoolean(KEY_ROOT, false);

            RootHandler rootHandler = new RootHandler(this.getActivity());
            rootHandler.checkRoot();

            if(state){
                rootHandler.checkRoot();
            }
        }
        /*if (key.equals(KEY_ROOT)) {

            Preference connectionPref = findPreference(key);

            // Set summary to be the user-description for the selected value
            connectionPref.setSummary(sharedPreferences.getString(key, ""));
        }*/
    }
}
