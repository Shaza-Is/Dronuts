package com.example.samy.connect;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.integreight.onesheeld.sdk.OneSheeldConnectionCallback;
import com.integreight.onesheeld.sdk.OneSheeldDevice;
import com.integreight.onesheeld.sdk.OneSheeldManager;
import com.integreight.onesheeld.sdk.OneSheeldScanningCallback;
import com.integreight.onesheeld.sdk.OneSheeldSdk;
import com.integreight.onesheeld.sdk.ShieldFrame;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {
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
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



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
              /*  device.addDataCallback(new OneSheeldDataCallback() {
                    private android.hardware.Camera.PictureCallback mPicture = new android.hardware.Camera.PictureCallback() {
                        @Override
                        public void onPictureTaken(byte[] data, android.hardware.Camera camera) {
                            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                            if (pictureFile == null){
                              //  Log.d(TAG, "Error creating media file, check storage permissions: " +
                                     //   e.getMessage());
                                Toast.makeText(getBaseContext(), " Error creating media file, check storage permissions", Toast.LENGTH_LONG).show();
                                return;
                            }
                            try {
                                FileOutputStream fos = new FileOutputStream(pictureFile);
                                fos.write(data);
                                fos.close();
                            } catch (FileNotFoundException e) {
                              //  Log.d( "File not found: " + e.getMessage());
                                Toast.makeText(getBaseContext(), "File not found ", Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                              //  Log.d( "Error accessing file: " + e.getMessage());
                                Toast.makeText(getBaseContext(), "Error accessing file ", Toast.LENGTH_LONG).show();
                            }
                        }
                    };
                });*/
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
        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 3000, 1, this);
    }




    private void sendShieldFrame(ShieldFrame frame) {
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                17 * 1000, pintent);

        startService(service);

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
        // Do something here if sensor accuracy changes.
    }



    @Override
    public void onLocationChanged(Location location) {
         latitude = (float) (location.getLatitude());
         longitude = (float) (location.getLongitude());
        Log.d("latitudeeeee",latitude  + "");
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
