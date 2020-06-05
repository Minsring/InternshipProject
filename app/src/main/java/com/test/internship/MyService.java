package com.test.internship;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service implements SensorEventListener {
    private static final String TAG = "Myservice";
    private SensorManager sensorManager;
    private Sensor stepsensor;
    private int mStepDetector;
    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()...");
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        stepsensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if(stepsensor==null)
            Toast.makeText(this, "센서 없어", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand()...");

        if(intent==null)
            return Service.START_NOT_STICKY;
        else{
            String command = intent.getStringExtra("command");
            String name = intent.getStringExtra("name");

            //Log.d(TAG, "전달받은 데이터: " + command+ ", " +name);

            try{
                Thread.sleep(5000); //5초동안 정지
            } catch(Exception e) {}

            Intent showIntent = new Intent(getApplicationContext(), User.class);

            /**
             화면이 띄워진 상황에서 다른 액티비티를 호출하는 것은 문제가없으나,
             지금은 따로 띄워진 화면이 없는 상태에서 백그라운드에서 실행중인 서비스가 액티비티를 호출하는 상황이다.
             이러한 경우에는 FLAG_ACTIVITY_NEW_TASK 옵션(플래그)을 사용해줘야 한다.
             **/

            showIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            showIntent.putExtra("command", "show");
            showIntent.putExtra("name", name + " from service.");

            // *** 이제 완성된 인텐트와 startActivity()메소드를 사용하여 MainActivity 액티비티를 호출한다. ***
            startActivity(showIntent);
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_STEP_DETECTOR){
            if(event.values[0]==1.0f){
                mStepDetector++;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
