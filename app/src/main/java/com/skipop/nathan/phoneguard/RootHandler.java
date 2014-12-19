package com.skipop.nathan.phoneguard;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by nathan on 12/16/14.
 * Nathan Prat
 */
class RootHandler {
    private Context mContext = null;
    private final String tag = "PhoneGuard ROOTHANDLER";
    private String mOldApk;
    private String mNewApk;

    public RootHandler(Context Context) {
        this.mContext = Context;
    }


    //update mOldApk name to get the full path(ie the one in data/app)
    private void getApkName(){
        Log.d(tag, "getApkName ");

        String sourceApk = "";
        String packageName = "com.skipop.nathan.phoneguard";
        PackageManager pm = mContext.getPackageManager();

        try {
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            sourceApk = ai.publicSourceDir;

            Log.d(tag, "sourceApk: "+sourceApk);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        mOldApk = sourceApk;
        //return sourceApk;
    }

    //http://stackoverflow.com/questions/13534419/check-to-see-whether-root-was-granted
    public boolean execCmdSu(String command, ArrayList<String> results){

        try {
            Log.d(tag, "execCmdSu ");

            Runtime rt = Runtime.getRuntime();
            Process proc;

            command = "su -c "+command;
            Log.d(tag, "execCmdSu command: " + command);
            proc = rt.exec(command);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(proc.getErrorStream()));

            // read the output from the command
            String s;
            while ((s = stdInput.readLine()) != null) {
                Log.d(tag, "execCmdSu output: " + s);
                results.add(s);
            }

            // read any errors from the attempted command
            while ((s = stdError.readLine()) != null) {
                Log.d(tag, "execCmdSu error output: " + s);
                results.add(s);
            }

            return true;
        } catch (Exception e) {
            Log.d(tag, "Error [" + e.getClass().getName() + "] : " + e.getMessage());
            return false;
        }
    }


    //TODO use an external apk to move it
    public void installToSystem(){
        Log.d(tag, "installToSystem ");

        if(!isInstalledSystem()){
            Log.d(tag, "installing to /system ");
            String block = getMemblockSystem();

            remountSystem(block);
            cleanSystem();
            copyToSystem();
            changePermissions();
            eraseOldApk();
        }
        else{
            Log.d(tag, "already a system app ");
        }
    }


    private void cleanSystem(){
        Log.d(tag, "cleanSystem ");

        String command = "rm /system/priv-app/com.skipop.* ";
        ArrayList<String> output = new ArrayList<String>();
        execCmdSu(command,output);
    }

    private void changePermissions(){
        Log.d(tag, "changePermissions ");

        ArrayList<String> output = new ArrayList<String>();

        //update newApk path
        String[] path = mOldApk.split("/");
        mNewApk = mNewApk+path[3];
        Log.d(tag, "newapk: " + mNewApk);

        String command = "su -c chmod 644 "+mNewApk;

        execCmdSu(command,output);
    }

    private String getMemblockSystem(){
        //TODO test on real device to adjust
        Log.d(tag, "getMemblockSystem ");

        String fullline,block="";
        ArrayList<String> output = new ArrayList<String>();
        String command="su -c mount";

        execCmdSu(command,output);

        // read the output from the command
        String s;
        for(int i=0; i<output.size(); i++){
            if(output.get(i).contains("system")){
                String[] tmp = output.get(i).split(" ",2);
                block = tmp[0];
                break;
            }
        }

        return block;
    }

    private boolean isInstalledSystem(){
        Log.d(tag, "isInstalledSystem ");

        getApkName();
        boolean isInstalledSystem = mOldApk.contains("system");

        Log.d(tag, "isInstalledSystem? "+isInstalledSystem);

        return isInstalledSystem;
    }

    private void remountSystem(String block){
        Log.d(tag, "remountSystem ");

        ArrayList<String> output = new ArrayList<String>();

        String command = "su -c mount -o rw,remount "+block+" /system";

        execCmdSu(command,output);
    }

    void eraseOldApk(){
        Log.d(tag, "eraseOldApk ");

        ArrayList<String> output = new ArrayList<String>();

        String command = "su -c rm "+ mOldApk;

        execCmdSu(command,output);
    }

    private void copyToSystem(){
        //TODO check setting for root perm
        Log.d(tag, "copyToSystem ");

        String command;
        ArrayList<String> output = new ArrayList<String>();

        if (Build.VERSION.SDK_INT > 18) {
            //Android 4.3+ -> /system/priv-app
            command = "su -c cp "+mOldApk+" /system/priv-app";
            mNewApk = "/system/priv-app/";
        }
        else{
            // -> /system/app
            command = "su -c cp "+mOldApk+" /system/app";
            mNewApk = "/system/app/";
        }

        execCmdSu(command,output);
    }

    public boolean checkRootv2() {
        //whoami -> no, random -> no
        boolean isRooted, result2 = false;
        boolean isApkInstalled = new File("/system/app/Superuser.apk").exists();

        Log.d(tag, "checkRootv2 Superuser.apk exists? " + isApkInstalled);

        ArrayList<String> output = new ArrayList<String>();
        output = new ArrayList<String>();
        if(execCmdSu("ls", output)){
            result2 = result2 || output.contains("system");
            Log.d(tag, "checkRootv2 result2 " + result2);
        }
        isRooted = isApkInstalled && result2 ;

        if (isRooted)
            Toast.makeText(mContext, "Root Access granted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(mContext, "Can't get Root Access", Toast.LENGTH_SHORT).show();


        Log.d(tag, "checkRootv2 isRooted: " + isRooted);
        return isRooted;
    }//checkRootv2
}//class
