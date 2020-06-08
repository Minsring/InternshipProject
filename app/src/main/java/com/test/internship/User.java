package com.test.internship;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
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

import java.util.Timer;
import java.util.TimerTask;


public class User extends AppCompatActivity {

    Button allSub, entSub, internalSub, obstSub, eyeSub, boneSub, neuroSub, childSub, dentalSub, skinSub
            ,hanSub, binyoSub, bogun, chkCenter, emergencyRoom, setting, btnregister;

    static String subject;
    static Timer timer;
    static TimerTask tt;

    Context context = this;

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

        // 윤모꺼
        tt = new TimerTask() {
            @Override
            public void run() {
                System.out.println("1");
                Intent intentBattery = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                int level = intentBattery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intentBattery.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                float batteryPct = level / (float)scale;
                int battery = (int)(batteryPct * 100);

                if(battery<15) Log.d("배터리 부족알림","배터리 부족! 보호자에게 알림을 보냅니다");
            }
       };
        timer = new Timer();
        //timer.schedule(tt, 0, 5000);
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
                    subject = "응급실";
                    intent = new Intent(getApplicationContext(), SubjectList.class);
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
}