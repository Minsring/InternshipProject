package com.test.internship;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import java.util.Timer;
import java.util.TimerTask;


public class Setting_Activity extends AppCompatActivity {
    Button btnregister;
    Switch switch1, switch2;
    IntentFilter ifilter;
    Handler handler;
    Timer timer;
    TimerTask task;
    boolean flag =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        btnregister = findViewById(R.id.btnregister);
        btnregister.setOnClickListener(listener);

        switch1 = (Switch)findViewById(R.id.switch1);
        switch2 = (Switch)findViewById(R.id.switch2);

        handler = new Handler();
        timer = new Timer();

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
                if (isChecked) {
                    flag=true;
                    /*
                    System.out.println("버튼 on확인");
                    timer.schedule(task, 0, 5000);
                    System.out.println("스타트됨");
                    TimerTask addTask = new TimerTask() {
                        @Override
                        public void run() {
                            //주기적으로 실행할 작업 추가
                            ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                            Intent batteryStatus = registerReceiver(null, ifilter);
                            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                            int batteryPct = (int) ((level / (float) scale) * 100);
                            System.out.println("배터리용량: " + batteryPct);
                        }
                    };
                    */
                                     }
                else {
                    flag=false;
                   // System.out.println("버튼 off 확인");
                    // Stop_Period();
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

    /*
    public void Start_Period(){
        //timer.schedule(adTast , 5000);  // 5초후 실행하고 종료
        //timer.schedule(adTast, 0, 300000); // 0초후 첫실행, 3초마다 계속실행
        timer.schedule(task, 0, 5000); //// 0초후 첫실행, Interval분마다 계속실행
    }

    public void Stop_Period(){
        //Timer 작업 종료
        if(timer != null) timer.cancel();
    }*/
}


