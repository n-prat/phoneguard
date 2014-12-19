package com.skipop.nathan.phoneguard;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import static com.google.android.gms.common.GooglePlayServicesUtil.isGooglePlayServicesAvailable;


/**
 * Created by nathan on 12/15/14.
 * Nathan Prat
 */
public class MainActivity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    //TODO implement SimManager?
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
    //TODO store auth numbers + security status etc in SharedPrefs
    //TODO ability to change keyword (ie phoneguard)

    private final String tag = "PhoneGuard Main";

    // GOOGLE APIs
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private double mLatitude, mLongitude;

    // build a client : call it when onCreate
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(tag, "onCreate: ");

        //initialization
        SecurityManager securityManager = new SecurityManager(MainActivity.this);
        ConnectionManager connectionManager = new ConnectionManager(MainActivity.this);

        buildGoogleApiClient();

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

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(tag, "onStart ");
        if(isGooglePlayServicesAvailable(MainActivity.this) == ConnectionResult.SUCCESS){
            Log.d(tag, "Google Services available ");
            // Connect the client.
            mGoogleApiClient.connect();
        }
        else{
            Log.d(tag, "Google Services not available ");
        }
    }

    @Override
    protected void onStop() {
        Log.d(tag, "onStop ");
        // Disconnecting the client invalidates it.
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(tag, "onDestroy ");
        //rootHandler.eraseOldApk();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(tag, "onConnected requesting location updates");
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitude = mLastLocation.getLatitude();
            mLongitude = mLastLocation.getLongitude();
            Log.d(tag, "location : "+mLatitude+" "+mLongitude);
        }
        else{
            Log.d(tag, "can not get a location");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(tag, "GoogleApiClient connection has been suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(tag, "GoogleApiClient connection has failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(tag, "onLocationChanged : "+location);
        //mLocationView.setText("Location received: " + location.toString());
        Toast.makeText(this, "onLocationChanged", Toast.LENGTH_SHORT).show();
    }
}
