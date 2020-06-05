package com.test.internship;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HospitalScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital_screen);
        Button callHospital=findViewById(R.id.callHospitalBtn);
        Intent getIntent = getIntent();
        final HospitalInformation hospital = (HospitalInformation) getIntent.getSerializableExtra("병원");

        Toast.makeText(getApplicationContext(), "병원이름: "+hospital.getHospitalName(), Toast.LENGTH_LONG).show();

        callHospital.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String number=hospital.getCallNumber(); //이거받으려면 hospital이 final되야한대
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+number)); //다이얼로 연결
                startActivity(intent);
            }
        });

    }


}