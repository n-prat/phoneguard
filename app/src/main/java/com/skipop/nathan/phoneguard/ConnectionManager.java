package com.skipop.nathan.phoneguard;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.widget.Toast;

/**
 * Created by nathan on 12/16/14.
 */
public class ConnectionManager {
    Context mContext;

    public ConnectionManager(Context mContext) {
        this.mContext = mContext;
    }

    public boolean checkWifiState(){
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(mContext.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    public boolean setWifiState(boolean state){
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(mContext.WIFI_SERVICE);
        wifiManager.setWifiEnabled(state);

        boolean result = (wifiManager.isWifiEnabled() == state);

        if(result)
            Toast.makeText(mContext, "Wifi(?)", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(mContext, "Wifi(??)", Toast.LENGTH_SHORT).show();

        return (wifiManager.isWifiEnabled() == state);
    }
}
