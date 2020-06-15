package com.test.internship;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ItemView extends LinearLayout {
    private ImageView mImg;
    private TextView mName;
    private  TextView mNum;

    public ItemView(Context context, ProtectorData protectorData){
        super(context);

        //인플레이션
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.protectorinfo_style, this,true);

        //set
        mImg = findViewById(R.id.img);
        mImg.setImageResource(protectorData.getImg());

        mName = findViewById(R.id.personname);
        mName.setText(protectorData.getPersonname());

        mNum = findViewById(R.id.personnum);
        mNum.setText(protectorData.getPersonnum());
    }

}
