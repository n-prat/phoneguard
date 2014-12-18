package com.skipop.nathan.phoneguard;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

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
        try {
            Log.d(tag, "cleanSystem ");

            Runtime rt = Runtime.getRuntime();
            Process proc;

            String command = "su -c rm /system/priv-app/com.skipop.* ";
            Log.d(tag, "command: " + command);
            proc = rt.exec(command);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(proc.getErrorStream()));

            // read the output from the command
            String s;
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

    private void changePermissions(){
        try {
            Log.d(tag, "remountSystem ");

            Runtime rt = Runtime.getRuntime();
            Process proc;

            //update newApk path
            String[] path = mOldApk.split("/");
            mNewApk = mNewApk+path[3];
            Log.d(tag, "newapk: " + mNewApk);

            String command = "su -c chmod 644 "+mNewApk;
            Log.d(tag, "command: " + command);
            proc = rt.exec(command);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(proc.getErrorStream()));

            // read the output from the command
            String s;
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

    private String getMemblockSystem(){
        try {
            Log.d(tag, "getMemblockSystem ");

            Runtime rt = Runtime.getRuntime();
            Process proc;
            String command="su -c mount";
            String fullline,block="";

            Log.d(tag, "command: " + command);
            proc = rt.exec(command);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(proc.getErrorStream()));

            // read the output from the command
            String s;
            while ((s = stdInput.readLine()) != null) {
                if(s.contains("/system")){
                    fullline = s;

                    String[] tmp = fullline.split(" ",2);
                    block = tmp[0];
                    break;
                }
            }

            // read any errors from the attempted command
            while ((s = stdError.readLine()) != null) {
                Log.d(tag, "error output: " + s);
            }

            Log.d(tag, "block: " + block);
            return block;
        } catch (Exception e) {
            Log.d(tag, "Error [" + e.getClass().getName() + "] : " + e.getMessage());
        }
        return null;
    }

    private boolean isInstalledSystem(){
        Log.d(tag, "isInstalledSystem ");

        getApkName();
        boolean isInstalledSystem = mOldApk.contains("system");

        Log.d(tag, "isInstalledSystem? "+isInstalledSystem);

        return isInstalledSystem;
    }

    private void remountSystem(String block){
        try {
            Log.d(tag, "remountSystem ");

            Runtime rt = Runtime.getRuntime();
            Process proc;
            String remount = "su -c mount -o rw,remount "+block+" /system";

            Log.d(tag, "command: " + remount);
            proc = rt.exec(remount);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(proc.getErrorStream()));

            // read the output from the command
            String s;
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

    void eraseOldApk(){
        try {
            Log.d(tag, "eraseOldApk ");

            Runtime rt = Runtime.getRuntime();
            Process proc;
            String command = "su -c rm "+ mOldApk;

            Log.d(tag, "command: " + command);
            proc = rt.exec(command);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(proc.getErrorStream()));

            // read the output from the command
            String s;
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

            Runtime rt = Runtime.getRuntime();
            Process proc;
            String command;

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
            Log.d(tag, "command: " + command);
            proc = rt.exec(command);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(proc.getErrorStream()));

            // read the output from the command
            String s;
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
        boolean isRooted, result2;
        boolean isApkInstalled = new File("/system/app/Superuser.apk").exists();

        Log.d(tag, "Superuser.apk exists? " + isApkInstalled);

        //result1 = checkMethod1();
        result2 = checkMethod2();

        isRooted = isApkInstalled && result2;

        if (isRooted)
            Toast.makeText(mContext, "Root Access granted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(mContext, "Can't get Root Access", Toast.LENGTH_SHORT).show();


        Log.d(tag, "Result: " + isRooted);
        return isRooted;
    }//checkRoot


// --Commented out by Inspection START (12/18/14 8:18 PM):
//    private boolean checkMethod1() {
//        // DOES NOT WORK
//        boolean isRooted = false;
//        try {
//            Log.d(tag, "checkMethod1 ");
//            //http://stackoverflow.com/questions/5711084/java-runtime-getruntime-getting-output-from-executing-a-command-line-program
//            Runtime rt = Runtime.getRuntime();
//            String[] commands = {"su", "-V"};
//            Process proc = rt.exec(commands);
//
//            BufferedReader stdInput = new BufferedReader(new
//                    InputStreamReader(proc.getInputStream()));
//
//            BufferedReader stdError = new BufferedReader(new
//                    InputStreamReader(proc.getErrorStream()));
//
//            // read the output from the command
//            String s = null;
//            while ((s = stdInput.readLine()) != null) {
//                Log.d(tag, "check output: " + s);
//                try {
//                    int version = Integer.parseInt(s);
//                    if (version >= 235)
//                        isRooted = true;
//                } catch (NumberFormatException e) {
//                    //
//                }
//            }
//
//            // read any errors from the attempted command
//            while ((s = stdError.readLine()) != null) {
//                Log.d(tag, "error output: " + s);
//            }
//        } catch (Exception e) {
//            Log.d(tag, "Root access rejected [" + e.getClass().getName() + "] : " + e.getMessage());
//        }
//
//        Log.d(tag, "Method1 result: " + isRooted);
//        return isRooted;
//    }//checkMethod1
// --Commented out by Inspection STOP (12/18/14 8:18 PM)

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
            String s;
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
