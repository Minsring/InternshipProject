package com.test.internship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Setting_Activity extends AppCompatActivity {
    Button btnregister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        btnregister = findViewById(R.id.btnregister);
        btnregister.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.btnregister:
                    intent = new Intent(getApplicationContext(), Register_Activity.class);
                    // startActivity(intent);
                    break;
            }
            if(intent!=null) startActivity(intent);
        }
    };

}


