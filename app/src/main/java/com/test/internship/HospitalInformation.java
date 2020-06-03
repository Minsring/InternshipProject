package com.test.internship;

public class HospitalInformation {
    private String hospitalName;
    private String openTime;
    private String closedTime;
    private String address;
    private String callNumber;
    private String subject;
    private String openDay;

    // 생성자 -> 사용할지는 모르게씀
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
}
