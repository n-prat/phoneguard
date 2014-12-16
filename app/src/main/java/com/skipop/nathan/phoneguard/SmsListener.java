package com.skipop.nathan.phoneguard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by nathan on 12/15/14.
 */
public class SmsListener extends BroadcastReceiver {

    private SharedPreferences preferences;
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from="";
            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();

                        // Show alert
                        int duration = Toast.LENGTH_LONG;

                        //TODO use an handler
                        if(msgBody.startsWith("phoneguard")){
                            String[] msgContent = msgBody.split(" ");

                            if(msgContent[1].compareToIgnoreCase("password") == 0){
                                //Toast toast = Toast.makeText(context, "senderNum: "+ msg_from + ", password: " + msgContent[index], duration);
                                Toast toast = Toast.makeText(context, "PhoneGuard: Password Received", duration);
                                toast.show();

                                if(msgContent.length > 2){
                                //if(passwd.isEmpty()){
                                    String passwd;
                                    passwd = msgContent[2]; //3rd field ex:"phoneguard password azerty"
                                    toast = Toast.makeText(context, "PhoneGuard: Password: "+passwd, duration);
                                    toast.show();
                                }
                                else{
                                    toast = Toast.makeText(context, "PhoneGuard: Password Empty...", duration);
                                    toast.show();
                                }
                            }
                            else{
                                Toast toast = Toast.makeText(context, "PhoneGuard: Invalid Command", duration);
                                toast.show();
                            }
                        }
                    }
                }catch(Exception e){
//                            Log.d("Exception caught",e.getMessage());
                }
            }
        }

        // WARNING!!!
        // If you uncomment the next line then received SMS will not be put to incoming.
        // Be careful!
        // this.abortBroadcast();
    }
}