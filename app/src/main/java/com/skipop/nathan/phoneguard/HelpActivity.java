package com.skipop.nathan.phoneguard;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class HelpActivity extends ActionBarActivity {
    private final String tag = "PhoneGuard Help";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_help, menu);
        //TODO open a different menu with About, license, contact...

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(tag, "onOptionsItemSelected ");

        int id = item.getItemId();

        //TODO use proper Activities to show the proper menus
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_about:
                Intent myIntent = new Intent(HelpActivity.this, SettingsActivity.class);
                startActivity(myIntent);
                return true;
            case R.id.action_license:
                Intent myIntent2 = new Intent(HelpActivity.this, HelpActivity.class);
                startActivity(myIntent2);
                return true;
            case R.id.action_contact:
                Intent myIntent3 = new Intent(HelpActivity.this, HelpActivity.class);
                startActivity(myIntent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
