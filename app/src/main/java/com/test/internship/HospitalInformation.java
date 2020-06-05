package com.test.internship;

import java.io.Serializable;
import java.util.ArrayList;

// 객체를 직렬화해야 액티비티에 데이터 객체를 넘겨줄 수 있다.
public class HospitalInformation implements Serializable {
    // 클래스의 버전을 의미, 객체를 전달하고 수신할 때 사용하는 클래스 파일이 동일한지 체크하는 용도로 사용
    private static final long serialVersionUID = 1L;

    private String hospitalName = "병원이름";
    private String address = "주소";
    private String callNumber = "전화번호";
//    private String openDay = "여는요일";    // 요일마다 영업시간이 바뀐다면?
    private String distance = "0km";         // 목록에 표시할 거라면 판단하는 함수 필요
    private String openClosed = "영업중";         // 목록에 표시할 거라면 판단하는 함수 필요
    private ArrayList<String> subjects = new ArrayList<String>();
    private ArrayList<String> openTime = new ArrayList<String>();
    private ArrayList<String> closedTime = new ArrayList<String>();
    private int numSubjects = 0;
    //시간 0번째 -> 평일, (1번째 -> 토요일, 2번째 -> 일요일) -> 없을수도 있음

    // 생성자 -> 사용할지는 모르게씀
    // 일단 distance와 openClosed는 제외하고 만듬
    public HospitalInformation(){}
//    public HospitalInformation(String hospitalName, String openTime, String closedTime, String address,
//                               String callNumber, String subject, String openDay){
//        this.hospitalName = hospitalName;
//        this.openTime = openTime;
//        this.closedTime = closedTime;
//        this.address = address;
//        this.callNumber = callNumber;
//        this.subject = subject;
//        this.openDay = openDay;
//    }


    public boolean openTime_isEmpty(int num){
        if(num > openTime.size()-1) return true;
        else return false;
    }

    // getter()
    public String getHospitalName() { return hospitalName; }
    public String getOpenTime(int num) { return openTime.get(num); }
    public String getClosedTime(int num) { return closedTime.get(num); }
    public int getOpenTimesize(){ return openTime.size();}
    public int getClosedTimesize(){return closedTime.size();}
    public String getAddress() { return address; }
    public String getCallNumber() { return callNumber; }
    // 일단 첫번째 ArrayList 원소 보이게
    public String getSubject(int num) { return subjects.get(num); }
    public int getNumSubjects() { return numSubjects;}
//    public String getOpenDay() { return openDay; }
    public String getDistance() { return distance; }
    public String getOpenClosed() { return openClosed; }
    public void setHospitalName(String name){ hospitalName=name; }
    public void setAddress(String address){ this.address=address; }
    public void setCallNumber(String callNumber){ this.callNumber=callNumber; }
//    public void setSubject(String subject){ this.subject=subject; }
//    public void setOpenDay(String openDay){this.openDay=openDay;}
    public void setDistance(String distance){this.distance=distance;}
    public void setOpenClosed(String openClosed){this.openClosed=openClosed;}
    public void setNumSubjects(int numSubjects){ this.numSubjects = numSubjects;}

    public void addOpenTime(String openTime){ this.openTime.add(openTime); }
    public void addClosedTime(String closedTime){ this.closedTime.add(closedTime); }



    // subject 추가 및 찾기
    public void addSubject(String subject){
        this.subjects.add(subject);
    }
    public boolean findSubject(String subject){
        for(String temp: subjects){
            if(temp.equals(subject)) return true;
        }
        return false;
    }
}
