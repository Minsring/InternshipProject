package com.test.internship;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class SubjectListMap extends FragmentActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    ArrayList<HospitalInformation>  hospitals=null;
    double lat,lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_list_map);
        Intent getIntent=getIntent();
        hospitals=(ArrayList<HospitalInformation>)getIntent().getSerializableExtra("열린병원리스트");
//        if(hospitals==null){
//            Toast.makeText(this,"아무것도 전달되지 않았다",Toast.LENGTH_LONG).show();
//        }
//        else{
//            Iterator<HospitalInformation> it=hospitals.iterator();
//            Toast.makeText(this,Integer.toString(hospitals.size()),Toast.LENGTH_LONG).show();
//        }
        //MapFragment는 지도에 대한 뷰 역할만을 담당
        //API를 호출하려면 인터페이스 역할을 하는 NaverMap 객체가 필요함
        //getMapAsync() 메서드로 OnMapReadyCallback을 등록하면 비동기로 NaverMap 객체를 얻을 수 있음
        //NaverMap 객체가 준비되면 onMapReady() 콜백 메서드가 호출됨
        FragmentManager fm = getSupportFragmentManager();

        lat=hospitals.get(0).getLat();
        lng=hospitals.get(0).getLng();

        // 초기 위치 및 맵 타입 설정 // 신평면사무소 근처
        NaverMapOptions options = new NaverMapOptions()
                .camera(new CameraPosition(new LatLng(lat,lng), 15))
                .mapType(NaverMap.MapType.Basic)
                .locationButtonEnabled(true)
                .compassEnabled(true)
                .stopGesturesEnabled(false)
                .tiltGesturesEnabled(false);
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);

        if(mapFragment == null){
            mapFragment = MapFragment.newInstance(options);
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

        // 사용자 위치 받아오기
        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
    }

    // 사용자 위치 받아오기
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)){
            if(!locationSource.isActivated()){
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap){
        this.naverMap = naverMap;

        // 지도 타입 설정
        naverMap.setMapType(NaverMap.MapType.Basic);
        naverMap.setLocale(new Locale("ko"));
        // 지도에 표시할 부가적 정보
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, true);
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, true);
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_MOUNTAIN, true);

        // 건물명 같은 심볼의 크기 조절 (0~2)
        naverMap.setSymbolScale(1.5f);


        // 카메라 위치를 한반도 인근으로 제한
        naverMap.setExtent(new LatLngBounds(new LatLng(31.43, 122.37), new LatLng(44.35, 132)));
        naverMap.setMinZoom(6.0);
        naverMap.setMaxZoom(18.0);

        // LocationButton을 누르면 사용자의 현위치로 이동
        naverMap.setLocationSource(locationSource);


        // 컨트롤 설정
        UiSettings uiSettings = naverMap.getUiSettings();

        uiSettings.setCompassEnabled(true);
        uiSettings.setLocationButtonEnabled(true);

        //돌면서 마커찍는 중 !
        Iterator<HospitalInformation> it=hospitals.iterator();
        while(it.hasNext()){
            HospitalInformation hos=it.next();
            double lat=hos.getLat();
            double lng=hos.getLng();
            Marker marker =new Marker();
            marker.setPosition(new LatLng(lat,lng));
            marker.setMap(naverMap);
        }
    }
}