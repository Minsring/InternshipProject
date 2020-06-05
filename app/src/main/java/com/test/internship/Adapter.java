package com.test.internship;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    ArrayList<HospitalInformation> openItems =new ArrayList<HospitalInformation>();      // 진료중인 병원 리스트
    ArrayList<HospitalInformation> closedItems =new ArrayList<HospitalInformation>();     // 준비중인 병원 리스트
    Context context;

    // items에 순차적으로 합쳐서 정렬되게 한다.
    public void combineItems(){
        items.addAll(openItems);
        items.addAll(closedItems);
    }

    // 커스텀 리스너 인터페이스 정의
    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }

    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener hosClickListener = null;
    private View.OnClickListener emergencyOpenClosedListener=null;
    private View.OnClickListener openClosedListener=null;

    // onItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메소드
    public void setOnItemClickListener(OnItemClickListener listener){
        this.hosClickListener = listener;
    }
    public void setEROpenClosedClickListener(View.OnClickListener listener){
        this.emergencyOpenClosedListener=listener;
    }
    public void setOpenClosedClickListener(View.OnClickListener listener){
        this.openClosedListener=listener;
    }


    // item으로 만든 itemView를 담아두는 뷰 홀더
    // 일단 필요한 객체의 수만 ViewHolder에 담아서 화면에 뿌려준다
    // 이벤트 처리를 위해 static -> public으로 변경 (items를 static으로 하기엔 찝찝해서..)
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemHospitalName, itemSubject;
        TextView itemDistance;
        Button itemOpenClosed;
        public ViewHolder(final View itemView){
            super(itemView);
            itemHospitalName = itemView.findViewById(R.id.itemHospitalName);
            itemDistance = itemView.findViewById(R.id.itemDistance);
            itemOpenClosed = itemView.findViewById(R.id.itemOpenClosed);

            // 아이템 클릭 이벤트 처리
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(itemView.getContext(), "아이템 클릭", Toast.LENGTH_SHORT).show();
                    int position = getAdapterPosition();        // 클릭한 아이템의 위치
                    if(position != RecyclerView.NO_POSITION){   // 아이템이 있으면
                        // 아이템클릭 이벤트 핸들러 메소드에서 리스너 객체 메소드 호출
                        if(hosClickListener != null){
                            hosClickListener.onItemClick(v, position);  // Subject List에서 처리하기
                        }
                    }
                }
            });

          itemOpenClosed.setOnClickListener(new View.OnClickListener(){
              @Override
              public void onClick(View view) {
                  if(emergencyOpenClosedListener!=null){
                      int position=getAdapterPosition();
                        view.setTag(position); //position을 전달해줘야 listener에서 병원번호 받을 수있음.
                      emergencyOpenClosedListener.onClick(view);
                  } //응급실 리스너가 붙어있다면 !
                  else if(openClosedListener!=null){
                      int position=getAdapterPosition();
                      view.setTag(position); //얘는 itemlistener와 같은 역할을 해야하므로 또 position이 필요함
                      //tag는 꼬리표같은 느낌으로 전해주는것 같음.
                      openClosedListener.onClick(view);
                  }
              }
          });
        }
        public void setItem(HospitalInformation hospital){
            itemHospitalName.setText(hospital.getHospitalName());
            itemDistance.setText(hospital.getDistance());
            if(hospital.findSubject("응급실") && User.subject.toString().equals("응급실"))
            {
                itemOpenClosed.setText("");
                itemOpenClosed.setBackgroundResource(R.drawable.telephone);
                itemDistance.setTextColor(0xFF999999);
                itemHospitalName.setTextColor(0xFF000000);
            }else{      // 정렬된 리스트의 진료중/준비중을 색으로 구분
                itemOpenClosed.setText(hospital.getOpenClosed());
                if(hospital.getOpenClosed()=="진료중"){
                    itemOpenClosed.setTextColor(0x99FF0000);
                    itemDistance.setTextColor(0xFF999999);
                    itemHospitalName.setTextColor(0xFF000000);

                }else if(hospital.getOpenClosed()=="준비중"){
                    itemOpenClosed.setTextColor(0x44000000);
                    itemDistance.setTextColor(0x44000000);
                    itemHospitalName.setTextColor(0x44000000);
                }
            }
        }
    }

    // 임시로 사용
    public void addOpenItem(HospitalInformation item){
        openItems.add(item);
    }
    public void addClosedItem(HospitalInformation item){
        closedItems.add(item);
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
