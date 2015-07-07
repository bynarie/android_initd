package org.androidhackers.initd;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.io.DataOutputStream;
import java.io.IOException;


public class MainActivity extends Activity {

    SharedPreferences pr;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("org.androidhackers.log", " app started");

        pr = getSharedPreferences("prefs",0);
        edit = pr.edit();
        Switch s1 = (Switch) findViewById(R.id.switch1);

        try {

            Process p;
            p = Runtime.getRuntime().exec("su");

        }
        catch (Exception e) {

            Log.d("org.androidhackers.log", e.toString());

        }

        try {

            String checked = pr.getString("checked", null);

            if (checked.equals("true"))
            {

                s1.setChecked(true);

            }
            else if (checked.equals("false"))
            {
                s1.setChecked(false);
            }

        } catch (Exception e) {

            Log.d("org.androidhackers.log", e.toString());

        }


        s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked) {

                    edit.putString("checked", "true");
                    edit.commit();
                    Log.d("org.androidhackers.log", " init.d turned on");

                }
                else {

                    edit.putString("checked", "false");
                    edit.commit();
                    Log.d("org.androidhackers.log", " init.d turned off");
                }


            }
        });
    }


}

class BackgroundThread extends AsyncTask<String, Void, Void> {
    private String s;
    public BackgroundThread(String command) {
        s = command;
    }
    @Override
    protected Void doInBackground(String... command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(s+"\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
        }
        finally {
            try {
                if (os != null)
                    os.close();
                else
                    process.destroy();
            } catch (Exception e) {

            }
        }
        return null;
    }
}
