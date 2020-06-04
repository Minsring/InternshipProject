package com.test.internship;

import java.util.ArrayList;

public class HospitalInformation {
    private String hospitalName = "병원이름";
    private String address = "주소";
    private String callNumber = "전화번호";
//    private String openDay = "여는요일";    // 요일마다 영업시간이 바뀐다면?
    private String distance = "0";         // 목록에 표시할 거라면 판단하는 함수 필요
    private String openClosed = "영업중";         // 목록에 표시할 거라면 판단하는 함수 필요
    private ArrayList<String> subjects = new ArrayList<String>();
    private ArrayList<String> openTime = new ArrayList<String>();
    private ArrayList<String> closedTime = new ArrayList<String>();
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

    // getter()
    public String getHospitalName() { return hospitalName; }
    public String getOpenTime(int num) { return openTime.get(num); }
    public String getClosedTime(int num) { return closedTime.get(num); }
    public int getOpenTimesize(){ return openTime.size();}
    public int getClosedTimesize(){return closedTime.size();}
    public String getAddress() { return address; }
    public String getCallNumber() { return callNumber; }
    // 일단 첫번째 ArrayList 원소 보이게
    public String getSubject() { return subjects.get(0); }
//    public String getOpenDay() { return openDay; }
    public String getDistance() {
        // TODO: 현재위치로 부터 병원과의 거리 구하기
        return distance+"km";
    }
    public String getOpenClosed() {
        // TODO: 영업중인지 아닌지 판단하기
        return openClosed;
    }
    public void setHospitalName(String name){ hospitalName=name; }
    public void setAddress(String address){ this.address=address; }
    public void setCallNumber(String callNumber){ this.callNumber=callNumber; }
//    public void setSubject(String subject){ this.subject=subject; }
//    public void setOpenDay(String openDay){this.openDay=openDay;}
    public void setDistance(String distance){this.distance=distance;}
    public void setOpenClosed(String openClosed){this.openClosed=openClosed;}

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
