package com.example.samy.camerathread;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    Intent service;
    AlarmManager alarm;
    PendingIntent pintent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void toast1(){
        Toast.makeText(getApplicationContext(), "msg msg", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(service);
        alarm.cancel(pintent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Calendar cal = Calendar.getInstance();

        service = new Intent(getBaseContext(), CapPhoto.class);
        cal.add(Calendar.SECOND, 15);
        //TAKE PHOTO EVERY 15 SECONDS
        pintent = PendingIntent.getService(this, 0, service, 0);
        alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                60 * 1000, pintent);
        startService(service);
    }
}
