package com.test.internship;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.util.TimerTask;

import static com.test.internship.User.mStepDetector;
import static com.test.internship.User.timeCounter;
import static com.test.internship.User.timer;
import static com.test.internship.User.tt1;
import static com.test.internship.User.tt2;

public class Setting_Activity extends AppCompatActivity {
    private boolean saveData;
    private boolean isCheck1;
    private boolean isCheck2;
    public int flag_Setting1;
    public int flag_Setting2;
    String phoneNo;
    String name;
    Button btnregister;
    Button btnprotectorList;
    Switch switch1;
    Switch switch2;

    private SharedPreferences appData;
    Context context = this;
    NotificationManager manager;
    NotificationCompat.Builder builder;
    private String CHANNEL_ID = "channel1";
    private String CHANEL_NAME = "Channel1";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        timeCounter=0;
        flag_Setting1=0;
        flag_Setting2=0;
        btnregister = findViewById(R.id.btnregister);
        btnregister.setOnClickListener(listener);

        switch1 = (Switch)findViewById(R.id.switch1);
        switch2 = (Switch)findViewById(R.id.switch2);
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();
        if(saveData){
            switch1.setChecked(isCheck1);
            if(isCheck1){
                tt1 = new TimerTask() {
                    @Override
                    public void run() {
                        flag_Setting1++;
                        Intent intentBattery = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                        int level = intentBattery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                        int scale = intentBattery.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                        float batteryPct = level / (float) scale;
                        int battery = (int) (batteryPct * 100);

                        if (battery < 15) {
                            showNoti();
                            for (int i = 0; i < Register_Activity.index; i++) {
                                phoneNo = Register_Activity.protectorPhone.get(i);
                                //System.out.println(phoneNo);
                                name = Register_Activity.protectorName.get(i);
                                //System.out.println(name); 정상출력됨 -> 전송이 안되고 있음
                                sendSMS(phoneNo, name, 1);
                            }
                        }
                    }
                };
                timer.schedule(tt1, 0, 1800000);
            }
            switch2.setChecked(isCheck2);
            if(isCheck2){
                tt2 = new TimerTask() {
                    @Override
                    public void run() {
                        flag_Setting2++;
                        if (mStepDetector < 20){//20걸음 미만이라면 보호자에게 메세지 보내기
                            for (int i = 0; i < Register_Activity.index; i++) {
                                phoneNo = Register_Activity.protectorPhone.get(i);
                                //System.out.println(phoneNo);
                                name = Register_Activity.protectorName.get(i);
                                //System.out.println(name); 정상출력됨 -> 전송이 안되고 있음
                                sendSMS(phoneNo, name, 2);
                            }
                        }
                    }
                };
                timer.schedule(tt2, 0, 86400000);
                mStepDetector=0;
            }
        }


        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
                if (isChecked){
                    save();
                    tt1 = new TimerTask() {
                        @Override
                        public void run() {
                            flag_Setting1++;
                            Intent intentBattery = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                            int level = intentBattery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                            int scale = intentBattery.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                            float batteryPct = level / (float) scale;
                            int battery = (int) (batteryPct * 100);

                            if (battery < 15) {
                                showNoti();
                                for (int i = 0; i < Register_Activity.index; i++) {
                                    phoneNo = Register_Activity.protectorPhone.get(i);
                                    //System.out.println(phoneNo);
                                    name = Register_Activity.protectorName.get(i);
                                    //System.out.println(name); 정상출력됨 -> 전송이 안되고 있음
                                    sendSMS(phoneNo, name, 1);
                                }
                            }
                        }
                    };
                    timer.schedule(tt1, 0, 1800000);

                }else{
                    if(flag_Setting1!=0){
                        tt1.cancel();
                        flag_Setting1=0;
                    }
                    save();
                }
            }
        });
        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
                if (isChecked){
                    save();
                    tt2 = new TimerTask() {
                        @Override
                        public void run() {
                            flag_Setting2++;
                            if (mStepDetector < 20){//20걸음 미만이라면 보호자에게 메세지 보내기

                                for (int i = 0; i < Register_Activity.index; i++) {
                                    phoneNo = Register_Activity.protectorPhone.get(i);
                                    //System.out.println(phoneNo);
                                    name = Register_Activity.protectorName.get(i);
                                    //System.out.println(name); 정상출력됨 -> 전송이 안되고 있음
                                    sendSMS(phoneNo, name, 2);
                                }
                            }
                            mStepDetector=0;
                            User.save();
                        }
                    };
                    timer.schedule(tt2, 0, 86400000);
                    mStepDetector=0;
                }else{
                    if(flag_Setting2!=0){
                        tt2.cancel();
                        flag_Setting2=0;
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
                    intent = new Intent(getApplicationContext(), Register_Activity.class);
//                    startActivity(intent);
                    break;
//                case R.id.btnprotectorList:
//                    intent = new Intent(getApplicationContext(), ProtectorList.class);
////                    //정보도 같이 들고가도록
////                    intent.putExtra("보호자 이름",Register_Activity.protectorName);
////                    intent.putExtra("보호자 연락처",Register_Activity.protectorPhone);
////                    startActivity(intent);
//                    break;
            }
            if(intent!=null) startActivity(intent);
        }
    };

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
    }
    private void save(){
        SharedPreferences.Editor editor = appData.edit();
        editor.putBoolean("SAVE_LOGIN_DATA", (switch1.isChecked()||switch2.isChecked()));
        editor.putBoolean("CHECK1", switch1.isChecked());
        editor.putBoolean("CHECK2", switch2.isChecked());
        editor.putInt("FLAG_SETTING1", flag_Setting1);
        editor.putInt("FLAG_SETTING2", flag_Setting2);


        editor.apply();

    }
    private void load(){
        saveData = appData.getBoolean("SAVE_LOGIN_DATA", false);
        isCheck1=appData.getBoolean("CHECK1", false);
        isCheck2=appData.getBoolean("CHECK2", false);
        flag_Setting1=appData.getInt("FLAG_SETTING1", 0);
        flag_Setting2=appData.getInt("FLAG_SETTING2", 0);
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
    public void sendSMS(String phoneNo, String name, int flag){
        phoneNo= Register_Activity.protectorPhone.toString();
        name= Register_Activity.protectorName.toString();
        if(flag==1){
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, name + "님 보호대상자의 배터리가 15% 미만입니다 !!", null, null);
                //Toast.makeText(getApplicationContext(), "메세지 전송!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        else {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, name + "님 보호대상자의 일일 걸음 수가 20보 미만입닙다 !!", null, null);
                //Toast.makeText(getApplicationContext(), "메세지 전송!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}


