package es.iessanvicente.eventos.myeventslistapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity {

    String[] permissions;
    Thread thread;
    SharedPreferences shared;
    boolean isPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        shared = PreferenceManager.getDefaultSharedPreferences(this);
        isPermissions = shared.getBoolean( "isPermissions", false );

        if (isPermissions)
        {
            Intent LoginMenu = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(LoginMenu);
            this.finish();
        }
        else
        {
            permissions = new String[]
            {
                    Manifest.permission.INTERNET,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.VIBRATE,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            };

            checkPermissions();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (isPermissions)
        {
            Intent LoginMenu = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(LoginMenu);
        }
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED)
            {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);

            thread = new Thread()
            {
                @Override
                public void run()
                {
                    try
                    {
                        sleep(5000);
                        Intent LoginPrincipal = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(LoginPrincipal);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
            SharedPreferences.Editor editor = shared.edit();
            editor.putBoolean("isPermissions", true);
            editor.commit();

            return false;
        }
        return true;
    }


}
