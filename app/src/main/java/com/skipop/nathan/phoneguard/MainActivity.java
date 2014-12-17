package com.skipop.nathan.phoneguard;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ToggleButton;

/**
 * Created by nathan on 12/15/14.
 */
public class MainActivity extends ActionBarActivity {
    final String tag = "PhoneGuard Main";
    SecurityManager securityManager;

    //TODO receive boot event
    //TODO-> if security was on before : restart service
    //TODO-> if security and new sim : send new number to old one
    //TODO implement service?
    //TODO (start when security is enabled, restart on boot if necessary)
    //TODO (stop only when phone is found again(unlock, security off message?)
    //TODO on sim card change -> send new number to the old on

    //TODO add PhotoHandler -> can take photo without sound (upload?)

    //TODO implement location services

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Properly set default values upon first launch
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        setContentView(R.layout.activity_main);

        securityManager= new SecurityManager(MainActivity.this);
        securityManager.isSecurityActivated();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(myIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onToggleWifiClicked(View view) {
        ToggleButton toggle = (ToggleButton) findViewById(R.id.togglebuttonwifi);

        // Is the toggle on?
        boolean on = toggle.isChecked();
        ConnectionManager connectionManager= new ConnectionManager(MainActivity.this);
        //w ConnectionManager(this.getApplicationContext());

        connectionManager.setWifiState(on);
    }

    public void onToggleDataClicked(View view) {
        ToggleButton toggle = (ToggleButton) findViewById(R.id.togglebuttondata);

        // Is the toggle on?
        boolean on = toggle.isChecked();
        ConnectionManager connectionManager= new ConnectionManager(MainActivity.this);
        if(on)
            connectionManager.manageData(true);
        else
            connectionManager.manageData(false);
    }

    public SecurityManager getSecurityManager() {
        return securityManager;
    }

}
