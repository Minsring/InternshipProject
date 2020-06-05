package com.test.internship;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class User extends AppCompatActivity {

    Button allSub, entSub, internalSub, obstSub, eyeSub, boneSub, neuroSub, childSub, dentalSub, skinSub
            ,hanSub, binyoSub, bogun, chkCenter, emergencyRoom, setting, testHospitalInfo, btnregister;

    static String subject;

//    TODO: 위치 기반 동의, 개인정보 보호 약관..?
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);

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
        testHospitalInfo = findViewById(R.id.testHospitalInfo); // Test용

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
        testHospitalInfo.setOnClickListener(listener);

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
//                    startActivity(intent);
                    break;
                case R.id.hanSub:
                    subject = "한의원";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
                    // intent에 정보 담아서 넘기기
//                    startActivity(intent);
                    break;
                case R.id.binyoSub:
                    subject = "비뇨기과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
//                    startActivity(intent);
                case R.id.entSub:
                    subject = "이비인후과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
//                    startActivity(intent);
                    break;
                case R.id.internalSub:
                    subject = "내과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
//                    startActivity(intent);
                    break;
                case R.id.obstSub:
                    subject = "산부인과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
//                    startActivity(intent);
                    break;
                case R.id.eyeSub:
                    subject = "안과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
//                    startActivity(intent);
                    break;
                case R.id.boneSub:
                    subject = "정형외과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
//                    startActivity(intent);
                    break;
                case R.id.neuroSub:
                    subject = "신경외과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
//                    startActivity(intent);
                    break;
                case R.id.childSub:
                    subject = "소아청소년과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
//                    startActivity(intent);
                    break;
                case R.id.dentalSub:
                    subject = "치과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
//                    startActivity(intent);
                    break;
                case R.id.skinSub:
                    subject = "피부과";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
//                    startActivity(intent);
                    break;
                case R.id.bogun:
                    subject = "보건소";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
//                    startActivity(intent);
                    break;
                case R.id.chkCenter:
                    subject = "건강검진센터";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
//                    startActivity(intent);
                    break;
                case R.id.emergencyRoom:
                    // 화면 전환 or 대화상자로 바로 연결할까영? 한번 물은 뒤 연결
//                    intent = new Intent(getApplicationContext(), EmergencyRoom_Activity.class);
//                    startActivity(intent);
                    break;
                case R.id.setting:
                    intent = new Intent(getApplicationContext(), Setting_Activity.class);
//                    startActivity(intent);
                    break;
                case R.id.testHospitalInfo:
                    intent = new Intent(getApplicationContext(), TestHospitalInfo_Activity.class);
//                    startActivity(intent);
                    break;

            }
            if(intent != null) startActivity(intent);    // 다른 처리 없다면 여기서 한번에 화면 전환
        }
    };


    private static final String TAG = "Exam_MainActivity";

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(Intent.ACTION_BATTERY_CHANGED.equals(action)){
                int health = intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN);
                int level = intent.getIntExtra("level", 0);
                int plug = intent.getIntExtra("plugged", 0);
                int scale = intent.getIntExtra("scale", 100);
                int status = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);
                String technology = intent.getStringExtra("technology");
                int temperature = intent.getIntExtra("temperature", 0);
                int voltage = intent.getIntExtra("voltage", 0);

                // health
                if(health == BatteryManager.BATTERY_HEALTH_COLD){
                    Log.i(TAG, "health cold");
                } else if(health == BatteryManager.BATTERY_HEALTH_DEAD){
                    Log.i(TAG, "health dead");
                } else if(health == BatteryManager.BATTERY_HEALTH_GOOD){
                    Log.i(TAG, "health good");
                } else if(health == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE){
                    Log.i(TAG, "health over voltage");
                } else if(health == BatteryManager.BATTERY_HEALTH_OVERHEAT){
                    Log.i(TAG, "health overheat");
                } else if(health == BatteryManager.BATTERY_HEALTH_UNKNOWN){
                    Log.i(TAG, "health unknown");
                } else if(health == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE){
                    Log.i(TAG, "health unspecified failure");
                }

                // 배터리 잔량 확인
                Log.i(TAG, "Charge : " + (level * 100 / scale));

                // 충전 방식
                if(plug == 0){
                    Log.i(TAG, "PlugType : unplugged");
                } else {
                    if((plug & BatteryManager.BATTERY_PLUGGED_AC) != 0){
                        Log.i(TAG, "PlugType : AC");
                    }

                    if((plug & BatteryManager.BATTERY_PLUGGED_USB) != 0){
                        Log.i(TAG, "PlugType : USB");
                    }

                    if((plug & BatteryManager.BATTERY_PLUGGED_WIRELESS) != 0){
                        Log.i(TAG, "PlugType : WIRELESS");
                    }
                }

                // 배터리 상태
                if(status == BatteryManager.BATTERY_STATUS_CHARGING){
                    Log.i(TAG, "Status : Charging");
                } else if(status == BatteryManager.BATTERY_STATUS_DISCHARGING){
                    Log.i(TAG, "Status : Discharging");
                } else if(status == BatteryManager.BATTERY_STATUS_FULL){
                    Log.i(TAG, "Status : Full");
                } else if(status == BatteryManager.BATTERY_STATUS_NOT_CHARGING){
                    Log.i(TAG, "Status : Not charging");
                } else if(status == BatteryManager.BATTERY_STATUS_UNKNOWN){
                    Log.i(TAG, "Status : Unknown");
                }

                // 배터리 기술에 대해 설명
                Log.i(TAG, "Technology : " + technology);

                // 배터리 온도
                Log.i(TAG, "Temperature : " + temperature);

                // 배터리 전압
                Log.i(TAG, "Voltage : " + voltage);
            }
        }
    };
    protected void onResume(){
        super.onResume();
        // 배터리 상태 변화에 대한 receiver 등록
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    protected void onPause(){
        super.onPause();
        // receiver 해제
        unregisterReceiver(receiver);
    }

    public void clickMethod(View v){
        sendBroadcast(new Intent("ACTION_BATTERY_LOW"));
    }

}