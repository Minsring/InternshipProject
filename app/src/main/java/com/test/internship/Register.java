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
    static String person1_n, person1_p, person2_n, person2_p, person3_n, person3_p,
            person4_n, person4_p, person5_n, person5_p;

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
        registerAdapter = new RegisterAdapter(this, R.layout.protector_info_style, personData);

        listView = findViewById(R.id.list);
        listView.setAdapter(registerAdapter);

        addInfo = findViewById(R.id.confirm);
        addInfo.setOnClickListener(listener);

        idEdit_n = (EditText)findViewById(R.id.pName);
        idEdit_p = (EditText)findViewById(R.id.pPhone);

        if (saveData) {
            if (person1_n != null && person1_p != null) {
                personData.add(new ProtectorData(R.drawable.person, person1_n, person1_p));
                listView.setAdapter(registerAdapter);
            }
            if (person2_n != null && person2_p != null) {
                personData.add(new ProtectorData(R.drawable.person, person2_n, person2_p));
                listView.setAdapter(registerAdapter);
            }
            if (person3_n != null && person3_p != null) {
                personData.add(new ProtectorData(R.drawable.person, person3_n, person3_p));
                listView.setAdapter(registerAdapter);
            }
            if (person4_n != null && person4_p != null) {
                personData.add(new ProtectorData(R.drawable.person, person4_n, person4_p));
                listView.setAdapter(registerAdapter);
            }
            if (person5_n != null && person5_p != null) {
                personData.add(new ProtectorData(R.drawable.person, person5_n, person5_p));
                listView.setAdapter(registerAdapter);
            }
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.confirm:

                    value_n = idEdit_n.getText().toString();
                    value_p = idEdit_p.getText().toString();

                    if (value_n.length() != 0 && value_p.length() != 0) {
                        if (person1_n == null && person1_p == null) {
                            person1_p = value_p;
                            person1_n = value_n;
                            personData.add(new ProtectorData(R.drawable.person, value_n, value_p));
                            Toast.makeText(Register.this, "보호자 정보가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                            listView.setAdapter(registerAdapter);
                            save(0);
                        } else if (person2_n == null && person2_p == null) {
                            person2_p = value_p;
                            person2_n = value_n;
                            personData.add(new ProtectorData(R.drawable.person, value_n, value_p));
                            Toast.makeText(Register.this, "보호자 정보가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                            listView.setAdapter(registerAdapter);
                            save(1);
                        } else if (person3_n == null && person3_p == null) {
                            person3_p = value_p;
                            person3_n = value_n;
                            personData.add(new ProtectorData(R.drawable.person, value_n, value_p));
                            Toast.makeText(Register.this, "보호자 정보가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                            listView.setAdapter(registerAdapter);
                            save(2);
                        } else if (person4_n == null && person4_p == null) {
                            person4_p = value_p;
                            person4_n = value_n;
                            personData.add(new ProtectorData(R.drawable.person, value_n, value_p));
                            Toast.makeText(Register.this, "보호자 정보가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                            listView.setAdapter(registerAdapter);
                            save(3);
                        } else if (person5_n == null && person5_p == null) {
                            person5_p = value_p;
                            person5_n = value_n;
                            personData.add(new ProtectorData(R.drawable.person, value_n, value_p));
                            Toast.makeText(Register.this, "보호자 정보가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                            listView.setAdapter(registerAdapter);
                            save(4);
                        }
                        else{
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
        for(int i=0;i<5;i++){
            save(i);
        }
        for(int i=0;i<5;i++){
            save(i);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        for(int i=0;i<5;i++){
            save(i);
        }
        for(int i=0;i<5;i++){
            save(i);
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        for(int i=0;i<5;i++){
            save(i);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        for(int i=0;i<5;i++){
            save(i);
        }
    }
    @Override
    public void onRestart() {
        super.onRestart();
        for(int i=0;i<5;i++){
            save(i);
        }
    }

    private void save(int numPerson) {
        SharedPreferences.Editor editor = appData.edit();
        editor.putBoolean("SAVE_ON", true);
        if (numPerson == 0) {
            editor.putString("PERSON1_NAME", person1_n);
            editor.putString("PERSON1_PHONE", person1_p);
        } else if (numPerson == 1) {
            editor.putString("PERSON2_NAME", person2_n);
            editor.putString("PERSON2_PHONE", person2_p);
        } else if (numPerson == 2) {
            editor.putString("PERSON3_NAME", person3_n);
            editor.putString("PERSON3_PHONE", person3_p);
        } else if (numPerson == 3) {
            editor.putString("PERSON4_NAME", person4_n);
            editor.putString("PERSON4_PHONE", person4_p);
        } else if (numPerson == 4) {
            editor.putString("PERSON5_NAME", person5_n);
            editor.putString("PERSON5_PHONE", person5_p);
        }
        editor.apply();

    }

    private void load() {
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