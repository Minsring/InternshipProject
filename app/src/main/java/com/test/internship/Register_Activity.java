package com.test.internship;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class Register_Activity<listener> extends AppCompatActivity{
    // 보호자가 입력한 정보를 받아서 변수에 넣는 부분
    EditText idEdit_n;
    String value_n;

    EditText idEdit_p;
    String value_p;

    int index=0;
    ArrayList<String> protectorName = new ArrayList<String>();
    ArrayList<String> protectorPhone = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Button addInfo;
        addInfo = findViewById(R.id.confirm);
        idEdit_n = (EditText)findViewById(R.id.pName);
        idEdit_p = (EditText)findViewById(R.id.pPhone);

        addInfo.setOnClickListener(listener);
    }

    /// ArrayList에 보호자정보(이름, 번호) 넣는 부분
    public void getProtectorInfo() {
        protectorName.add(index,value_n);
        protectorPhone.add(index,value_p);
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
                    getProtectorInfo();
                    System.out.println(protectorPhone.get(index-1));
                    System.out.println(protectorName.get(index-1));
                    break;
            }
        }
    };
}
