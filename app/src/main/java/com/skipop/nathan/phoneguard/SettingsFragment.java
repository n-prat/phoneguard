package com.skipop.nathan.phoneguard;


import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String KEY_ROOT = "setting_root";

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
        String tag = "PhoneGuard settings";
        Log.i(tag, "preference changed: " + key);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());

        if (key.equals(KEY_ROOT)) {
            boolean state = prefs.getBoolean(KEY_ROOT, false);

            RootHandler rootHandler = new RootHandler(this.getActivity());

            if(state){
                //checkRoot will ask for root if necessary(su prompt)
                //boolean isRooted = rootHandler.checkRoot();
                boolean isRooted = rootHandler.checkRootv2();

                //we set the checkbox state to the result of checkRoot
                CheckBoxPreference checkboxRoot = (CheckBoxPreference) findPreference(KEY_ROOT);
                checkboxRoot.setChecked(isRooted);
            }
        }
    }
}
