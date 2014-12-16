package com.skipop.nathan.phoneguard;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

/**
 * Created by nathan on 12/16/14.
 */
public class RootHandler {
    public static boolean checkRoot()
    {
        boolean isRooted = false;

        try
        {
            //http://stackoverflow.com/questions/5711084/java-runtime-getruntime-getting-output-from-executing-a-command-line-program
            Runtime rt = Runtime.getRuntime();
            String[] commands = {"su","-V"};
            Process proc = rt.exec(commands);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(proc.getErrorStream()));

            // read the output from the command
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
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
                System.out.println(s);
            }
        }
        catch (Exception e)
        {
            Log.d("ROOT", "Root access rejected [" + e.getClass().getName() + "] : " + e.getMessage());
        }

        return isRooted;
    }
}
