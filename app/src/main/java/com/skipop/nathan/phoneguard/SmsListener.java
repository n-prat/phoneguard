package com.skipop.nathan.phoneguard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by nathan on 12/15/14.
 */
public class SmsListener extends BroadcastReceiver {
    Context mContext = null;
    final String tag = "PhoneGuard SMS";

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    SmsMessage[] messages;


    public SmsListener(Context mContext) {
        this.mContext = mContext;
    }

    public SmsListener() {
        this.mContext = null;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;

        //http://stackoverflow.com/questions/9378328/android-how-to-get-phone-number-from-a-incoming-sms
        Bundle bundle = intent.getExtras();

        if (bundle == null) {
            Log.w(tag, "BroadcastReceiver failed, no intent data to process.");
            return;
        }

        //Broadcast valid, we can move on
        String smsOriginatingAddress, smsDisplayMessage;


        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            //we have received the correct Intent
            Log.d(tag, "SMS_RECEIVED");

            if (Build.VERSION.SDK_INT < 19) {
                //Pre-KitKat way to handle SMS
                Object[] data = (Object[]) bundle.get("pdus");
                for (Object pdu : data) {
                    Log.d(tag, "legacy SMS implementation");
                    SmsMessage message = SmsMessage.createFromPdu((byte[]) pdu);

                    if (message == null) {
                        Log.e(tag, "SMS message is null -- ABORT");
                        break;
                    }

                    smsOriginatingAddress = message.getDisplayOriginatingAddress();
                    smsDisplayMessage = message.getDisplayMessageBody(); // see getMessageBody();
                    processSms(message);
                    //processReceivedSms(smsOriginatingAddress, smsDisplayMessage);

                }
            } else {
                //The KitKat way  : API level 19
                for (SmsMessage message : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                    Log.d(tag, "KitKat or newer");
                    if (message == null) {
                        Log.e(tag, "SMS message is null -- ABORT");
                        break;
                    }
                    smsOriginatingAddress = message.getDisplayOriginatingAddress();
                    smsDisplayMessage = message.getDisplayMessageBody(); //see getMessageBody();
                    processSms(message);
                }
            }
        }
        else{
            //we have received the wrong Intent
            Log.d(tag, "SMS_RECEIVED wrong intent");
        }
    }


    public void processSms(SmsMessage message) {
        String msgAddress = message.getDisplayOriginatingAddress();
        String msgBody = message.getDisplayMessageBody();

        if (msgBody.startsWith("phoneguard")) {
            //we have an useful message

            String[] msgContent = msgBody.split(" ");
            int duration = Toast.LENGTH_SHORT;
            Toast toast;

            if (msgContent.length > 1) {
                //handle the multiple possible commands

                if (msgContent[1].compareToIgnoreCase("password") == 0) {
                    //"password" command
                    if (msgContent.length > 2) {
                        //if(passwd.isEmpty()){
                        String passwd;
                        passwd = msgContent[2]; //3rd field ex:"phoneguard password azerty"

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                        String passwdSet = prefs.getString("setting_password","not found");

                        if(passwd.equals(passwdSet)){
                            //we have a match
                            Toast.makeText(mContext, "Password Accepted ", Toast.LENGTH_SHORT).show();

                            //TODO activate security mode -> new file?
                            //TODO service on?

                            //TODO send back confirmation text with command list
                        }
                        else{
                            //wrong password
                            Toast.makeText(mContext, "Password Refused ", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        toast = Toast.makeText(mContext, "PhoneGuard: Password Empty...", duration);
                        toast.show();
                    }
                } else if (msgContent[1].compareToIgnoreCase("othercommand") == 0) {

                } else {
                    toast = Toast.makeText(mContext, "PhoneGuard: Invalid Command", duration);
                    toast.show();
                }
            } else {
                toast = Toast.makeText(mContext, "PhoneGuard: Empty Command", duration);
                toast.show();
            }
        }
    }


}//end of class
