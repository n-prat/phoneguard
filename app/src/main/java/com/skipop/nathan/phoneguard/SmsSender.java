package com.skipop.nathan.phoneguard;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SmsSender extends ActionBarActivity {
    //TODO do not send sms to number outside country

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        //String value = intent.getStringExtra("key"); //if it's a string you stored.

        setContentView(R.layout.activity_send_sms);

        EditText txtNumber = (EditText) findViewById(R.id.number1);

        Button button = (Button) findViewById(R.id.btnSendSMS);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText txtNumber = (EditText) findViewById(R.id.number1);
                String valNumber = txtNumber.getText().toString();

                EditText txtMessage = (EditText) findViewById(R.id.editTextSMS);
                String valMessage = txtMessage.getText().toString();

                Toast.makeText(SmsSender.this, valNumber, Toast.LENGTH_SHORT).show();
                Toast.makeText(SmsSender.this, valMessage, Toast.LENGTH_SHORT).show();
            }
        });


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
                Intent myIntent = new Intent(SmsSender.this, SettingsActivity.class);
                startActivity(myIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
