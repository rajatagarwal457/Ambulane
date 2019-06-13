package com.ambulane.driver;

import android.app.Service;
import android.content.Context;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.content.Intent;
import android.provider.Settings;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.nio.file.StandardOpenOption;

/**
 * Created by Ayush on 07-04-2019.
 */

public class UID extends Service{

    private static String AppID = "";

    public IBinder onBind(Intent intent){
        return null;
    }

    //accessor method
    public String ID(){

        if (AppID.equals("")){
            getID();
        }

        return AppID;
    }

    private void getID(){

        FileInputStream inStream;

        //reading the file to check for code
        try {
            inStream = openFileInput("uid.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            AppID = bufferedReader.readLine();

            inStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        //if code read from file is null, fetch code from ANDROID_ID
        if (AppID.equals("")) {
            AppID = createID();
        }
    }

    private String createID(){
        int appversion = Integer.valueOf(android.os.Build.VERSION.SDK);
        String newID = "";
        if (appversion < 23) {
            TelephonyManager mngr = (TelephonyManager)
                    getSystemService(Context.TELEPHONY_SERVICE);

            try {
                newID = mngr.getDeviceId();
            } catch (SecurityException se){
                se.printStackTrace();
                newID = "";
            }
        }
        else
        {
            newID = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                            Settings.Secure.ANDROID_ID);
        }

        //writing the code to a file:
        FileOutputStream outStream;
        try {
            outStream = openFileOutput("uid.txt", Context.MODE_PRIVATE);
            outStream.write(newID.getBytes());
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newID;
    }
}
