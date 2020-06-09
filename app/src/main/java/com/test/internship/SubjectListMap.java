package com.test.internship;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.Locale;

import ted.gun0912.clustering.naver.TedNaverClustering;

public class SubjectListMap extends FragmentActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    ArrayList<HospitalInformation> hospitals = null;
    LatLng latlng = null;
    ConstraintLayout simple = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_list_map);
        Intent getIntent = getIntent();
        hospitals = (ArrayList<HospitalInformation>) getIntent().getSerializableExtra("열린병원리스트");
        FragmentManager fm = getSupportFragmentManager();

        simple = findViewById(R.id.simple);
        simple.setVisibility(View.VISIBLE);

        latlng = hospitals.get(0).getLatLng();


        // 초기 위치 및 맵 타입 설정 // 신평면사무소 근처
        NaverMapOptions options = new NaverMapOptions()
                .camera(new CameraPosition(latlng, 15))
                .mapType(NaverMap.MapType.Basic)
                .locationButtonEnabled(true)
                .compassEnabled(true)
                .stopGesturesEnabled(false)
                .tiltGesturesEnabled(false);
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);

        if (mapFragment == null) {
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) {
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
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

        TedNaverClustering.with(this, naverMap)
                .items(hospitals)
//                .customMarker(new Marker().setPosition(HospitalInformation.get))
                .make();
    }

//    private ArrayList<HospitalInformation> getItems() {
//        LatLngBounds bounds = naverMap.getContentBounds();
//        ArrayList<HospitalInformation> items = new ArrayList<>();
//
//
//    }
//        //돌면서 마커찍는 중 !
////        int i = 0;
//        Iterator<HospitalInformation> it=hospitals.iterator();
//        while(it.hasNext()){
//            HospitalInformation hos=it.next();
//            latlng = hos.getLatLng();
//            Marker marker =new Marker();
//            marker.setPosition(latlng);
//            marker.setCaptionText(hos.getHospitalName());
////            marker.setZIndex(hospitals.size()-i);
////            marker.setHideCollidedCaptions(true);
//            marker.setMap(naverMap);
////            i++;
//        }

}