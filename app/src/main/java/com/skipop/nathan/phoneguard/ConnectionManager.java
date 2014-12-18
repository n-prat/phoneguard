package com.skipop.nathan.phoneguard;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by nathan on 12/16/14.
 */
public class ConnectionManager {
    Context mContext;
    final String tag = "PhoneGuard ConnectionManager";

    /*public ConnectionManager() {
        this.mContext = null;
    }*/

    public ConnectionManager(Context mContext) {
        this.mContext = mContext;
    }

    public boolean checkWifiState() {
        Log.d(tag, "checkWifiState");
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    public boolean setWifiState(boolean state) {
        Log.d(tag, "setWifiState");
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(state);

        boolean result = (wifiManager.isWifiEnabled() == state);

        if (result)
            Toast.makeText(mContext, "Wifi(?)", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(mContext, "Wifi(??)", Toast.LENGTH_SHORT).show();

        return (wifiManager.isWifiEnabled() == state);
    }


    public void manageData(boolean enable) {
        Log.d(tag, "manageData: " + enable);

        Log.d(tag, "Trying method2: " + enable);
        setMobileDataEnabled2(mContext, enable);
    }

    public int getDataState() {
        Log.d(tag, "getDataState: ");

        TelephonyManager TelephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        int state = TelephonyManager.getDataState();

        return state;
    }

    private void setMobileDataEnabled1(boolean enabled) {
        Log.d(tag, "setMobileDataEnabled1: " + enabled);
        //Hack for KitKat:http://stackoverflow.com/questions/21511216/toggle-mobile-data-programmatically-on-android-4-4-2
        ConnectivityManager dataManager;
        dataManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        Method dataMtd = null;
        try {
            dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        dataMtd.setAccessible(true);
        try {
            dataMtd.invoke(dataManager, enabled);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void setMobileDataEnabled2(Context context, boolean enabled) {
        Log.d(tag, "setMobileDataEnabled2: " + enabled);
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
