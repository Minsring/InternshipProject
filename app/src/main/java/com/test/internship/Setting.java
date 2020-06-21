package com.test.internship;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.util.TimerTask;

import static com.test.internship.DistanceRange.nowLatLng;
import static com.test.internship.User.mStepDetector;
import static com.test.internship.User.stepSensor;
import static com.test.internship.User.timer;
import static com.test.internship.User.timerTask1;
import static com.test.internship.User.timerTask2;

public class Setting extends AppCompatActivity {

    private String CHANNEL_ID = "channel1";
    private String CHANNEL_NAME = "Channel1";
    private SharedPreferences appData;
    private boolean saveData;
    private boolean isCheckBattery;
    private boolean isCheckMotion;

    int flagBattery;
    int flagMotion;
    public int num = 0;

    ImageView healthImage;
    Button buttonRegister;
    Switch batterySwitch;
    Switch motionSwitch;

    Context context = this;
    NotificationManager manager;
    NotificationCompat.Builder builder;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        flagBattery = 0;
        flagMotion = 0;

        // 사용자 등록 버튼 리스너
        buttonRegister = findViewById(R.id.btnregister);
        buttonRegister.setOnClickListener(listener);

        // 배터리 스위치와 움직임 스위치 설정
        batterySwitch = findViewById(R.id.batterySwitch);
        motionSwitch = findViewById(R.id.motionSwitch);

        healthImage=findViewById(R.id.health_image);
        healthImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.mohw.go.kr/react/policy/index.jsp?PAR_MENU_ID=06&MENU_ID=06390104&PAGE=4&topTitle=노인맞춤돌봄서비스"));
                startActivity(intent);
            }
        });

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();
        if(saveData){
            batterySwitch.setChecked(isCheckBattery);
            if(isCheckBattery){
                timerTask1 = new TimerTask() {
                    @Override
                    public void run() {
                        flagBattery++;
                        Intent intentBattery = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                        int level = intentBattery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                        int scale = intentBattery.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                        float batteryPct = level / (float) scale;
                        int battery = (int) (batteryPct * 100);

                        if (battery < 15) {
                            showNoti(1);
                            for(ProtectorData protectorData: Register.personData){
                                sendSMS(protectorData.personNum, protectorData.personName, 1);
                            }
                        }
                    }
                };
                timer.schedule(timerTask1, 0, 1800000);
            }
            motionSwitch.setChecked(isCheckMotion);
            if(isCheckMotion){
                mStepDetector=30;
                timerTask2 = new TimerTask() {
                    @Override
                    public void run() {
                        flagMotion++;
                        if (mStepDetector < 20){ //20걸음 미만이라면 보호자에게 메세지 보내기
                            for(ProtectorData protectorData: Register.personData){
                                sendSMS(protectorData.personNum, protectorData.personName, 2);
                            }
                        }
                    }
                };
                timer.schedule(timerTask2, 0, 86400000);
                mStepDetector=0;
            }
        }


        batterySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked){
                    save();
                    if(num==0) {
                        Toast.makeText(getApplicationContext(), "보호자 정보를 먼저 등록해주세요.", Toast.LENGTH_SHORT).show();
                        batterySwitch.setChecked(false);
                    }
                    else{
                        timerTask1 = new TimerTask() {
                            @Override
                            public void run() {
                                flagBattery++;
                                Intent intentBattery = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                                int level = intentBattery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                                int scale = intentBattery.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                                float batteryPct = level / (float) scale;
                                int battery = (int) (batteryPct * 100);

                                if (battery < 15) {
                                    showNoti(1);
                                    for(ProtectorData protectorData: Register.personData){
                                        sendSMS(protectorData.personNum, protectorData.personName, 1);
                                    }
                                }
                            }
                        };
                        timer.schedule(timerTask1, 0, 1800000);
                    }


                }else{
                    if(flagBattery!=0){
                        timerTask1.cancel();
                        flagBattery=0;
                    }
                    save();
                }
            }
        });

        motionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked){
                    save();
                    if(num==0) {
                        Toast.makeText(getApplicationContext(), "보호자 정보를 먼저 등록해주세요.", Toast.LENGTH_SHORT).show();
                        motionSwitch.setChecked(false);
                    }
                    else{

                        if(stepSensor == null) Toast.makeText(Setting.this, "걸음감지 센서가 없습니다.", Toast.LENGTH_SHORT).show();
                        else{
                            mStepDetector=30;
                            timerTask2 = new TimerTask() {
                                @Override
                                public void run() {
                                    flagMotion++;
                                    if (mStepDetector < 20){//20걸음 미만이라면 보호자에게 메세지 보내기
                                        showNoti(2);
                                        for(ProtectorData protectorData: Register.personData){
                                            sendSMS(protectorData.personNum, protectorData.personName, 2);
                                        }
                                    }
                                    mStepDetector=0;
                                    save();
                                }
                            };
                            timer.schedule(timerTask2, 0, 86400000);
                            mStepDetector=0;
                        }

                    }

                }else{
                    if(flagMotion!=0){
                        timerTask2.cancel();
                        flagMotion=0;
                    }
                    save();
                }
            }
        });
    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.btnregister:
                    intent = new Intent(getApplicationContext(), Register.class);
                    break;
            }
            if(intent!=null) startActivity(intent);
        }
    };


    private void save(){
        SharedPreferences.Editor editor = appData.edit();
        editor.putBoolean("SAVE_LOGIN_DATA", (batterySwitch.isChecked()||motionSwitch.isChecked()));
        editor.putBoolean("CHECK1", batterySwitch.isChecked());
        editor.putBoolean("CHECK2", motionSwitch.isChecked());
        editor.putInt("FLAG_SETTING1", flagBattery);
        editor.putInt("FLAG_SETTING2", flagMotion);

        editor.apply();
    }
    private void load(){
        saveData = appData.getBoolean("SAVE_LOGIN_DATA", false);
        isCheckBattery=appData.getBoolean("CHECK1", false);
        isCheckMotion=appData.getBoolean("CHECK2", false);
        flagBattery=appData.getInt("FLAG_SETTING1", 0);
        flagMotion=appData.getInt("FLAG_SETTING2", 0);
        num=appData.getInt("NUM",0);
        if(num<0) num=0;
    }

    //알림창 실행
    public void showNoti(int flag){
        builder = null;
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //버전 오레오 이상일 경우
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            manager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT));
            builder = new NotificationCompat.Builder(this,CHANNEL_ID);
        }
        //하위 버전일 경우
        else{
            builder = new NotificationCompat.Builder(this);
        }
        if(flag==1) {
            builder.setContentTitle("배터리 부족 알림");
            builder.setContentText("배터리 부족!! 보호자에 알림을 전송합니다.");
            builder.setSmallIcon(R.mipmap.app_icon);
            Notification notification = builder.build();
            manager.notify(1, notification);
        }
        else{
            builder.setContentTitle("걸음수 부족 알림");
            builder.setContentText("걸음수 20걸음 미만!! 보호자에 알림을 전송합니다.");
            builder.setSmallIcon(R.mipmap.app_icon);
            Notification notification = builder.build();
            manager.notify(1, notification);
        }
    }

    //문자전송
    public static void sendSMS(String phoneNo, String name, int flag){
        if(flag==1){
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, name + "님 보호대상자의 휴대전화 배터리가 15% 미만입니다 !!", null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(flag==2){
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, name + "님 보호대상자의 일일 걸음 수가 20보 미만입니다 !!", null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (flag==3){
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, name + "님 보호대상자님께서 설정한 이동 반경을 벗어나셨습니다 !!\n 현재 위도: "+ nowLatLng.latitude+", 경도: "+nowLatLng.longitude, null, null);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        load();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        load();
    }
}

