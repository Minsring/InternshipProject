package com.test.internship;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;

///커스텀 리스너 정의 -> 추가
public class CustomAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<String>data;

    public CustomAdapter(Context context, ArrayList<String>data){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.protectorinfo_style,null);
        TextView t1 = view.findViewById(R.id.personName);

        t1.setText(data.get(position));

        View bodyView = view.findViewById(R.id.body);
        Button btn = view.findViewById(R.id.btndelete);

        bodyView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(context,"바디클릭 테스트",Toast.LENGTH_SHORT).show();
            }
        });

        btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                // 삭제처리

                Toast.makeText(context,"삭제",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
