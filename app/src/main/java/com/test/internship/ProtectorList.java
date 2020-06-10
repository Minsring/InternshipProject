package com.test.internship;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

//import static com.test.internship.Register_Activity.protectorAdd;
import static com.test.internship.Register_Activity.index;
import static com.test.internship.Register_Activity.protectorName;
import static com.test.internship.Register_Activity.protectorPhone;

public class ProtectorList extends AppCompatActivity {

    ListView listView;
    ArrayList<String>data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protector_list);
        data = new ArrayList<String>();

        for(int i=0;i<index;i++) {
            data.add(protectorName.get(i));
        }
        listView = findViewById(R.id.list);
        CustomAdapter customAdapter = new CustomAdapter(this, data);
        listView.setAdapter(customAdapter);

    }
}
