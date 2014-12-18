package com.skipop.nathan.phoneguard;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ToggleButton;

/**
 * Created by nathan on 12/15/14.
 * Nathan Prat
 */
public class MainActivity extends ActionBarActivity {
    private final String tag = "PhoneGuard Main";

    //TODO receive boot event
    //TODO-> if security was on before : restart service
    //TODO-> if security and new sim : send new number to old one
    //TODO implement service?
    //TODO (start when security is enabled, restart on boot if necessary)
    //TODO (stop only when phone is found again(unlock, security off message?)

    //TODO on sim card change -> send new number to the old on

    //TODO add PhotoHandler -> can take photo without sound (upload?)

    //TODO implement location services

    //TODO disclaimer on start

    //TODO device admin -> add setting

    //TODO settings to control data, send sms, keyword, photo, email...

    //TODO store a list oh authenticated numberS
    //TODO store those numbers in a file(allows to survive reboot, sim change, etc)

    //TODO geofencing :-> restart tracking when moving
    //TODO idem with Activity detection

    //TODO display notification while security is on?

    //TODO store auth numbers + security status etc in SharedPrefs

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(tag, "onDestroy ");
        //rootHandler.eraseOldApk();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(tag, "onStop ");
        //rootHandler.eraseOldApk();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(tag, "onCreate: ");

        //initialization
        SecurityManager securityManager = new SecurityManager(MainActivity.this);
        ConnectionManager connectionManager = new ConnectionManager(MainActivity.this);


        // Properly set default values upon first launch
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        setContentView(R.layout.activity_main);

        securityManager.isSecurityActivated();

        //set data toggle to the correct value
        ToggleButton toggleData = (ToggleButton) findViewById(R.id.togglebuttondata);
        int dataState = connectionManager.getDataState();
        boolean dataConnectedOrConnecting = (dataState == 1 || dataState == 2);
        toggleData.setChecked(dataConnectedOrConnecting);

        //set wifi toggle to the correct value
        ToggleButton toggleWifi = (ToggleButton) findViewById(R.id.togglebuttonwifi);
        toggleWifi.setChecked(connectionManager.checkWifiState());

        //RootHandler rootHandler = new RootHandler(MainActivity.this);
        //rootHandler.installToSystem();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(tag, "onOptionsItemSelected ");
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(myIntent);
                return true;
            case R.id.help:
                Intent myIntent2 = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(myIntent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onToggleWifiClicked(View v) {
        Log.d(tag, "onToggleWifiClicked ");
        ToggleButton toggle = (ToggleButton) findViewById(R.id.togglebuttonwifi);

        // Is the toggle on?
        boolean on = toggle.isChecked();
        ConnectionManager connectionManager= new ConnectionManager(MainActivity.this);
        //w ConnectionManager(this.getApplicationContext());

        connectionManager.setWifiState(on);
    }

    public void onToggleDataClicked(View v) {
        Log.d(tag, "onToggleDataClicked ");
        ToggleButton toggle = (ToggleButton) findViewById(R.id.togglebuttondata);

        // Is the toggle on?
        boolean on = toggle.isChecked();
        ConnectionManager connectionManager= new ConnectionManager(MainActivity.this);
        if(on)
            connectionManager.manageData(true);
        else
            connectionManager.manageData(false);
    }

}
