package com.test.internship;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    ArrayList<HospitalInformation> items =new ArrayList<HospitalInformation>();
    Context context;

    // item으로 만든 itemView를 담아두는 뷰 홀더
    // 일단 필요한 객체의 수만 ViewHolder에 담아서 화면에 뿌려준다
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemHospitalName, itemSubject;
        TextView itemDistance, itemOpenClosed;      // distance, openclosed 부분
        public ViewHolder(View itemView){
            super(itemView);
            itemHospitalName = itemView.findViewById(R.id.itemHospitalName);
            itemSubject = itemView.findViewById(R.id.itemDistance);
            // distance, openclosed 부분
            itemDistance = itemView.findViewById(R.id.itemDistance);
            itemOpenClosed = itemView.findViewById(R.id.itemOpenClosed);
        }
        public void setItem(HospitalInformation hospital){
            itemHospitalName.setText(hospital.getHospitalName());
            itemSubject.setText(hospital.getSubject());
            // distance, openclosed 부분
            itemDistance.setText(hospital.getDistance());
            itemOpenClosed.setText(hospital.getOpenClosed());
        }
    }

    // 임시로 사용
    public void addItem(HospitalInformation item){
        items.add(item);
    }
    public HospitalInformation getItem(int position){
        return items.get(position);
    }
    // 해당위치의 item을 변경하는 함수
    public void setItem(int position, HospitalInformation item){
        items.set(position, item);
    }
    // 리스트를 set
    public void setItems(ArrayList<HospitalInformation> items){
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override       // ViewHolder객체가 생성될 때 자동으로 호출
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.item_style, viewGroup, false);

        // 새로 생성된 ViewHolder 객체를 뷰 객체를 담아서 리턴
        return new ViewHolder(itemView);
    }

    @Override       // ViewHolder객체가 재사용될 때 자동으로 호출
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        HospitalInformation item = items.get(position);
        viewHolder.setItem(item);
    }
}
