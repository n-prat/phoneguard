package com.skipop.nathan.phoneguard;

import android.telephony.SmsManager;
import android.util.Log;


/**
 * Created by nathan on 12/17/14.
 * Nathan Prat
 */
class SmsSender {
    private final String tag = "PhoneGuard SMSSender";
    private final SmsManager smsManager;

    public SmsSender() {
        this.smsManager = SmsManager.getDefault();
    }

    public void sendCommandList(String number){
        Log.d(tag, "sendCommandList");
        smsManager.sendTextMessage(number,null,"CommandList",null,null);
    }

    public void sendSecurityChanged(String number){
        Log.d(tag, "sendSecurityChanged");
        smsManager.sendTextMessage(number,null,"SecurityChanged",null,null);
    }
}
