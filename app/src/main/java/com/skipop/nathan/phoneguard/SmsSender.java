package com.skipop.nathan.phoneguard;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SmsSender extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);


        EditText txtNumber = (EditText) findViewById(R.id.number1);
        //final String valNumber = txtNumber.getText().toString();

        Toast.makeText(SmsSender.this, "plop", Toast.LENGTH_SHORT).show();

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
        getMenuInflater().inflate(R.menu.menu_send_sms, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
