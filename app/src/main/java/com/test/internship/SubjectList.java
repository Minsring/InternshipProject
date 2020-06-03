package com.test.internship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class SubjectList extends AppCompatActivity {
    Button mapBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_list);

        mapBtn = findViewById(R.id.mapBtn);
        mapBtn.setOnClickListener(listener);

        // 리사이클러뷰, 레이아웃 매니저
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        // Adapter 세팅
        Adapter adapter = new Adapter();
        adapter.addItem(new HospitalInformation("민슬 병원", "9:00", "17:00", "민슬집"
                                                        , "010-1234-5678", "진료과", "일요일"));
        adapter.addItem(new HospitalInformation("희정 병원", "11:00", "20:00", "희정집"
                , "010-1122-5566", "희정과", "토요일"));
        adapter.addItem(new HospitalInformation("민옥 병원", "0:00", "24:00", "민옥집"
                , "010-1234-1234", "민옥과", "매일"));

        recyclerView.setAdapter(adapter);
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
