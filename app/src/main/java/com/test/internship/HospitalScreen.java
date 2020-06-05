package com.test.internship;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class HospitalScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital_screen);
        Button callHospital=findViewById(R.id.callHospitalBtn);
        Intent getIntent = getIntent();
        final HospitalInformation hospital = (HospitalInformation) getIntent.getSerializableExtra("병원");

        Toast.makeText(getApplicationContext(), "병원이름: "+hospital.getHospitalName(), Toast.LENGTH_LONG).show();

        callHospital.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String number=hospital.getCallNumber(); //이거받으려면 hospital이 final되야한대
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+number)); //다이얼로 연결
                startActivity(intent);
            }
        });


        // 탭 레이아웃 설정
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        changeView(0);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            // 선택 안된 상태에서 선택 된 상태의 탭에 대한 이벤트
            public void onTabSelected(TabLayout.Tab tab){
                int pos= tab.getPosition();
                changeView(pos);
            }

            @Override
            // 선택된 상태에서 선택되지 않음으로 바뀐 탭에 대한 이벤트
            public void onTabUnselected(TabLayout.Tab tab){

            }

            @Override
            // 이미 선택된 탭이 사용자에 의해 다시 선택된 탭의 이벤트
            public void onTabReselected(TabLayout.Tab tab){

            }
        });



        TextView Title = findViewById(R.id.Title);
        TextView info_Name = findViewById(R.id.info_Name);
        TextView info_Call = findViewById(R.id.info_Call);
        TextView info_Address = findViewById(R.id.info_Address);
        TextView info_Subject = findViewById(R.id.info_Subject);
        TextView info_Distance = findViewById(R.id.info_Distance);
        TextView info_Weekday = findViewById(R.id.info_Weekday);
        TextView info_Saturday = findViewById(R.id.info_Saturday);
        TextView info_Sunday = findViewById(R.id.info_Sunday);

        Title.setText(hospital.getHospitalName());
        info_Name.setText("병원명 : "+hospital.getHospitalName());
        info_Call.setText("전화번호 : "+hospital.getCallNumber());
        info_Distance.setText("거리 : "+hospital.getDistance());
        info_Address.setText("주소 : "+hospital.getAddress());
        String allSubjects = "";
        for(int pos = 0; pos < hospital.getNumSubjects(); pos++) {
            allSubjects = allSubjects+hospital.getSubject(pos)+"  ";
        }
        info_Subject.setText("진료과목 : "+allSubjects);
        if(hospital.openTime_isEmpty(0)==false){
            info_Weekday.setText("주중 진료시간 : "+hospital.getOpenTime(0)+" ~ "+hospital.getClosedTime(0));
        }
        if(hospital.openTime_isEmpty(1)==false){
            info_Saturday.setText("토요일 진료시간 : "+hospital.getOpenTime(1)+" ~ "+hospital.getClosedTime(1));
        }
        if(hospital.openTime_isEmpty(2)==false){
            info_Sunday.setText("일요일 진료시간 : "+hospital.getOpenTime(2)+" ~ "+hospital.getClosedTime(2));
        }

    }
    private void changeView(int index){
        LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.content_info);
        LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.content_map);

        switch(index){
            case 0:
                linearLayout1.setVisibility(View.VISIBLE);
                linearLayout2.setVisibility(View.INVISIBLE);
                break;
            case 1:
                linearLayout1.setVisibility(View.INVISIBLE);
                linearLayout2.setVisibility(View.VISIBLE);
                break;
        }
    }


}