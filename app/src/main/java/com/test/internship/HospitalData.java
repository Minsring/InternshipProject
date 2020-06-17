package com.test.internship;

import com.naver.maps.geometry.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

import ted.gun0912.clustering.clustering.TedClusterItem;
import ted.gun0912.clustering.geometry.TedLatLng;


// 클러스터링을 위한 라이브러리 추가
public class HospitalData implements Serializable, TedClusterItem {
    // 클래스의 버전을 의미, 객체를 전달하고 수신할 때 사용하는 클래스 파일이 동일한지 체크하는 용도로 사용
    private static final long serialVersionUID = 1L;

    private String hospitalName = "병원이름";
    private String address = "주소";
    private String callNumber = "전화번호";
    private String distance = "0km";
    private String openClosed = "영업중";
    private ArrayList<String> subjects = new ArrayList<String>();
    private ArrayList<String> openTime = new ArrayList<String>();
    private ArrayList<String> closedTime = new ArrayList<String>();
    private int numSubjects = 0;
    //시간 0번째 -> 평일, (1번째 -> 토요일, 2번째 -> 일요일) -> 없을수도 있음
    private double lat = 0.0f;     // 위도
    private double lng = 0.0f;     // 경도

    public HospitalData(){}
    public HospitalData(double lat, double lng){
        this.lat = lat;
        this.lng = lng;
    }
    public HospitalData(LatLng latLng){
        this.lat = latLng.latitude;
        this.lng = latLng.longitude;
    }


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

    public String getDistance() { return distance; }
    public String getOpenClosed() { return openClosed; }
    public void setHospitalName(String name){ hospitalName=name; }
    public void setAddress(String address){ this.address=address; }
    public void setCallNumber(String callNumber){ this.callNumber=callNumber; }

    public void setDistance(String distance){this.distance=distance;}
    public void setOpenClosed(String openClosed){this.openClosed=openClosed;}
    public void setNumSubjects(int numSubjects){ this.numSubjects = numSubjects;}

    public void addOpenTime(String openTime){ this.openTime.add(openTime); }
    public void addClosedTime(String closedTime){ this.closedTime.add(closedTime); }

    // 위도 경도
    public LatLng getLatLng(){
        return new LatLng(lat, lng);
    }
    public void setLatLng(LatLng latLng) {
        this.lat = latLng.latitude;
        this.lng = latLng.longitude;
    }

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

    @Override
    public TedLatLng getTedLatLng() {
        return new TedLatLng(lat, lng);
    }
}
