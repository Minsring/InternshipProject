package com.test.internship;

public class ProtectorData {
    int img;
    String personname;
    String personnum;

    public ProtectorData(int img, String personname, String personnum){
        this.img =img;
        this.personname=personname;
        this.personnum=personnum;
    }

    public int getImg() {
        return img;
    }
    public String getPersonname(){
        return personname;
    }
    public String getPersonnum(){
        return personnum;
    }

}
