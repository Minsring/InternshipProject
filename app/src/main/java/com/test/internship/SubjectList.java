package com.test.internship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SubjectList extends AppCompatActivity {
    Button mapBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_list);

        mapBtn = findViewById(R.id.mapBtn);
        mapBtn.setOnClickListener(listener);
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
