package com.skipop.nathan.phoneguard;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by nathan on 12/15/14.
 */
public class MainActivity extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.Button01);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Hello World", Toast.LENGTH_SHORT).show();

                final Runtime runtime = Runtime.getRuntime();
                try {
                    Toast.makeText(MainActivity.this, "Rooting...", Toast.LENGTH_SHORT).show();
                    runtime.exec("su"); //or whatever command.
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
