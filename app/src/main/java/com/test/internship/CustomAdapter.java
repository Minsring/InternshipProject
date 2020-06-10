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
    ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();
    Context context;

    public CustomAdapter(Context context, ArrayList<ListViewItem> listViewItemList){
        this.context = context;
        this.listViewItemList = listViewItemList;
    }

    public <listener> CustomAdapter(ProtectorList <listener> context, ArrayList<String> protectorName) {

    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        final int pos = position;
//        final Context context = parent.getContext();

        if (convertView == null) {
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = LayoutInflater.from(context).inflate(R.layout.protectorinfo_style,null);
        }

        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.personimg);
        TextView personNameTextView = (TextView) convertView.findViewById(R.id.personName);
        TextView personNumTextView = (TextView) convertView.findViewById(R.id.personNum);
        Button delete = (Button) convertView.findViewById(R.id.btndelete);

        ListViewItem listViewItem = listViewItemList.get(position);
        iconImageView.setImageDrawable(listViewItem.getIcon());
        personNameTextView.setText(listViewItem.getPersonName());
        personNumTextView.setText(listViewItem.getPersonNum());


        Button button = (Button)convertView.findViewById(R.id.btndelete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 삭제 처리 부분
            }
        });

        return convertView;
    }

    //getItem 겹쳐 -> getList
    public Object getList(int position) {
        return listViewItemList.get(position);
    }

    public void addItem(Drawable icon, String name, String num) {
        ListViewItem item = new ListViewItem();

        item.setIcon(icon);
        item.setPersonName(name);
        item.setPersonNum(num);

        listViewItemList.add(item);
    }
}
