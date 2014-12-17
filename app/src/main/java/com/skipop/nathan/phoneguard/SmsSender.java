package com.skipop.nathan.phoneguard;

import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;


/**
 * Created by nathan on 12/17/14.
 */
public class SmsSender {
    Context mContext;
    final String tag = "PhoneGuard SMSSender";
    SmsManager smsManager;

    public SmsSender(Context mContext) {
        this.mContext = mContext;
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
