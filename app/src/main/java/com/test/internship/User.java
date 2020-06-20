package com.test.internship;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TableLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Timer;
import java.util.TimerTask;

@RequiresApi(api = Build.VERSION_CODES.M)
public class User extends AppCompatActivity implements SensorEventListener {

    Button allSub, entSub, internalSub, obstSub, eyeSub, boneSub, neuroSub, childSub, dentalSub, skinSub,
            hanSub, binyoSub, bogun, chkCenter, emergencyRoom, contactProtector, fence;
    private static SharedPreferences appData;
    private SensorManager sensorManager;
    static Sensor stepSensor;
    static String subject;
    static Timer timer;
    static TimerTask timerTask1;
    static TimerTask timerTask2;
    static int mStepDetector;
    private final int MY_PERMISSION_REQUEST_SMS=1001;

    TableLayout tableLayout;
    Animation anim1, anim2, anim3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();

        if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            getWindow().setStatusBarColor(Color.WHITE);
        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);


        // 버튼 연결
        allSub = findViewById(R.id.allSub);                     // 전체
        hanSub = findViewById(R.id.hanSub);                     // 한의원
        binyoSub = findViewById(R.id.binyoSub);                 // 비뇨기과
        entSub = findViewById(R.id.entSub);                     // 이비인후과
        internalSub = findViewById(R.id.internalSub);           // 내과
        obstSub = findViewById(R.id.obstSub);                   // 산부인과
        eyeSub = findViewById(R.id.eyeSub);                     // 안과
        boneSub = findViewById(R.id.boneSub);                   // 정형외과
        neuroSub = findViewById(R.id.neuroSub);                 // 신경외과
        childSub = findViewById(R.id.childSub);                 // 소아청소년과
        dentalSub = findViewById(R.id.dentalSub);               // 치과
        skinSub = findViewById(R.id.skinSub);                   // 피부과
        bogun = findViewById(R.id.bogun);                       // 보건소
        chkCenter = findViewById(R.id.chkCenter);               // 건강검진센터
        emergencyRoom = findViewById(R.id.emergencyRoom);       // 응급실

        contactProtector = findViewById(R.id.contactProtector);
        fence = findViewById(R.id.fence);

        // 리스너
        allSub.setOnClickListener(listener);
        hanSub.setOnClickListener(listener);
        binyoSub.setOnClickListener(listener);
        entSub.setOnClickListener(listener);
        internalSub.setOnClickListener(listener);
        obstSub.setOnClickListener(listener);
        eyeSub.setOnClickListener(listener);
        boneSub.setOnClickListener(listener);
        neuroSub.setOnClickListener(listener);
        childSub.setOnClickListener(listener);
        dentalSub.setOnClickListener(listener);
        skinSub.setOnClickListener(listener);
        bogun.setOnClickListener(listener);
        chkCenter.setOnClickListener(listener);
        emergencyRoom.setOnClickListener(listener);
        contactProtector.setOnClickListener(listener);
        fence.setOnClickListener(listener);

        // 애니메이션 연결
        anim1 = AnimationUtils.loadAnimation(this, R.anim.anim1);
        anim2 = AnimationUtils.loadAnimation(this, R.anim.anim2);
        anim3 = AnimationUtils.loadAnimation(this, R.anim.anim3);

        // 애니메이션
        tableLayout = findViewById(R.id.tableLayout);
        tableLayout.startAnimation(anim1);
        contactProtector.startAnimation(anim2);
        fence.startAnimation(anim3);

        // 타이머
        timer = new Timer();

        //sms 허가
        if (ContextCompat.checkSelfPermission(User.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(User.this, Manifest.permission.SEND_SMS)) {
            }
            else {
                ActivityCompat.requestPermissions(User.this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSION_REQUEST_SMS);
            }
        }
    }

    // 각 버튼 별 처리
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.allSub:
                    subject="모든 병원";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
                    break;
                case R.id.hanSub:
                    subject = "한의원";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
                    break;
                case R.id.binyoSub:
                    subject = "비뇨기과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
                    break;
                case R.id.entSub:
                    subject = "이비인후과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
                    break;
                case R.id.internalSub:
                    subject = "내과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
                    break;
                case R.id.obstSub:
                    subject = "산부인과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
                    break;
                case R.id.eyeSub:
                    subject = "안과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
                    break;
                case R.id.boneSub:
                    subject = "정형외과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
                    break;
                case R.id.neuroSub:
                    subject = "신경외과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
                    break;
                case R.id.childSub:
                    subject = "소아청소년과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
                    break;
                case R.id.dentalSub:
                    subject = "치과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
                    break;
                case R.id.skinSub:
                    subject = "피부과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
                    break;
                case R.id.bogun:
                    subject = "보건소";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
                    break;
                case R.id.chkCenter:
                    subject = "건강검진센터";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
                    break;
                case R.id.emergencyRoom:
                    subject = "응급실";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
                    break;
                case R.id.contactProtector:
                    intent = new Intent(getApplicationContext(), Setting.class);
                    break;
                case R.id.fence:
                    intent = new Intent(getApplicationContext(), DistanceRange.class);
                    break;
            }
            if(intent != null) startActivity(intent);
        }
    };

    public static void save() {
        SharedPreferences.Editor editor = appData.edit();
        editor.putInt("SAVE_STEP_DATA", mStepDetector);
        editor.apply();
    }
    private void load() {
        mStepDetector = appData.getInt("SAVE_STEP_DATA", 0);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_STEP_DETECTOR){
            if(event.values[0]==1.0f){
                mStepDetector++;
                save();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
}