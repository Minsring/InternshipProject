package com.test.internship;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Register extends AppCompatActivity {

    private SharedPreferences appData;
    private boolean saveData;
    private ListView listView = null;

    static ArrayList<ProtectorData> personData = null;
    static RegisterAdapter registerAdapter = null;
    static String person1_n, person1_p, person2_n, person2_p, person3_n, person3_p, person4_n, person4_p, person5_n, person5_p;
    static int num;

    EditText idEdit_n;
    EditText idEdit_p;
    String value_n;
    String value_p;
    Button addInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();

        personData = new ArrayList<ProtectorData>();

        listView = findViewById(R.id.list);
        listView.setAdapter(registerAdapter);

        addInfo = findViewById(R.id.confirm);
        addInfo.setOnClickListener(listener);

        idEdit_n = (EditText)findViewById(R.id.pName);
        idEdit_p = (EditText)findViewById(R.id.pPhone);

        if (saveData) {
            if (person1_n != null && person1_p != null) {
                personData.add(new ProtectorData(R.drawable.person, person1_n, person1_p));
            }
            if (person2_n != null && person2_p != null) {
                personData.add(new ProtectorData(R.drawable.person, person2_n, person2_p));
            }
            if (person3_n != null && person3_p != null) {
                personData.add(new ProtectorData(R.drawable.person, person3_n, person3_p));
            }
            if (person4_n != null && person4_p != null) {
                personData.add(new ProtectorData(R.drawable.person, person4_n, person4_p));
            }
            if (person5_n != null && person5_p != null) {
                personData.add(new ProtectorData(R.drawable.person, person5_n, person5_p));
            }
        }
        registerAdapter = new RegisterAdapter(this, R.layout.protector_info_style, personData);
        listView.setAdapter(registerAdapter);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.confirm:

                    value_n = idEdit_n.getText().toString();
                    value_p = idEdit_p.getText().toString();

                    if (value_n.length() != 0 && value_p.length() != 0) {
                        if (num <= 4) {
                            personData.add(new ProtectorData(R.drawable.person, value_n, value_p));
                            Toast.makeText(Register.this, "보호자 정보가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                            num++;
                            listView.setAdapter(registerAdapter);
                            save();
                        } else {
                            Toast.makeText(Register.this, "보호자는 최대 5명까지 등록 가능합니다!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Register.this, "보호자 정보를 등록해주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        save();
    }
    @Override
    public void onPause() {
        super.onPause();
        save();
    }
    @Override
    public void onStop() {
        super.onStop();
        save();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        save();
    }
    @Override
    public void onRestart() {
        super.onRestart();
        load();
    }

    private void save() {
        SharedPreferences.Editor editor = appData.edit();
        editor.putBoolean("SAVE_ON", true);
        if(num<0) num=0;
        editor.putInt("NUM",num);
        for(int i = 1; i<=num; i++) {
            editor.putString("PERSON"+i+"_NAME", personData.get(i-1).getPersonName());
            editor.putString("PERSON"+i+"_PHONE",personData.get(i-1).getPersonNum());
        }
        for(int i = num+1; i<=5; i++){
            editor.putString("PERSON"+i+"_NAME", null);
            editor.putString("PERSON"+i+"_PHONE",null);
        }
        editor.apply();
    }

    private void load() {
        num = appData.getInt("NUM", 0);
        if(num<0) num=0;
        saveData = appData.getBoolean("SAVE_ON", false);

        person1_n = appData.getString("PERSON1_NAME",null);
        person1_p = appData.getString("PERSON1_PHONE", null);
        person2_n = appData.getString("PERSON2_NAME", null);
        person2_p = appData.getString("PERSON2_PHONE", null);
        person3_n = appData.getString("PERSON3_NAME", null);
        person3_p = appData.getString("PERSON3_PHONE", null);
        person4_n = appData.getString("PERSON4_NAME", null);
        person4_p = appData.getString("PERSON4_PHONE", null);
        person5_n = appData.getString("PERSON5_NAME", null);
        person5_p = appData.getString("PERSON5_PHONE", null);
    }
}