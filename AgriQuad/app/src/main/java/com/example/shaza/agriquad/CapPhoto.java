package com.example.shaza.agriquad;

import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Samy on 07-Oct-15.
 */
public class CapPhoto extends Service {
    private SurfaceHolder sHolder;
    private Camera mCamera;
    private Camera.Parameters parameters;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
/*    public CapPhoto(String name) {
        super(name);
    }
    public CapPhoto() {
        super("SensingService");
        setIntentRedelivery(true);
    } */


    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.d("CAM", "start");

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);};
        Thread myThread = null;


    }
    @Override
    public void onStart(Intent intent, int startId) {

        super.onStart(intent, startId);

        if (Camera.getNumberOfCameras() >= 2) {
            mCamera = Camera.open();
           // mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
           }

        if (Camera.getNumberOfCameras() < 2) {

            mCamera = Camera.open(); }
        SurfaceView sv = new SurfaceView(getApplicationContext());


        try {
            mCamera.setPreviewDisplay(sv.getHolder());
            parameters = mCamera.getParameters();
            mCamera.setParameters(parameters);
            mCamera.startPreview();

            mCamera.takePicture(null, null, mCall);
        } catch (IOException e) { e.printStackTrace(); }

        sHolder = sv.getHolder();
        sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

/*    @Override
    protected void onHandleIntent(Intent intent) {

    } */

    Camera.PictureCallback mCall = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            FileOutputStream outStream = null;

            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/Drounts");
            if(!myDir.exists()) {
                myDir.mkdirs();
                Log.i("FO", "folder" + myDir);
            }

            try{

                File sd = new File(myDir, "A");


                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String tar = (sdf.format(cal.getTime()));

                outStream = new FileOutputStream(sd+tar+".jpg");
                outStream.write(data);  outStream.close();

                Log.i("CAM", data.length + " byte written to:" + sd + tar + ".jpg");
                camkapa(sHolder);


            } catch (FileNotFoundException e){
                Log.d("CAM", e.getMessage());
            } catch (IOException e){
                Log.d("CAM", e.getMessage());
            }}
    };


  @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void camkapa(SurfaceHolder sHolder) {

        if (null == mCamera)
            return;
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        Log.i("CAM", " closed");
    }
    private void galleryAddPic(File imageFile) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        getApplicationContext().sendBroadcast(mediaScanIntent);
    }


}
