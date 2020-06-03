package com.test.internship;

import android.content.Intent;
import android.content.res.AssetManager;
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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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


        Adapter adapter = new Adapter();
        xmlParser(adapter);
        // Adapter 세팅
        //ArrayList<HospitalInformation> list=xmlParser();

//        adapter.addItem(new HospitalInformation("민슬 병원", "9:00", "17:00", "민슬집"
//                                                        , "010-1234-5678", "진료과", "일요일"));
//        adapter.addItem(new HospitalInformation("희정 병원", "11:00", "20:00", "희정집"
//                , "010-1122-5566", "희정과", "토요일"));
//        adapter.addItem(new HospitalInformation("민옥 병원", "0:00", "24:00", "민옥집"
//                , "010-1234-1234", "민옥과", "매일"));

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

    }


    private void xmlParser(Adapter adapter) {
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
                        if (startTag.equals("opentime")) {
                            hospital.setOpenTime(parser.nextText());
                        }
                        if (startTag.equals("closedtime")) {
                            hospital.setClosedTime(parser.nextText());
                        }
                        if (startTag.equals("address")) {
                            hospital.setAddress(parser.nextText());
                        }
                        if (startTag.equals("callnumber")) {
                            hospital.setCallNumber(parser.nextText());
                        }
                        if (startTag.equals("subject")) {
                            hospital.setSubject(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if((hospital.getSubject().equals(User.subject.toString())||User.subject.toString().equals("모든 병원"))==true) {
                            if (endTag.equals("hospital")) {
                                hospital.setDistance("0");
                                hospital.setOpenClosed("영업중");
                                hospital.setOpenDay("매일");
                                adapter.addItem(hospital);
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
