package com.test.internship;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

    public CustomAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.protector_list, parent, false);
        }

        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.personimg);
        TextView personNameTextView = (TextView) convertView.findViewById(R.id.personName);
        TextView personNumTextView = (TextView) convertView.findViewById(R.id.personNum);
        Button delete = (Button) convertView.findViewById(R.id.btndelete);

        ListViewItem listViewItem = listViewItemList.get(position);

        iconImageView.setImageDrawable(listViewItem.getIcon());
        personNameTextView.setText(listViewItem.getPersonName());
        personNumTextView.setText(listViewItem.getPersonNum());

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
