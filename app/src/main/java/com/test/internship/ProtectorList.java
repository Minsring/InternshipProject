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

public class ProtectorList<listener> extends AppCompatActivity {
   Context context;
   int resource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView listView;
        CustomAdapter adapter;

        adapter = new CustomAdapter(context,resource);

        listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(adapter);


        for(int i=0;i<index;i++) {
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_launcher_foreground), protectorName.get(i),protectorPhone.get(i));
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position) ;

                String titleStr = item.getPersonName() ;
                String descStr = item.getPersonNum() ;
                Drawable iconDrawable = item.getIcon() ;

            }
        }) ;
    }


}
