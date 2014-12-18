package com.skipop.nathan.phoneguard;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;

/**
 * Created by nathan on 12/16/14.
 */
public class RootHandler {
    Context mContext = null;
    final String tag = "PhoneGuard ROOTHANDLER";

    public RootHandler(Context Context) {
        this.mContext = Context;
    }


    private String getApkName(){
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

        return sourceApk;
    }

    public void installToSystem(){
        Log.d(tag, "installToSystem ");

        if(!isInstalledSystem()){
            Log.d(tag, "installing to /system ");
            String apkname = getApkName();

            remountSystem();
            copyToSystem();
            //eraseOldApk(apkname);
        }
        else{
            Log.d(tag, "already a system app ");
        }
    }

    private boolean isInstalledSystem(){
        Log.d(tag, "isInstalledSystem ");

        String apkname = getApkName();
        boolean isInstalledSystem = apkname.contains("system");

        Log.d(tag, "isInstalledSystem? "+isInstalledSystem);

        return isInstalledSystem;
    }

    private void remountSystem(){
        try {
            Log.d(tag, "remountSystem ");

            Runtime rt = Runtime.getRuntime();
            Process proc;
            String command;
            String remount = "su -c mount -o rw,remount /dev/block/mtdblock0 /system";
            ///dev/block/mmcblk0p13
            //su -c mount -o rw,remount /dev/block/mmcblk0p13 /system

            Log.d(tag, "command: " + remount);
            proc = rt.exec(remount);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(proc.getErrorStream()));

            // read the output from the command
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                Log.d(tag, "output: " + s);
            }

            // read any errors from the attempted command
            while ((s = stdError.readLine()) != null) {
                Log.d(tag, "error output: " + s);
            }
        } catch (Exception e) {
            Log.d(tag, "Error [" + e.getClass().getName() + "] : " + e.getMessage());
        }
    }

    private void eraseOldApk(String apkname){
        try {
            Log.d(tag, "eraseOldApk ");

            Runtime rt = Runtime.getRuntime();
            Process proc;
            String command = "su -c rm "+apkname;

            Log.d(tag, "command: " + command);
            proc = rt.exec(command);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(proc.getErrorStream()));

            // read the output from the command
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                Log.d(tag, "output: " + s);
            }

            // read any errors from the attempted command
            while ((s = stdError.readLine()) != null) {
                Log.d(tag, "error output: " + s);
            }
        } catch (Exception e) {
            Log.d(tag, "Error [" + e.getClass().getName() + "] : " + e.getMessage());
        }
    }

    private void copyToSystem(){
        //TODO check setting for root perm

        try {
            Log.d(tag, "copyToSystem ");

            String apkname = getApkName();
            Runtime rt = Runtime.getRuntime();
            Process proc;
            String command;

            Log.d(tag, "Remounting /system ");
            remountSystem();

            if (Build.VERSION.SDK_INT > 18) {
                //Android 4.3+ -> /system/priv-app
                command = "su -c cp "+apkname+" /system/priv-app";
            }
            else{
                // -> /system/app
                command = "su -c cp "+apkname+" /system/app";
            }
            Log.d(tag, "command: " + command);
            proc = rt.exec(command);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(proc.getErrorStream()));

            // read the output from the command
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                Log.d(tag, "output: " + s);

            }

            // read any errors from the attempted command
            while ((s = stdError.readLine()) != null) {
                Log.d(tag, "error output: " + s);
            }
        } catch (Exception e) {
            Log.d(tag, "Error [" + e.getClass().getName() + "] : " + e.getMessage());
        }

    }

    public boolean checkRoot() {
        boolean isRooted = false, result1 = false, result2 = false;
        boolean isApkInstalled = new File("/system/app/Superuser.apk").exists();

        Log.d(tag, "Superuser.apk exists? " + isApkInstalled);

        //result1 = checkMethod1();
        result2 = checkMethod2();

        isRooted = result1 || result2;

        if (isRooted)
            Toast.makeText(mContext, "Root Access granted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(mContext, "Can't get Root Access", Toast.LENGTH_SHORT).show();


        Log.d(tag, "Result: " + isRooted);
        return isRooted;
    }//checkRoot


    private boolean checkMethod1() {
        // DOES NOT WORK
        boolean isRooted = false;
        try {
            Log.d(tag, "checkMethod1 ");
            //http://stackoverflow.com/questions/5711084/java-runtime-getruntime-getting-output-from-executing-a-command-line-program
            Runtime rt = Runtime.getRuntime();
            String[] commands = {"su", "-V"};
            Process proc = rt.exec(commands);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(proc.getErrorStream()));

            // read the output from the command
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                Log.d(tag, "check output: " + s);
                try {
                    int version = Integer.parseInt(s);
                    if (version >= 235)
                        isRooted = true;
                } catch (NumberFormatException e) {
                    //
                }
            }

            // read any errors from the attempted command
            while ((s = stdError.readLine()) != null) {
                Log.d(tag, "error output: " + s);
            }
        } catch (Exception e) {
            Log.d(tag, "Root access rejected [" + e.getClass().getName() + "] : " + e.getMessage());
        }

        Log.d(tag, "Method1 result: " + isRooted);
        return isRooted;
    }//checkMethod1

    private boolean checkMethod2() {
        // send command "su -c whoami"
        // seems to be working
        // -> return true if access was granted, and ask if new install
        // -> return false is not granted or no su installed
        boolean isRooted = false;
        try {
            Log.d(tag, "checkMethod2 ");
            //http://stackoverflow.com/questions/5711084/java-runtime-getruntime-getting-output-from-executing-a-command-line-program
            Runtime rt = Runtime.getRuntime();

            //command su -c -> do not show root prompt
            //String[] commands = {"su", "-c"};
            //Process proc = rt.exec(commands);
            String command = "su -c whoami";
            Process proc = rt.exec(command);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(proc.getErrorStream()));

            // read the output from the command
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                Log.d(tag, "check output 2: " + s);
                isRooted = isRooted || s.contains("uid");
            }

            // read any errors from the attempted command
            while ((s = stdError.readLine()) != null) {
                Log.d(tag, "error output 2: " + s);
                isRooted = isRooted || s.contains("uid");
            }
        } catch (Exception e) {
            Log.d(tag, "Root access rejected [" + e.getClass().getName() + "] : " + e.getMessage());
        }

        Log.d(tag, "Method2 result: " + isRooted);
        return isRooted;
    }//checkMethod2

}//class
