package com.test.internship;

import android.content.Context;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Register_Activity extends AppCompatActivity {

    // 보호자가 입력한 정보를 받아서 변수에 넣는 부분
    EditText idEdit_n;
    String value_n;

    EditText idEdit_p;
    String value_p;

    static int index=0;
    static ArrayList<String> protectorName = new ArrayList<String>();
    static ArrayList<String> protectorPhone = new ArrayList<String>();
    static ArrayList<String> protectorAdd = new ArrayList<>();

//    CustomAdapter customAdapter;
//    ListView listView;
//    ArrayList<String>data;
//    Context context;

    //추가
    private ArrayList<ProtectorData> persondata=null;
    private CustomAdapter customAdapter = null;
    private ListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        //추가
        persondata = new ArrayList<ProtectorData>();
        persondata.add(new ProtectorData(R.drawable.ic_launcher_foreground, "사용자 입력 이름","사용자 입력 번호"));


        customAdapter = new CustomAdapter(this,R.layout.protectorinfo_style,persondata);
        listView = findViewById(R.id.list);
        listView.setAdapter(customAdapter);










        Button addInfo;
        addInfo = findViewById(R.id.confirm);
        addInfo.setOnClickListener(listener);

        idEdit_n = (EditText)findViewById(R.id.pName);
        idEdit_p = (EditText)findViewById(R.id.pPhone);

        data = new ArrayList<String>();
        listView = findViewById(R.id.list);
//        customAdapter = new CustomAdapter(this, data);
    }

    /// ArrayList에 보호자정보(이름, 번호) 넣는 부분
    public void getProtectorInfo() {
        protectorName.add(index,value_n);
        protectorPhone.add(index,value_p);
        protectorAdd.add(index,value_n+"  , "+value_p);
        index++;
        return;
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.confirm:
                    value_n = idEdit_n.getText().toString();
                    value_p = idEdit_p.getText().toString();

                    if(value_n.length()!=0 && value_p.length()!=0) {
                        if(index==0){
                            getProtectorInfo();
                            Toast.makeText(Register_Activity.this, "보호자 정보 등록되었습니다.", Toast.LENGTH_SHORT).show();
                            data.add(protectorAdd.get(index - 1));
                            listView.setAdapter(customAdapter);
                        }
                        else{
                            Toast.makeText(Register_Activity.this, "보호자정보 삭제 후 등록해주세요!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(Register_Activity.this, "보호자 정보가 부족합니다", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
}