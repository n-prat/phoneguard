package com.skipop.nathan.phoneguard;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by nathan on 12/16/14.
 */
public class ConnectionManager {
    Context mContext;

    public ConnectionManager() {
        this.mContext = null;
    }

    public ConnectionManager(Context mContext) {
        this.mContext = mContext;
    }

    public boolean checkWifiState(){
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    public boolean setWifiState(boolean state){
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(state);

        boolean result = (wifiManager.isWifiEnabled() == state);

        if(result)
            Toast.makeText(mContext, "Wifi(?)", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(mContext, "Wifi(??)", Toast.LENGTH_SHORT).show();

        return (wifiManager.isWifiEnabled() == state);
    }



    public void manageData(boolean enable){
        //Toast.makeText(mContext, "Method 1 true", Toast.LENGTH_SHORT).show();
        //setMobileDataEnabled1(true);
        // -> turn off data?

        //Toast.makeText(mContext, "Method 1 false", Toast.LENGTH_SHORT).show();
        //setMobileDataEnabled1(false);
        // -> turn off data?

        Toast.makeText(mContext, "Method 2 "+enable, Toast.LENGTH_SHORT).show();
        setMobileDataEnabled2(mContext,enable);
        /*if(enable){
            Toast.makeText(mContext, "Method 2 true", Toast.LENGTH_SHORT).show();
            setMobileDataEnabled2(mContext,true);
            // data on -> do nothing || data off -> data on
        }
        else{
            Toast.makeText(mContext, "Method 2 false", Toast.LENGTH_SHORT).show();
            setMobileDataEnabled2(mContext,false);
        }*/
    }

    private void setMobileDataEnabled1(boolean enabled) {
        //Hack for KitKat:http://stackoverflow.com/questions/21511216/toggle-mobile-data-programmatically-on-android-4-4-2
        ConnectivityManager dataManager;
        dataManager  = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        Method dataMtd = null;
        try {
            dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dataMtd.setAccessible(true);
        try {
            dataMtd.invoke(dataManager, enabled);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void setMobileDataEnabled2(Context context, boolean enabled) {
        //http://stackoverflow.com/questions/12535101/how-can-i-turn-off-3g-data-programmatically-on-android
        final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        Class conmanClass = null;
        try {
            conmanClass = Class.forName(conman.getClass().getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Field iConnectivityManagerField = null;
        try {
            iConnectivityManagerField = conmanClass.getDeclaredField("mService");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        iConnectivityManagerField.setAccessible(true);
        Object iConnectivityManager = null;
        try {
            iConnectivityManager = iConnectivityManagerField.get(conman);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Class iConnectivityManagerClass = null;
        try {
            iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Method setMobileDataEnabledMethod = null;
        try {
            setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        setMobileDataEnabledMethod.setAccessible(true);

        try {
            setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
