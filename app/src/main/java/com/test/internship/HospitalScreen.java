package com.test.internship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class HospitalScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital_screen);

        Intent getIntent = getIntent();
        HospitalInformation hospital = (HospitalInformation) getIntent.getSerializableExtra("병원");

        Toast.makeText(getApplicationContext(), "병원이름: "+hospital.getHospitalName(), Toast.LENGTH_LONG).show();
    }
}