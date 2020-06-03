package com.test.internship;

public class HospitalInformation {
    private String hospitalName = "병원이름";
    private String openTime = "여는시간";
    private String closedTime = "닫는시간";
    private String address = "주소";
    private String callNumber = "전화번호";
    private String subject = "진료과목";
    private String openDay = "여는요일";    // 요일마다 영업시간이 바뀐다면?
    private String distance = "0";         // 목록에 표시할 거라면 판단하는 함수 필요
    private String openClosed = "영업중";         // 목록에 표시할 거라면 판단하는 함수 필요

    // 생성자 -> 사용할지는 모르게씀
    // 일단 distance와 openClosed는 제외하고 만듬
    public HospitalInformation(){}
    public HospitalInformation(String hospitalName, String openTime, String closedTime, String address,
                               String callNumber, String subject, String openDay){
        this.hospitalName = hospitalName;
        this.openTime = openTime;
        this.closedTime = closedTime;
        this.address = address;
        this.callNumber = callNumber;
        this.subject = subject;
        this.openDay = openDay;
    }

    // getter()
    public String getHospitalName() { return hospitalName; }
    public String getOpenTime() { return openTime; }
    public String getClosedTime() { return closedTime; }
    public String getAddress() { return address; }
    public String getCallNumber() { return callNumber; }
    public String getSubject() { return subject; }
    public String getOpenDay() { return openDay; }
    public String getDistance() {
        // TODO: 현재위치로 부터 병원과의 거리 구하기
        return distance+"km";
    }
    public String getOpenClosed() {
        // TODO: 영업중인지 아닌지 판단하기
        return openClosed;
    }
    public void setHospitalName(String name){ hospitalName=name; }
    public void setOpenTime(String openTime){ this.openTime=openTime; }
    public void setAddress(String address){ this.address=address; }
    public void setCallNumber(String callNumber){ this.callNumber=callNumber; }
    public void setClosedTime(String closedTime){ this.closedTime=closedTime; }
    public void setSubject(String subject){ this.subject=subject; }
    public void setOpenDay(String openDay){this.openDay=openDay;}
    public void setDistance(String distance){this.distance=distance;}
    public void setOpenClosed(String openClosed){this.openClosed=openClosed;}
}
