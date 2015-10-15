package com.example.shaza.agriquad;

import com.example.shaza.agriquad.util.SystemUiHider;
import com.integreight.onesheeld.sdk.OneSheeldConnectionCallback;
import com.integreight.onesheeld.sdk.OneSheeldDevice;
import com.integreight.onesheeld.sdk.OneSheeldManager;
import com.integreight.onesheeld.sdk.OneSheeldScanningCallback;
import com.integreight.onesheeld.sdk.OneSheeldSdk;
import com.integreight.onesheeld.sdk.ShieldFrame;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class OnFlight extends Activity implements SensorEventListener, LocationListener {
// Used for receiving notifications from the SensorManager when sensor values have changed.
private SensorManager mSensorManager;
private Sensor mPressure;
private Sensor mtemp;
private Sensor mAccelerometer;
private Sensor mgyroscope;
private Sensor morientation;
private LocationManager locationManager;
Location location; // location
float latitude; // latitude
float longitude; // longitude
private ShieldFrame frame;
float oldInput_pressure = 0;
float oldInput_temp = 0;
public static final byte ACCELEROMETER_VALUE = 0x01;
float oldInput_xacc = 0, oldInput_yacc = 0, oldInput_zacc = 0;
float oldInput_xgyr = 0, oldInput_ygyr = 0, oldInput_zgyr = 0;
float azimuth_angle = 0, pitch_angle = 0, roll_angle = 0;
Intent service;
AlarmManager alarm;
PendingIntent pintent;




    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 15000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_on_flight);

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content_controls);
        OneSheeldSdk.init(this);
        OneSheeldSdk.setDebugging(true);
        OneSheeldManager manager = OneSheeldSdk.getManager();
        manager.setConnectionRetryCount(1);
        manager.setAutomaticConnectingRetries(true);
        OneSheeldScanningCallback scanningCallback = new OneSheeldScanningCallback() {
            @Override
            public void onDeviceFind(OneSheeldDevice device) {
                OneSheeldSdk.getManager().cancelScanning();
                // Connect to the found device
                device.connect();
            }
        };
        OneSheeldConnectionCallback connectionCallback = new OneSheeldConnectionCallback() {
            @Override
            public void onConnect(OneSheeldDevice device) {
                // Output high on pin 13
                device.digitalWrite(13, true);
                // Read the value of pin 12
                boolean isHigh = device.digitalRead(12);

                device.sendShieldFrame(frame);
            }


        };
        manager.addConnectionCallback(connectionCallback);
        manager.addScanningCallback(scanningCallback);
        manager.scan();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        mgyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mtemp = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        morientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1, this);


        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if (mControlsHeight == 0) {
                                mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            controlsView.animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });
    }
        private void sendShieldFrame(ShieldFrame frame){
        }




        // Set up the user interaction to manually show or hide the system UI.
   /*     contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.fly).setOnTouchListener(mDelayHideTouchListener);
    }
*/
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };
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
                17 * 1000, pintent);

        startService(service);

    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = (float) (location.getLatitude());
        longitude = (float) (location.getLongitude());
        Log.d("latitudeeeee", latitude + "");
        Log.d("longitudeeee",longitude  + "");
        Toast.makeText(getBaseContext(), "latitude & longitude ", Toast.LENGTH_LONG).show();
        frame.addArgument(latitude);
        frame.addArgument(longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getBaseContext(), "Gps turned on ", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getBaseContext(), "Gps turned off ", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // accelerometer
        if (event.sensor == mAccelerometer) {
            frame = new ShieldFrame((byte) 0x0B,
                    ACCELEROMETER_VALUE);

            oldInput_xacc = event.values[0];
            oldInput_yacc = event.values[1];
            oldInput_zacc = event.values[2];
            frame.addArgument(event.values[0]);
            frame.addArgument(event.values[1]);
            frame.addArgument(event.values[2]);
            Log.d("Sensor Data of X", event.values[0] + "");
            Log.d("Sensor Data of Y", event.values[1] + "");
            Log.d("Sensor Data of Z", event.values[2] + "");
        }
        // gyroscope
        if (event.sensor == mgyroscope) {
            frame = new ShieldFrame((byte) 0x0E, ACCELEROMETER_VALUE);
            oldInput_xgyr = event.values[0];
            oldInput_ygyr = event.values[1];
            oldInput_zgyr = event.values[2];
            frame.addArgument(event.values[0]);
            frame.addArgument(event.values[1]);
            frame.addArgument(event.values[2]);
            Log.d("Sensor Data of X gyro", event.values[0] + "");
            Log.d("Sensor Data of Y gyro", event.values[1] + "");
            Log.d("Sensor Data of Z gyro", event.values[2] + "");
        }
        if (event.sensor == mtemp) {
            frame = new ShieldFrame((byte) 0x12, ACCELEROMETER_VALUE);

            oldInput_temp = event.values[0];
            frame.addArgument((byte) Math.round(event.values[0]));

            Log.d("Sensor Data of temp", event.values[0] + "");
        }

        if (event.sensor == mPressure) {
            frame = new ShieldFrame((byte) 0x11, ACCELEROMETER_VALUE);
            oldInput_pressure = event.values[0];
            // frame.addByteArgument((byte) Math.round(event.values[0]));
            frame.addArgument(2, Math.round(event.values[0]));
        }
        if (event.sensor == morientation) {
            frame = new ShieldFrame((byte) 0x0E,
                    ACCELEROMETER_VALUE);
            azimuth_angle = event.values[0];
            pitch_angle = event.values[1];
            roll_angle = event.values[2];
            // frame.addByteArgument((byte) Math.round(event.values[0]));
            frame.addArgument(event.values[0]);
            frame.addArgument(event.values[1]);
            frame.addArgument(event.values[2]);
            Log.d("Sensor Data of azimuth", event.values[0] + "");
            Log.d("Sensor Data of pitch", event.values[1] + "");
            Log.d("Sensor Data of roll", event.values[2] + "");

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        mSensorManager.registerListener(this, mPressure, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mtemp, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mgyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, morientation, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mgyroscope);
        mSensorManager.unregisterListener(this, mtemp);
        mSensorManager.unregisterListener(this, mPressure);
        mSensorManager.unregisterListener(this, morientation);
        mSensorManager.unregisterListener(this);
        stopService(service);
        alarm.cancel(pintent);
    }

}
