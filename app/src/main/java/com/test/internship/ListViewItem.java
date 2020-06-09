package com.test.internship;

import android.graphics.drawable.Drawable;
import android.widget.Button;

public class ListViewItem {
    private Drawable iconDrawable ;
    private String personName ;
    private String personNum ;
    private Button delete;

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setPersonName(String name) {
        personName = name ;
    }
    public void setPersonNum(String num) {
        personNum = num ;
    }

    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getPersonName() {
        return this.personName ;
    }
    public String getPersonNum() {
        return this.personNum ;
    }


}
