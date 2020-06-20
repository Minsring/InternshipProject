package com.test.internship;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.test.internship.Register.personData;

public class RegisterAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ProtectorData> mData;
    private LayoutInflater inflater;
    private int layout;

    public RegisterAdapter(Context context, int layout, ArrayList<ProtectorData> mData) {
        this.mContext = context;
        this.layout = layout;
        this.mData = mData;
        this.inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
        }
        ImageView img = convertView.findViewById(R.id.img);
        final TextView personName = convertView.findViewById(R.id.personname);
        TextView personNum = convertView.findViewById(R.id.personnum);
        LinearLayout item_layout = convertView.findViewById(R.id.item_layout);

        img.setImageResource(mData.get(position).getImg());
        personName.setText(mData.get(position).getPersonName());
        personNum.setText(mData.get(position).getPersonNum());

        item_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(personData.get(position) != null) {
                    personData.remove(position);
                    Register.num--;
                    if(Register.num<0) Register.num=0;
                    Register.registerAdapter.notifyDataSetChanged();
                    Toast.makeText(mContext, "선택한 보호자의 정보를 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                    String name = "";
                    for(int i = 0; i<Register.num; i++){
                        name+= mData.get(i).getPersonName();
                    }
                    Toast.makeText(mContext, name, Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
        return convertView;
    }
}
