package com.test.internship;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SubjectListAdapter extends RecyclerView.Adapter<SubjectListAdapter.ViewHolder> {
    ArrayList<HospitalData> items =new ArrayList<HospitalData>();
    ArrayList<HospitalData> openItems =new ArrayList<HospitalData>();      // 진료중인 병원 리스트
    ArrayList<HospitalData> closedItems =new ArrayList<HospitalData>();     // 준비중인 병원 리스트

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
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemHospitalName;
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
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        if(hosClickListener != null){
                            hosClickListener.onItemClick(v, position);
                        }
                    }
                }
            });

          itemOpenClosed.setOnClickListener(new View.OnClickListener(){
              @Override
              public void onClick(View view) {
                  if(emergencyOpenClosedListener!=null){
                      int position=getAdapterPosition();
                        view.setTag(position);
                      emergencyOpenClosedListener.onClick(view);
                  }
                  else if(openClosedListener!=null){
                      int position=getAdapterPosition();
                      view.setTag(position);
                      openClosedListener.onClick(view);
                  }
              }
          });
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        public void setItem(HospitalData hospital){
            itemHospitalName.setText(hospital.getHospitalName());
            itemDistance.setText(hospital.getDistance());
            if(hospital.findSubject("응급실") && User.subject.toString().equals("응급실"))
            {
                itemOpenClosed.setText("");
                itemOpenClosed.setBackgroundResource(R.drawable.emergency_call);
                itemDistance.setTextColor(0xFF999999);
                itemHospitalName.setTextColor(0xFF000000);
            }
            else{      // 정렬된 리스트의 진료중/준비중을 색으로 구분
                itemOpenClosed.setText(hospital.getOpenClosed());
                if(hospital.getOpenClosed()=="진료중"){
                    itemOpenClosed.setTextColor(0xFFFF0000);
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

    // items에 순차적으로 합쳐서 정렬되게 한다.
    public void combineItems(){
        items.addAll(openItems);
        items.addAll(closedItems);
    }
    public void addOpenItem(HospitalData item){
        openItems.add(item);
    }
    public void addClosedItem(HospitalData item){
        closedItems.add(item);
    }
    public HospitalData getItem(int position){
        return items.get(position);
    }
    public ArrayList<HospitalData> getOpenItem(){
        if(openItems.size()==0){ return null; }
        else{ return openItems; }
    }
    public ArrayList<HospitalData> getClosedItem(){
        if(closedItems.size()==0){ return null; }
        else{ return closedItems; }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override       // ViewHolder객체가 생성될 때 자동으로 호출
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.hospital_info_style, viewGroup, false);

        // 새로 생성된 ViewHolder 객체를 뷰 객체를 담아서 리턴
        return new ViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override       // ViewHolder객체가 재사용될 때 자동으로 호출
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        HospitalData item = items.get(position);
        viewHolder.setItem(item);
    }
}