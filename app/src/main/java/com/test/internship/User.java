package com.test.internship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class User extends AppCompatActivity {

    Button allSub, entSub, internalSub, obstSub, eyeSub, boneSub, neuroSub, childSub, dentalSub, skinSub
            ,hanSub, binyoSub, bogun, chkCenter, emergencyRoom, setting, testHospitalInfo;

//    TODO: 위치 기반 동의, 개인정보 보호 약관..?
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 버튼 연결
        allSub = findViewById(R.id.allSub);
        hanSub = findViewById(R.id.hanSub);
        binyoSub = findViewById(R.id.binyoSub);
        entSub = findViewById(R.id.entSub);
        internalSub = findViewById(R.id.internalSub);
        obstSub = findViewById(R.id.obstSub);
        eyeSub = findViewById(R.id.eyeSub);
        boneSub = findViewById(R.id.boneSub);
        neuroSub = findViewById(R.id.neuroSub);
        childSub = findViewById(R.id.childSub);
        dentalSub = findViewById(R.id.dentalSub);
        skinSub = findViewById(R.id.skinSub);
        bogun = findViewById(R.id.bogun);
        chkCenter = findViewById(R.id.chkCenter);
        emergencyRoom = findViewById(R.id.emergencyRoom);
        setting = findViewById(R.id.setting);
        testHospitalInfo = findViewById(R.id.testHospitalInfo);

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
                    intent = new Intent(getApplicationContext(), SubjectList.class);
//                    startActivity(intent);
                    break;
                case R.id.hanSub:
//                    intent = new Intent(getApplicationContext(), HanSub_Activity.class);
//                    startActivity(intent);
                    break;
                case R.id.binyoSub:
//                    intent = new Intent(getApplicationContext(), BinyoSub_Activity.class);
//                    startActivity(intent);
                case R.id.entSub:
//                    intent = new Intent(getApplicationContext(), ENTSub_Activity.class);
//                    startActivity(intent);
                    break;
                case R.id.internalSub:
//                    intent = new Intent(getApplicationContext(), InternalSub_Activity.class);
//                    startActivity(intent);
                    break;
                case R.id.obstSub:
//                    intent = new Intent(getApplicationContext(), ObstSub_Activity.class);
//                    startActivity(intent);
                    break;
                case R.id.eyeSub:
//                    intent = new Intent(getApplicationContext(), EyeSub_Activity.class);
//                    startActivity(intent);
                    break;
                case R.id.boneSub:
//                    intent = new Intent(getApplicationContext(), BoneSub_Activity.class);
//                    startActivity(intent);
                    break;
                case R.id.neuroSub:
//                    intent = new Intent(getApplicationContext(), NeuroSub_Activity.class);
//                    startActivity(intent);
                    break;
                case R.id.childSub:
//                    intent = new Intent(getApplicationContext(), ChildSub_Activity.class);
//                    startActivity(intent);
                    break;
                case R.id.dentalSub:
//                    intent = new Intent(getApplicationContext(), DentalSub_Activity.class);
//                    startActivity(intent);
                    break;
                case R.id.skinSub:
//                    intent = new Intent(getApplicationContext(), SkinSub_Activity.class);
//                    startActivity(intent);
                    break;
                case R.id.bogun:
//                    intent = new Intent(getApplicationContext(), Bogun_Activity.class);
//                    startActivity(intent);
                    break;
                case R.id.chkCenter:
//                    intent = new Intent(getApplicationContext(), ChkCenter_Activity.class);
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
}