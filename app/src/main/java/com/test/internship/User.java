package com.test.internship;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.Timer;
import java.util.TimerTask;


@RequiresApi(api = Build.VERSION_CODES.M)
public class User extends AppCompatActivity implements SensorEventListener {

    Button allSub, entSub, internalSub, obstSub, eyeSub, boneSub, neuroSub, childSub, dentalSub, skinSub, hanSub, binyoSub, bogun, chkCenter, emergencyRoom, setting, btnregister;
    private SensorManager sensorManager;
    private Sensor stepsensor;
    private int mStepDetector;
    static String subject;
    static Timer timer;
    static TimerTask tt;

    Context context = this;
    NotificationManager manager;
    NotificationCompat.Builder builder;

    private static String CHANNEL_ID = "channel1";
    private static String CHANEL_NAME = "Channel1";

    String phoneNo;
    String name;
    private final int MY_PERMISSION_REQUEST_SMS=1001;

    //    TODO: 위치 기반 동의, 개인정보 보호 약관..?
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepsensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (stepsensor == null) {
            Toast.makeText(this, "센서 없어", Toast.LENGTH_LONG).show();
            System.out.println("센서 없어");
        } else {
            Toast.makeText(this, "센서 있어", Toast.LENGTH_LONG).show();
            System.out.println("센서 있어");
        }

        //문자 기능
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.SEND_SMS)){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("알림");
                builder.setMessage("이 어플리케이션은 SMS 접근을 허용하지 않으면 작동하지 않습니다.");
                builder.setIcon(R.drawable.ic_launcher_foreground);

                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(User.this, new String[]{Manifest.permission.SEND_SMS},MY_PERMISSION_REQUEST_SMS);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else{
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.SEND_SMS},MY_PERMISSION_REQUEST_SMS);
            }
        }


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

        setting = findViewById(R.id.setting);                   // 설정

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
        setting.setOnClickListener(listener);

        tt = new TimerTask() {
            @Override
            public void run() {
                Intent intentBattery = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                int level = intentBattery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intentBattery.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                float batteryPct = level / (float) scale;
                int battery = (int) (batteryPct * 100);

                if (battery < 15) {
                    Log.d("배터리 부족알림", "배터리 부족! 보호자에게 알림을 보냅니다.");
                    showNoti();

                    for (int i = 0; i < Register_Activity.index; i++) {
                        phoneNo = Register_Activity.protectorPhone.get(i);
                        //System.out.println(phoneNo);
                        name = Register_Activity.protectorName.get(i);
                        //System.out.println(name); 정상출력됨 -> 전송이 안되고 있음
                        sendSMS(phoneNo, name);
                    }
                }
            }
        };
        timer = new Timer();
        //timer.schedule(tt, 0, 5000);
    }

    //알림창 실행
    public void showNoti(){
        builder = null;
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //버전 오레오 이상일 경우
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            manager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            ); builder = new NotificationCompat.Builder(this,CHANNEL_ID);
            //하위 버전일 경우
        }else{ builder = new NotificationCompat.Builder(this)
        ; }
        //알림창 제목
        builder.setContentTitle("배터리 부족 알림");
        // 알림창 메시지
        builder.setContentText("배터리 부족!! 보호자에 알림을 전송하겠습니다.");
        //알림창 아이콘
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        Notification notification = builder.build();
        //알림창 실행
        manager.notify(1,notification);
    }

    //문자전송
    public void sendSMS(String phoneNo, String name){
        phoneNo= Register_Activity.protectorPhone.toString();
        name= Register_Activity.protectorName.toString();
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, name + "님 보호대상자의 배터리가 15% 미만입니다 !!", null, null);
            Toast.makeText(getApplicationContext(), "메세지 전송!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch(requestCode){
            case MY_PERMISSION_REQUEST_SMS:{
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"허용",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"거부",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // TODO: 각 버튼 별 처리
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.allSub:
                    subject="모든 병원";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
//                    startActivity(intent);
                    break;
                case R.id.hanSub:
                    subject = "한의원";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
//                    startActivity(intent);
                    break;
                case R.id.binyoSub:
                    subject = "비뇨기과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
//                    startActivity(intent);
                case R.id.entSub:
                    subject = "이비인후과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
//                    startActivity(intent);
                    break;
                case R.id.internalSub:
                    subject = "내과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
//                    startActivity(intent);
                    break;
                case R.id.obstSub:
                    subject = "산부인과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
//                    startActivity(intent);
                    break;
                case R.id.eyeSub:
                    subject = "안과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
//                    startActivity(intent);
                    break;
                case R.id.boneSub:
                    subject = "정형외과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
//                    startActivity(intent);
                    break;
                case R.id.neuroSub:
                    subject = "신경외과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
//                    startActivity(intent);
                    break;
                case R.id.childSub:
                    subject = "소아청소년과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
//                    startActivity(intent);
                    break;
                case R.id.dentalSub:
                    subject = "치과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
//                    startActivity(intent);
                    break;
                case R.id.skinSub:
                    subject = "피부과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
//                    startActivity(intent);
                    break;
                case R.id.bogun:
                    subject = "보건소";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
//                    startActivity(intent);
                    break;
                case R.id.chkCenter:
                    subject = "건강검진센터";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
//                    startActivity(intent);
                    break;
                case R.id.emergencyRoom:
                    // 화면 전환 or 대화상자로 바로 연결할까영? 한번 물은 뒤 연결
                    subject = "응급실";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    intent.putExtra("진료과", subject);
//                    startActivity(intent);
                    break;
                case R.id.setting:
                    intent = new Intent(getApplicationContext(), Setting_Activity.class);
//                    startActivity(intent);
                    break;
            }
            if(intent != null) startActivity(intent);    // 다른 처리 없다면 여기서 한번에 화면 전환
        }
    };
    @Override
    public void onResume() {
        super.onResume();
    //    sensorManager.registerListener(this, stepsensor, SensorManager.SENSOR_DELAY_UI);
    }
    @Override
    public void onPause() {
        super.onPause();
       // sensorManager.registerListener(this, stepsensor, SensorManager.SENSOR_DELAY_UI);
    }
    @Override
    public void onStart() {
        super.onStart();
        sensorManager.registerListener(this, stepsensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_STEP_DETECTOR){
            if(event.values[0]==1.0f){
                mStepDetector++;
                System.out.println(String.valueOf(mStepDetector));
                Toast.makeText(getApplicationContext(), "걸음 수: "+String.valueOf(mStepDetector), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}