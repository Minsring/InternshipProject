package com.test.internship;

import android.content.Context;
import android.drm.DrmStore;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

///커스텀 리스너 정의 -> 추가
public class CustomAdapter extends BaseAdapter {

//    Context context;
//    LayoutInflater layoutInflater;
//    ArrayList<String>data;
//    ListView listView;

    //추가
    private Context mcontext = null;
    private ArrayList<ProtectorData> mdata = null;
    private int layout = 0;
    private LayoutInflater inflater = null;

    public CustomAdapter(Context context, int layout, ArrayList<ProtectorData> mdata) {
        this.mcontext = context;
        this.layout = layout;
        this.mdata = mdata;
        this.inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }
//    public CustomAdapter(Context context, ArrayList<String>data){
//        this.context = context;
//        this.layoutInflater = LayoutInflater.from(context);
////        this.listener=listener;
//        this.data = data;
//    }

    @Override
    public int getCount() {
        return mdata.size();
    }

    @Override
    public Object getItem(int position) {
        return mdata.get(position).getPersonname();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
        }
        ImageView img = convertView.findViewById(R.id.img);
        TextView personname = convertView.findViewById(R.id.personname);
        TextView personnum = convertView.findViewById(R.id.personnum);
        Button btndelete = convertView.findViewById(R.id.btndelete);

        img.setImageResource(mdata.get(position).getImg());
        personname.setText(mdata.get(position).getPersonname());
        personnum.setText(mdata.get(position).getPersonnum());

//        View view = layoutInflater.inflate(R.layout.protectorinfo_style,null);
//        TextView t1 = view.findViewById(R.id.personinfo);
//        t1.setText(data.get(position));


//        View bodyView = view.findViewById(R.id.body);
//        Button btn = view.findViewById(R.id.btndelete);
//
//        bodyView.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Toast.makeText(context,"바디클릭 테스트",Toast.LENGTH_SHORT).show();
//            }
//        });
        return convertView;
    }
}

