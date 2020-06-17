package com.test.internship;

public class ProtectorData {
    int img;
    String personName;
    String personNum;

    public ProtectorData(int img, String personName, String personNum){
        this.img =img;
        this.personName = personName;
        this.personNum = personNum;
    }

    public int getImg() {
        return img;
    }
    public String getPersonName(){
        return personName;
    }
    public String getPersonNum(){
        return personNum;
    }

}
