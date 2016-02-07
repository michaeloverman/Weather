package com.overman.weather.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.sunshine.app.SettingsActivity;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new com.overman.weather.app.ForecastFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            //Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        if (id == R.id.action_map) {
            // get preferredLocation from Shared
        //    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String locationPref = PreferenceManager.getDefaultSharedPreferences(this)
                    .getString(getString(R.string.pref_location_key),
                    getString(R.string.pref_location_default));

            Uri location = Uri.parse("geo:0,0?q="+locationPref);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);

            // verify location & map app
    //        PackageManager packageManager = getPackageManager();
    //        List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
    //        boolean isIntentSafe = activities.size() > 0;

            // handle intent
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                // MAKE TOAST to instruct acquiring map app
            }

            return true;
        }


        return super.onOptionsItemSelected(item);
    }


}
