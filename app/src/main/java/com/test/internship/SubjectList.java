package com.test.internship;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


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
    Button mapBtn;
    TextView subjectTitle;
    LinearLayout linearLayout;
    Button openClosed;
    ArrayList<HospitalInformation> openHospital=null;
    ArrayList<HospitalInformation> closedHospital=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_list);

        mapBtn = findViewById(R.id.mapBtn);
        mapBtn.setOnClickListener(listener);
        openClosed=findViewById(R.id.itemOpenClosed);

        // 과목 제목 달기
        Intent getIntent = getIntent();
        subject = getIntent.getStringExtra("진료과");
        subjectTitle = findViewById(R.id.subjectName);
        subjectTitle.setText(subject);

        // 리사이클러뷰, 레이아웃 매니저
        RecyclerView recyclerView = new RecyclerView(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


        final Adapter adapter = new Adapter();
        xmlParser(adapter);
        adapter.combineItems();     // 진료중-준비중 순서대로 들어가게 하는 메소드 호출
        openHospital=adapter.getOpenItem(); //얘가 어댑터에서 받아와야 Map에 전달해줄수있다 !
        closedHospital=adapter.getClosedItem(); //얘가 어댑터에서 받아와야 Map에 전달해줄수있다 !

        // 해당 과목의 병원이 있으면 리스트 동적제공, 없으면 "해당 병원 없습니다." 텍스트 동적 제공
        linearLayout = findViewById(R.id.linearLayout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        if(adapter.getItemCount()!=0){
            recyclerView.setLayoutParams(params);
            recyclerView.setBackgroundColor(Color.parseColor("#22ff0000"));
            linearLayout.addView(recyclerView);
            recyclerView.setAdapter(adapter);
        } else{
            TextView textView = new TextView(this);
            textView.setBackgroundColor(Color.parseColor("#22ff0000"));
            textView.setText("선택하신 과목의 병원이 없습니다.");
            textView.setTextSize(25);
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(params);
            linearLayout.addView(textView);
        }

        // 액티비티에서 커스텀 리스너 객체 생성 및 전달
        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                // 클릭시 이벤트를 SubjectList에서 처리
                HospitalInformation hospital = adapter.getItem(position);
//                Toast.makeText(getApplicationContext(), "클릭한 병원 이름: "+hospital.getHospitalName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), HospitalScreen.class);
                intent.putExtra("병원", (Serializable)hospital);
                startActivity(intent);
            }
        });

        if(subjectTitle.getText().equals("응급실")){
            adapter.setEROpenClosedClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    //번호받아와서
                    int position=(int)view.getTag(); //붙였던 position꼬리표 때오기
                    HospitalInformation hospital=adapter.getItem(position);
                    String number=hospital.getCallNumber();
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+number)); //다이얼로 연결
                    startActivity(intent);
                }
            });
        } //응급실이면 다이얼로 연결되는 리스너 연결하자
        else{
            adapter.setOpenClosedClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                 //  Toast.makeText(getApplicationContext(),"버튼눌림",Toast.LENGTH_LONG).show();
                    int position=(int)view.getTag();
                    HospitalInformation hospital = adapter.getItem(position);
                    Intent intent = new Intent(getApplicationContext(), HospitalScreen.class);
                    intent.putExtra("병원", (Serializable)hospital);
                    intent.putExtra("진료과", subject);
                    startActivity(intent);

                }
            });
        } //응급실 아니면 그냥 itemlistener와 같은 역할로 다음 화면으로 넘어가야한다.

    }


    private void xmlParser(Adapter adapter) {
        Calendar calendar=Calendar.getInstance();
        int dayOfWeek; //요일을 숫자로 받을거다
        int starth,startm,endh,endm; //병원 여는 시,분, 닫는 시, 분 시분...
        int nowh,nowm; //현재 시,분

        ArrayList<HospitalInformation> items = new ArrayList<HospitalInformation>();
        InputStream is = getResources().openRawResource(R.raw.hospitaldata);

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(new InputStreamReader(is, "UTF-8"));
            int eventType = parser.getEventType();
            HospitalInformation hospital = null;
            double lat= 0.0f, lng = 0.0f;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String startTag = parser.getName();
                        if (startTag.equals("hospital")) {
                            hospital = new HospitalInformation();
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

                                dayOfWeek=calendar.get(Calendar.DAY_OF_WEEK);
                                String pattern="HH:mm";
                                SimpleDateFormat sdf=new SimpleDateFormat(pattern);
                                Date now=new Date();
                                String nowString=sdf.format(now); //현재 시시:분분 형태 String
                                String[] array=nowString.split(":");
                                nowh=Integer.parseInt(array[0]);
                                nowm=Integer.parseInt(array[1]); //현재시간 받아옴

                                hospital.setOpenClosed("준비중");

                                if(dayOfWeek==2||dayOfWeek==3||dayOfWeek==4||dayOfWeek==5||dayOfWeek==6){
                                    String startTime=hospital.getOpenTime(0);
                                    String endTime=hospital.getClosedTime(0);
                                    //병원 진료시간
                                    String[] array1=startTime.split(":");
                                    starth=Integer.parseInt(array1[0]);
                                    startm=Integer.parseInt(array1[1]);
                                    String[] array2=endTime.split(":");
                                    endh=Integer.parseInt(array2[0]);
                                    endm=Integer.parseInt(array2[1]);

                                    if(((nowh<endh)||(nowh==endh&&nowm<endm)) && ((starth<nowh)||(starth==nowh&&startm<nowm))){
                                        hospital.setOpenClosed("진료중");
                                    }
                                } //평일이다
                                else if(dayOfWeek==7)
                                {
                                    if(hospital.getOpenTimesize()>=2){
                                        String startTime=hospital.getOpenTime(1);
                                        String endTime=hospital.getClosedTime(1);
                                        //병원 진료시간
                                        array=startTime.split(":");
                                        starth=Integer.parseInt(array[0]);
                                        startm=Integer.parseInt(array[1]);
                                        array=endTime.split(":");
                                        endh=Integer.parseInt(array[0]);
                                        endm=Integer.parseInt(array[1]);

                                        if(((nowh<endh)||(nowh==endh&&nowm<endm)) && ((starth<nowh)||(starth==nowh&&startm<nowm))){
                                            hospital.setOpenClosed("진료중");
                                        }
                                    }
                                } //토요일이다
                                else if(dayOfWeek==1){
                                    if(hospital.getOpenTimesize()>=3){
                                        String startTime=hospital.getOpenTime(2);
                                        String endTime=hospital.getClosedTime(2);
                                        //병원 진료시간
                                        array=startTime.split(":");
                                        starth=Integer.parseInt(array[0]);
                                        startm=Integer.parseInt(array[1]);
                                        array=endTime.split(":");
                                        endh=Integer.parseInt(array[0]);
                                        endm=Integer.parseInt(array[1]);

                                        if(((nowh<endh)||(nowh==endh&&nowm<endm)) && ((starth<nowh)||(starth==nowh&&startm<nowm))){
                                            hospital.setOpenClosed("진료중");
                                        }
                                    }
                                } //일요일이다

                                if(hospital.getOpenClosed()=="진료중"){
                                    adapter.addOpenItem(hospital);
                                }
                                else if(hospital.getOpenClosed()=="준비중"){
                                    adapter.addClosedItem(hospital);
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
//                    intent = new Intent(getApplicationContext(), SubjectListMap.class);
                    intent = new Intent(getApplicationContext(), CustomMarkerClusterClass.class);
                    intent.putExtra("열린병원리스트",openHospital);
                    intent.putExtra("닫은병원리스트",closedHospital);
                    intent.putExtra("진료과",subject);
//                    startActivity(intent);
                    break;
            }
//            if(intent.getSerializableExtra("열린병원리스트")==null){
//                Toast.makeText(getApplicationContext(),"현재 열린 병원이 없습니다.",Toast.LENGTH_LONG).show();
//            }
//            else{
//                startActivity(intent);
//            }
             if(intent != null) startActivity(intent);    // 다른 처리 없다면 여기서 한번에 화면 전환

        }
    };
}