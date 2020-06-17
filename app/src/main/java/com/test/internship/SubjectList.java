package com.test.internship;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naver.maps.geometry.LatLng;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SubjectList extends AppCompatActivity {

    String subject;
    Button mapButton;
    TextView subjectTitle;
    RecyclerView recyclerView;
    ArrayList<HospitalData> openHospital=null;
    ArrayList<HospitalData> closedHospital=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_list);

        mapButton = findViewById(R.id.mapBtn);
        mapButton.setOnClickListener(listener);

        // 과목 제목 달기
        Intent getIntent = getIntent();
        subject = getIntent.getStringExtra("진료과");
        subjectTitle = findViewById(R.id.subjectName);
        subjectTitle.setText(subject);

        // 리사이클러뷰, 레이아웃 매니저
        recyclerView = findViewById(R.id.listRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


        final SubjectListAdapter subjectListAdapter = new SubjectListAdapter();
        xmlParser(subjectListAdapter);
        subjectListAdapter.combineItems();     // 진료중-준비중 순서대로 들어가게 하는 메소드 호출
        openHospital= subjectListAdapter.getOpenItem(); //얘가 어댑터에서 받아와야 Map에 전달해줄수있다 !
        closedHospital= subjectListAdapter.getClosedItem();

        recyclerView.setAdapter(subjectListAdapter);

        // 액티비티에서 커스텀 리스너 객체 생성 및 전달
        subjectListAdapter.setOnItemClickListener(new SubjectListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                // 클릭시 이벤트를 SubjectList에서 처리
                HospitalData hospital = subjectListAdapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), HospitalInformation.class);
                intent.putExtra("병원", (Serializable)hospital);
                startActivity(intent);
            }
        });

        if(subjectTitle.getText().equals("응급실")){
            subjectListAdapter.setEROpenClosedClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    //번호받아와서
                    int position=(int)view.getTag(); //붙였던 position꼬리표 때오기
                    HospitalData hospital= subjectListAdapter.getItem(position);
                    String number=hospital.getCallNumber();
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+number)); //다이얼로 연결
                    startActivity(intent);
                }
            });
        }
        else{
            subjectListAdapter.setOpenClosedClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                 //  Toast.makeText(getApplicationContext(),"버튼눌림",Toast.LENGTH_LONG).show();
                    int position=(int)view.getTag();
                    HospitalData hospital = subjectListAdapter.getItem(position);
                    Intent intent = new Intent(getApplicationContext(), HospitalInformation.class);
                    intent.putExtra("병원", (Serializable)hospital);
                    intent.putExtra("진료과", subject);
                    startActivity(intent);
                }
            });
        }
    }

    private void xmlParser(SubjectListAdapter subjectListAdapter) {
        Calendar calendar=Calendar.getInstance();
        int dayOfWeek; //요일을 숫자로 받기
        int starth,startm,endh,endm; //병원 여는 시,분, 닫는 시, 분 시분...
        int nowh,nowm; //현재 시,분

        InputStream inputStream = getResources().openRawResource(R.raw.hospitaldata);

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(new InputStreamReader(inputStream, "UTF-8"));
            int eventType = parser.getEventType();
            HospitalData hospital = null;
            double lat = 0;
            double lng = 0;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String startTag = parser.getName();
                        if (startTag.equals("hospital")) {
                            hospital = new HospitalData();
                        }
                        if (startTag.equals("name")) {
                            hospital.setHospitalName(parser.nextText());
                        }
                        if(startTag.equals("openweekday")){
                            hospital.addOpenTime(parser.nextText());
                        }
                        if(startTag.equals("opensaturday")){
                            hospital.addOpenTime(parser.nextText());
                        }
                        if(startTag.equals("opensunday")){
                            hospital.addOpenTime(parser.nextText());
                        }
                        if(startTag.equals("closeweekday")){
                            hospital.addClosedTime(parser.nextText());
                        }
                        if(startTag.equals("closesaturday")){
                            hospital.addClosedTime(parser.nextText());
                        }
                        if(startTag.equals("closesunday")){
                            hospital.addClosedTime(parser.nextText());
                        }
                        if (startTag.equals("address")) {
                            hospital.setAddress(parser.nextText());
                        }
                        if (startTag.equals("callnumber")) {
                            hospital.setCallNumber(parser.nextText());
                        }
                        if (startTag.equals("subject")) {
                            hospital.addSubject(parser.nextText());
                            hospital.setNumSubjects(hospital.getNumSubjects()+1);
                        }
                        if (startTag.equals("distance")) {
                            hospital.setDistance(parser.nextText());
                        }
                        if (startTag.equals("lat")) {
                            lat= Double.parseDouble(parser.nextText());
                        }
                        if (startTag.equals("lng")) {
                            lng= Double.parseDouble(parser.nextText());
                            hospital.setLatLng(new LatLng(lat, lng));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if(((hospital.findSubject(subject))||subject.equals("모든 병원"))==true) {
                            if (endTag.equals("hospital")) {

                                dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                                String pattern = "HH:mm";
                                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                                Date now = new Date();
                                String nowString = sdf.format(now); //현재 시시:분분 형태 String
                                String[] array = nowString.split(":");
                                nowh = Integer.parseInt(array[0]);
                                nowm = Integer.parseInt(array[1]); //현재시간 받아옴

                                hospital.setOpenClosed("준비중");

                                // 평일
                                if(dayOfWeek==2||dayOfWeek==3||dayOfWeek==4||dayOfWeek==5||dayOfWeek==6){
                                    String startTime = hospital.getOpenTime(0);
                                    String endTime = hospital.getClosedTime(0);

                                    //병원 진료시간
                                    String[] array1 = startTime.split(":");
                                    starth = Integer.parseInt(array1[0]);
                                    startm = Integer.parseInt(array1[1]);
                                    String[] array2 = endTime.split(":");
                                    endh = Integer.parseInt(array2[0]);
                                    endm = Integer.parseInt(array2[1]);

                                    if(((nowh<endh)||(nowh==endh&&nowm<endm)) && ((starth<nowh)||(starth==nowh&&startm<nowm))){
                                        hospital.setOpenClosed("진료중");
                                    }
                                }
                                // 토요일
                                else if(dayOfWeek==7)
                                {
                                    if(hospital.getOpenTimeSize()>=2){
                                        String startTime = hospital.getOpenTime(1);
                                        String endTime = hospital.getClosedTime(1);

                                        //병원 진료시간
                                        array = startTime.split(":");
                                        starth = Integer.parseInt(array[0]);
                                        startm = Integer.parseInt(array[1]);
                                        array = endTime.split(":");
                                        endh = Integer.parseInt(array[0]);
                                        endm = Integer.parseInt(array[1]);

                                        if(((nowh<endh)||(nowh==endh&&nowm<endm)) && ((starth<nowh)||(starth==nowh&&startm<nowm))){
                                            hospital.setOpenClosed("진료중");
                                        }
                                    }
                                }
                                // 일요일
                                else if(dayOfWeek==1){
                                    if(hospital.getOpenTimeSize()>=3){
                                        String startTime = hospital.getOpenTime(2);
                                        String endTime = hospital.getClosedTime(2);

                                        //병원 진료시간
                                        array = startTime.split(":");
                                        starth = Integer.parseInt(array[0]);
                                        startm = Integer.parseInt(array[1]);
                                        array = endTime.split(":");
                                        endh = Integer.parseInt(array[0]);
                                        endm = Integer.parseInt(array[1]);

                                        if(((nowh<endh)||(nowh==endh&&nowm<endm)) && ((starth<nowh)||(starth==nowh&&startm<nowm))){
                                            hospital.setOpenClosed("진료중");
                                        }
                                    }
                                }

                                if(hospital.getOpenClosed()=="진료중"){
                                    subjectListAdapter.addOpenItem(hospital);
                                }
                                else if(hospital.getOpenClosed()=="준비중"){
                                    subjectListAdapter.addClosedItem(hospital);
                                }
                            }
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.mapBtn:
                    intent = new Intent(getApplicationContext(), SubjectListMap.class);
                    intent.putExtra("열린병원리스트",openHospital);
                    intent.putExtra("닫은병원리스트",closedHospital);
                    intent.putExtra("진료과",subject);
                    break;
            }
             if(intent != null) startActivity(intent);    // 다른 처리 없다면 여기서 한번에 화면 전환
        }
    };
}