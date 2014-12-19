package com.skipop.nathan.phoneguard;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by nathan on 12/16/14.
 * Nathan Prat
 */
class ConnectionManager {
    private final Context mContext;
    private final String tag = "PhoneGuard ConnectionManager";

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

    public void setWifiState(boolean state) {
        Log.d(tag, "setWifiState");
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(state);

        boolean result = (wifiManager.isWifiEnabled() == state);

        if (result)
            Toast.makeText(mContext, "Wifi(?)", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(mContext, "Wifi(??)", Toast.LENGTH_SHORT).show();

        //return (wifiManager.isWifiEnabled() == state);
    }


    public void manageData(boolean enable) {
        Log.d(tag, "manageData: " + enable);

        // DO NOT TRY on Lollipop or FC!
        if (Build.VERSION.SDK_INT >= 21) {
            // Lollipop
            //TODO add Lollipop way
            Log.d(tag, "Lollipop -> doing nothing... ");
        }
        else{
            Log.d(tag, "Trying method2: " + enable);
            setMobileDataEnabled2(mContext, enable);
        }
    }

    public int getDataState() {
        Log.d(tag, "getDataState: ");

        TelephonyManager TelephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        return TelephonyManager.getDataState();
    }

// --Commented out by Inspection START (12/18/14 8:15 PM):
//    private void setMobileDataEnabled1(boolean enabled) {
//        Log.d(tag, "setMobileDataEnabled1: " + enabled);
//        //Hack for KitKat:http://stackoverflow.com/questions/21511216/toggle-mobile-data-programmatically-on-android-4-4-2
//        ConnectivityManager dataManager;
//        dataManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
//        Method dataMtd = null;
//        try {
//            dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
//        dataMtd.setAccessible(true);
//        try {
//            dataMtd.invoke(dataManager, enabled);
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//    }
// --Commented out by Inspection STOP (12/18/14 8:15 PM)

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
            assert conmanClass != null;
            iConnectivityManagerField = conmanClass.getDeclaredField("mService");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        assert iConnectivityManagerField != null;
        iConnectivityManagerField.setAccessible(true);
        Object iConnectivityManager = null;
        try {
            iConnectivityManager = iConnectivityManagerField.get(conman);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Class iConnectivityManagerClass = null;
        try {
            assert iConnectivityManager != null;
            iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Method setMobileDataEnabledMethod = null;
        try {
            assert iConnectivityManagerClass != null;
            //noinspection unchecked
            setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        } catch (NoSuchMethodException | NullPointerException e) {
            e.printStackTrace();
        }
        assert setMobileDataEnabledMethod != null;
        setMobileDataEnabledMethod.setAccessible(true);

        try {
            setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
