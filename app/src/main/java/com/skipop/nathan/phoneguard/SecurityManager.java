package com.skipop.nathan.phoneguard;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by nathan on 12/17/14.
 * Nathan Prat
 */
class SecurityManager {
    private final String filename = "securitystate";
    private File file;
    private final Context mContext;
    private final String tag = "PhoneGuard SecurityManager";

    public SecurityManager(Context context) {
        mContext = context;
    }

    public boolean isSecurityActivated(){
        Log.d(tag, "isSecurityActivated");

        file = mContext.getFileStreamPath(filename);
        if(file == null || !file.exists()) {
            Log.d(tag, "no file");
            return false;
        }
        Log.d(tag, "file exists");
        return true;
    }

    //turn on security
    public void securityOn(){
        Log.d(tag, "turning security On");

        //writing something just to be sure file exists
        String string = "Hello world!";
        FileOutputStream outputStream;

        try {
            outputStream = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void securityOff(){
        Log.d(tag, "turning security Off");

        if(isSecurityActivated()){
            if(file.delete())
                Log.d(tag, "file deleted");
            else
                Log.d(tag, "error during file delete");
        }
    }
}
