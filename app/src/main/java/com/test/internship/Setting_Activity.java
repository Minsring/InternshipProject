package com.test.internship;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import java.util.Timer;
import java.util.TimerTask;

import static com.test.internship.User.tt;


public class Setting_Activity extends AppCompatActivity {
    Button btnregister;
    Switch switch1;
    Switch switch2;
    IntentFilter ifilter;
    Handler handler;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        btnregister = findViewById(R.id.btnregister);
        btnregister.setOnClickListener(listener);

        switch1 = (Switch)findViewById(R.id.switch1);
        switch2 = (Switch)findViewById(R.id.switch2);


        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
                if (isChecked){
                    // 타이머 활성화 여기서 시켜야함(잘 돌아감)
                    //Log.d("버튼클릭확인","버튼 클릭됨");
                    User.timer.schedule(tt, 0, 5000);

                }else{
                    //타이머 꺼두기
                }
            }
        });


        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
                if (isChecked){

                }else{

                }
            }
        });
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.btnregister:
                    intent = new Intent(getApplicationContext(), Register_Activity.class);
                    /// startActivity(intent);
                    break;
            }
            if(intent!=null) startActivity(intent);
        }
    };
}


