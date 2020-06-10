package com.test.internship;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Register_Activity<listener> extends AppCompatActivity {

    // 보호자가 입력한 정보를 받아서 변수에 넣는 부분
    EditText idEdit_n;
    String value_n;
    private boolean saveData;
    EditText idEdit_p;
    String value_p;
    private SharedPreferences appData;
    static int index=0;
    static ArrayList<String> protectorName = new ArrayList<String>();
    static ArrayList<String> protectorPhone = new ArrayList<String>();
    static ArrayList<String> protectorAdd = new ArrayList<>();
    String person1_n, person1_p, person2_n, person2_p, person3_n, person3_p, person4_n, person4_p, person5_n, person5_p;

    CustomAdapter customAdapter;
    ListView listView;
    ArrayList<String>data;
    Context context;
    public void getProtectorInfo(String n, String p) {

        protectorName.add(index,n);
        protectorPhone.add(index,p);
        protectorAdd.add(index,n+"  , "+p);
        index++;
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        index=0;

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();
        Button addInfo;
        addInfo = findViewById(R.id.confirm);
        addInfo.setOnClickListener(listener);

        idEdit_n = (EditText)findViewById(R.id.pName);
        idEdit_p = (EditText)findViewById(R.id.pPhone);

        data = new ArrayList<String>();
        listView = findViewById(R.id.list);
        customAdapter = new CustomAdapter(this, data);
        if(saveData) {
            if(person1_n!=null&&person1_p!=null){
                getProtectorInfo(person1_n, person1_p);
                Toast.makeText(Register_Activity.this, "보호자 정보 등록되었습니다.", Toast.LENGTH_SHORT).show();
                data.add(protectorAdd.get(index-1));
                listView.setAdapter(customAdapter);
            }
            if(person2_n!=null&&person2_p!=null){
                getProtectorInfo(person2_n, person2_p);
                Toast.makeText(Register_Activity.this, "보호자 정보 등록되었습니다.", Toast.LENGTH_SHORT).show();
                data.add(protectorAdd.get(index-1));
                listView.setAdapter(customAdapter);
            }
            if(person3_n!=null&&person3_p!=null){
                getProtectorInfo(person3_n, person3_p);
                Toast.makeText(Register_Activity.this, "보호자 정보 등록되었습니다.", Toast.LENGTH_SHORT).show();
                data.add(protectorAdd.get(index-1));
                listView.setAdapter(customAdapter);
            }
            if(person4_n!=null&&person4_p!=null){
                getProtectorInfo(person4_n, person4_p);
                Toast.makeText(Register_Activity.this, "보호자 정보 등록되었습니다.", Toast.LENGTH_SHORT).show();
                data.add(protectorAdd.get(index-1));
                listView.setAdapter(customAdapter);
            }
            if(person5_n!=null&&person5_p!=null){
                getProtectorInfo(person5_n, person5_p);
                Toast.makeText(Register_Activity.this, "보호자 정보 등록되었습니다.", Toast.LENGTH_SHORT).show();
                data.add(protectorAdd.get(index-1));
                listView.setAdapter(customAdapter);
            }

        }
    }

    /// ArrayList에 보호자정보(이름, 번호) 넣는 부분


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.confirm:
                    value_n = idEdit_n.getText().toString();
                    value_p = idEdit_p.getText().toString();
                    if(value_n.length()!=0 && value_p.length()!=0) {
                        if(person1_n==null&&person1_p==null){
                            person1_p = value_p;
                            person1_n = value_n;
                            getProtectorInfo(value_n, value_p);
                            Toast.makeText(Register_Activity.this, "보호자 정보 등록되었습니다.", Toast.LENGTH_SHORT).show();
                            data.add(protectorAdd.get(index-1));
                            listView.setAdapter(customAdapter);
                            save(0);
                        }
                        else if(person2_n==null&&person2_p==null){
                            person2_p = value_p;
                            person2_n = value_n;
                            getProtectorInfo(value_n, value_p);
                            Toast.makeText(Register_Activity.this, "보호자 정보 등록되었습니다.", Toast.LENGTH_SHORT).show();
                            data.add(protectorAdd.get(index-1));
                            listView.setAdapter(customAdapter);
                            save(1);
                        }
                        else if(person3_n==null&&person3_p==null){
                            person3_p = value_p;
                            person3_n = value_n;
                            getProtectorInfo(value_n, value_p);
                            Toast.makeText(Register_Activity.this, "보호자 정보 등록되었습니다.", Toast.LENGTH_SHORT).show();
                            data.add(protectorAdd.get(index-1));
                            listView.setAdapter(customAdapter);
                            save(2);
                        }
                        else if(person4_n==null&&person4_p==null){
                            person4_p = value_p;
                            person4_n = value_n;
                            getProtectorInfo(value_n, value_p);
                            Toast.makeText(Register_Activity.this, "보호자 정보 등록되었습니다.", Toast.LENGTH_SHORT).show();
                            data.add(protectorAdd.get(index-1));
                            listView.setAdapter(customAdapter);
                            save(3);
                        }
                        else if(person5_n==null&&person5_p==null){
                            person5_p = value_p;
                            person5_n = value_n;
                            getProtectorInfo(value_n, value_p);
                            Toast.makeText(Register_Activity.this, "보호자 정보 등록되었습니다.", Toast.LENGTH_SHORT).show();
                            data.add(protectorAdd.get(index-1));
                            listView.setAdapter(customAdapter);
                            save(4);
                        }
                    }
                    else {
                        Toast.makeText(Register_Activity.this, "보호자 정보가 부족합니다", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

        private void save(int a){
            SharedPreferences.Editor editor = appData.edit();
            editor.putBoolean("SAVE_ON", true);
            if(a==0){
                editor.putString("PERSON1_NAME", person1_n);
                editor.putString("PERSON1_PHONE", person1_p);
            }
            else if (a==1){
                editor.putString("PERSON2_NAME", person2_n);
                editor.putString("PERSON2_PHONE", person2_p);
            }
            else if(a==2){
                editor.putString("PERSON3_NAME", person3_n);
                editor.putString("PERSON3_PHONE", person3_p);
            }
            else if (a==3){
                editor.putString("PERSON4_NAME", person4_n);
                editor.putString("PERSON4_PHONE", person4_p);
            }
            else if(a==4){
                editor.putString("PERSON5_NAME", person5_n);
                editor.putString("PERSON5_PHONE", person5_p);
            }
            editor.apply();

    }
    private void load(){
        saveData = appData.getBoolean("SAVE_ON", false);
        person1_n = appData.getString("PERSON1_NAME", null);
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