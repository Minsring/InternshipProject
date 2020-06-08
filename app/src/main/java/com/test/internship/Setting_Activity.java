package com.test.internship;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import static com.test.internship.User.timer;
import static com.test.internship.User.tt;


public class Setting_Activity extends AppCompatActivity {
    private boolean saveData;
    private boolean isCheck1;
    private boolean isCheck2;
    Button btnregister;
    Switch switch1;
    Switch switch2;
    IntentFilter ifilter;
    Handler handler;
    private SharedPreferences appData;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        btnregister = findViewById(R.id.btnregister);
        btnregister.setOnClickListener(listener);

        switch1 = (Switch)findViewById(R.id.switch1);
        switch2 = (Switch)findViewById(R.id.switch2);
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();
        if(saveData){
            switch1.setChecked(isCheck1);
            switch2.setChecked(isCheck2);
        }


        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
                if (isChecked){
                    save();
                    timer.schedule(tt, 0, 5000);
                    System.out.println("1");

                }else{
                    System.out.println("2");
                    save();
                }
            }
        });


        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
                if (isChecked){
                    save();
                }else{
                    save();
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
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onStart() {
        super.onStart();
    }
    private void save(){
        SharedPreferences.Editor editor = appData.edit();
        editor.putBoolean("SAVE_LOGIN_DATA", (switch1.isChecked()||switch2.isChecked()));
        editor.putBoolean("CHECK1", switch1.isChecked());
        editor.putBoolean("CHECK2", switch2.isChecked());

        editor.apply();

    }
    private void load(){
        saveData = appData.getBoolean("SAVE_LOGIN_DATA", false);
        isCheck1=appData.getBoolean("CHECK1", false);
        isCheck2=appData.getBoolean("CHECK2", false);
    }
}


