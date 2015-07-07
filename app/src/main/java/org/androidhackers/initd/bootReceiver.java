package org.androidhackers.initd;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;

public class bootReceiver extends BroadcastReceiver {

    SharedPreferences pr;
    String isChecked;

    @Override
    public void onReceive(Context context, Intent intent) {

        pr = context.getSharedPreferences("prefs", 0);
        isChecked = pr.getString("checked", null);

        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED))
        {

            if(isChecked.equals("true"))
            {
                Log.d ("org.androidhackers.log", pr.getString("checked",null));

                Log.d("org.androidhackers.log", "Received boot_completed intent");

                if (new File("/system/xbin/busybox").exists() || new File("/system/xbin/run-parts").exists())
                {
                    if (new File("/system/xbin/busybox").exists() && new File("/system/xbin/run-parts").exists())
                    {

                        try {

                            new BackgroundThread("run-parts /system/etc/init.d").execute();
                            Log.d("org.androidhackers.log", "busybox run-parts /system/etc/init.d");


                        } catch (Exception e) {

                            Log.d("org.androidhackers.log", e.toString());

                        }
                    }
                } else {

                    Log.d("org.androidhackers.log", "busybox not found");
                }
            }
            else
            {
                Log.d("org.androidhackers.log", "Init.d is NOT enabled");
            }


        }
    }
}
