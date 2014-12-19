package com.skipop.nathan.phoneguard;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

//TODO compat?
//public class SettingsActivity extends PreferenceActivity {
public class SettingsActivity extends Activity {
    private final String tag = "PhoneGuard SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
        /*
        if (Build.VERSION.SDK_INT > 10) {
            //Honeycomb+
            Log.d(tag, "Honeycomb+ mode");

            // Display the fragment as the main content.
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new SettingsFragment())
                    .commit();
        } else {
            Log.d(tag, "compatibility");
            //TODO not working
            addPreferencesFromResource(R.xml.preferences);
        }
        */
    }
} //end of class

