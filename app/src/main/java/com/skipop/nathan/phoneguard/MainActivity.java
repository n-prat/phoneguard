package com.skipop.nathan.phoneguard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Created by nathan on 12/15/14.
 */
public class MainActivity extends ActionBarActivity {

    /** Called when the user touches the button */
    public void buttonSmsHandler(View view) {
        // Do something in response to button click
        Intent myIntent = new Intent(MainActivity.this, SmsSender.class);
        //myIntent.putExtra("argu1", 5); //Optional parameters
        startActivity(myIntent);
    }

    /** Called when the user touches the button */
    public void buttonRootHandler(View view) {
        // Do something in response to button click
        RootHandler rootHandler = new RootHandler();
        if(rootHandler.checkRoot()){
            Toast.makeText(MainActivity.this, "Rooted(?)", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(MainActivity.this, "Probably not rooted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
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
            case R.id.new_game:
                //newGame();
                return true;
            case R.id.help:
                //showHelp();
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
}
