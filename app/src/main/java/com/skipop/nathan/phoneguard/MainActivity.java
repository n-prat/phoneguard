package com.skipop.nathan.phoneguard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by nathan on 12/15/14.
 */
public class MainActivity extends Activity{
    Button buttonRoot;
    Button buttonSms;

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
}
