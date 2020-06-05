package com.test.internship;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SubjectList extends AppCompatActivity {
    Button mapBtn;
    TextView subjectTitle;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_list);

        mapBtn = findViewById(R.id.mapBtn);
        mapBtn.setOnClickListener(listener);

        // 과목 제목 달기
        subjectTitle = findViewById(R.id.subjectName);
        subjectTitle.setText(User.subject.toString());

        // 리사이클러뷰, 레이아웃 매니저
        RecyclerView recyclerView = new RecyclerView(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


        final Adapter adapter = new Adapter();
        xmlParser(adapter);
        adapter.combineItems();     // 진료중-준비중 순서대로 들어가게 하는 메소드 호출

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
        adapter.setOnItemClickListener(new Adapter.onItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                // 클릭시 이벤트를 SubjectList에서 처리
                HospitalInformation hospital = adapter.getItem(position);
//                Toast.makeText(getApplicationContext(), "클릭한 병원 이름: "+hospital.getHospitalName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), HospitalScreen.class);
                intent.putExtra("병원", hospital);
                startActivity(intent);
            }
        });
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
                        }
                        if (startTag.equals("distance")) {
                            hospital.setDistance(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if(((hospital.findSubject(User.subject.toString()))||User.subject.toString().equals("모든 병원"))==true) {
                            if (endTag.equals("hospital")) {

                                dayOfWeek=calendar.get(Calendar.DAY_OF_WEEK);
                                String pattern="HH:mm";
                                SimpleDateFormat sdf=new SimpleDateFormat(pattern);
                                Date now=new Date();
                                String nowString=sdf.format(now); //현재 시시:분분 형태 String
                                String[] array=nowString.split(":");
                                nowh=Integer.parseInt(array[0]);
                                nowm=Integer.parseInt(array[1]); //현재시간 받아옴

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

                                    if((nowh<endh)||(nowh==endh&&nowm<endm)){
                                        if((starth<nowh)||(starth==nowh&&startm<nowm)){
                                            hospital.setOpenClosed("진료중");
                                        }
                                        else{
                                            hospital.setOpenClosed("준비중");
                                        }
                                    }
                                    else{
                                        hospital.setOpenClosed("준비중");
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

                                        if((nowh<endh)||(nowh==endh&&nowm<endm)){
                                            if((starth<nowh)||(starth==nowh&&startm<nowm)){
                                                hospital.setOpenClosed("진료중");
                                            }
                                            else{
                                                hospital.setOpenClosed("준비중");
                                            }
                                        }
                                        else{
                                            hospital.setOpenClosed("준비중");
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

                                        if((nowh<endh)||(nowh==endh&&nowm<endm)){
                                            if((starth<nowh)||(starth==nowh&&startm<nowm)){
                                                hospital.setOpenClosed("진료중");
                                            }
                                            else{
                                                hospital.setOpenClosed("준비중");
                                            }
                                        }
                                        else{
                                            hospital.setOpenClosed("준비중");
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
                    intent = new Intent(getApplicationContext(), SubjectListMap.class);
//                    startActivity(intent);
                    break;
            }
            if(intent != null) startActivity(intent);    // 다른 처리 없다면 여기서 한번에 화면 전환
        }
    };
}