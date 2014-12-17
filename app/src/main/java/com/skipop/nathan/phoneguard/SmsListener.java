package com.skipop.nathan.phoneguard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by nathan on 12/15/14.
 */
public class SmsListener extends BroadcastReceiver {
    Context mContext = null;
    final String tag = ".onReceive";
    private SharedPreferences preferences;
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

            /*//The KitKat way  : API level 19
            for (SmsMessage message : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                Log.d(tag, "KitKat or newer");
                if (message == null) {
                    Log.e(tag, "SMS message is null -- ABORT");
                    break;
                }
                smsOriginatingAddress = message.getDisplayOriginatingAddress();
                smsDisplayMessage = message.getDisplayMessageBody(); //see getMessageBody();
                processReceivedSms(smsOriginatingAddress, smsDisplayMessage);
            }*/
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
                        toast = Toast.makeText(mContext, "PhoneGuard: Password: " + passwd, duration);
                        toast.show();
                    } else {
                        toast = Toast.makeText(mContext, "PhoneGuard: Password Empty...", duration);
                        toast.show();
                    }
                } else if(msgContent[1].compareToIgnoreCase("othercommand") == 0){

                }else {
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
