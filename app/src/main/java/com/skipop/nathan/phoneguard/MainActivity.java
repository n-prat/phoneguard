package com.skipop.nathan.phoneguard;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by nathan on 12/15/14.
 */
public class MainActivity extends ActionBarActivity {
    Button buttonRoot;
    Button buttonSms;
    Button buttonSettings;

    View.OnClickListener handlerButtonSms = new View.OnClickListener() {
        public void onClick(View v) {
            Intent myIntent = new Intent(MainActivity.this, SmsSender.class);
            //myIntent.putExtra("argu1", 5); //Optional parameters
            startActivity(myIntent);
        }
    };

    View.OnClickListener handlerButtonRoot = new View.OnClickListener() {
        public void onClick(View v) {
            final Runtime runtime = Runtime.getRuntime();
            try {
                Toast.makeText(MainActivity.this, "Rooting...", Toast.LENGTH_SHORT).show();
                runtime.exec("su"); //or whatever command.
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Button buttonRoot = (Button) findViewById(R.id.buttonRoot);
        Button buttonSms = (Button) findViewById(R.id.buttonSms);

        buttonRoot.setOnClickListener(handlerButtonRoot);
        buttonSms.setOnClickListener(handlerButtonSms);
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
}
