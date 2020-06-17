package com.test.internship;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;
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
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.Locale;

public class HospitalInformation extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;

    HospitalData hospital = null;
    LatLng latLng = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital_information);
        Button callHospital = findViewById(R.id.callHospitalBtn);
        Intent getIntent = getIntent();
        hospital = (HospitalData) getIntent.getSerializableExtra("병원");

        callHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = hospital.getCallNumber();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
                startActivity(intent);
            }
        });


        // 탭 레이아웃 설정
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        changeView(0);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            // 선택 안된 상태에서 선택 된 상태의 탭에 대한 이벤트
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                changeView(pos);
            }

            @Override
            // 선택된 상태에서 선택되지 않음으로 바뀐 탭에 대한 이벤트
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            // 이미 선택된 탭이 사용자에 의해 다시 선택된 탭의 이벤트
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        TextView Title = findViewById(R.id.Title);
        TextView info_Name = findViewById(R.id.info_Name);
        TextView info_Call = findViewById(R.id.info_Call);
        TextView info_Address = findViewById(R.id.info_Address);
        TextView info_Subject = findViewById(R.id.info_Subject);
        TextView info_Distance = findViewById(R.id.info_Distance);
        TextView info_Weekday = findViewById(R.id.info_Weekday);
        TextView info_Saturday = findViewById(R.id.info_Saturday);
        TextView info_Sunday = findViewById(R.id.info_Sunday);

        Title.setText(hospital.getHospitalName());
        info_Name.setText("병원명 : " + hospital.getHospitalName());
        info_Call.setText("전화번호 : " + hospital.getCallNumber());
        info_Distance.setText("거리 : " + hospital.getDistance());
        info_Address.setText("주소 : " + hospital.getAddress());
        String allSubjects = "";
        for (int pos = 0; pos < hospital.getNumSubjects(); pos++) {
            allSubjects = allSubjects + hospital.getSubject(pos) + "  ";
        }
        info_Subject.setText("진료과목 : " + allSubjects);
        if (hospital.openTime_isEmpty(0) == false) {
            info_Weekday.setText("주중 진료시간 : " + hospital.getOpenTime(0) + " ~ " + hospital.getClosedTime(0));
        }
        if (hospital.openTime_isEmpty(1) == false) {
            info_Saturday.setText("토요일 진료시간 : " + hospital.getOpenTime(1) + " ~ " + hospital.getClosedTime(1));
        }
        if (hospital.openTime_isEmpty(2) == false) {
            info_Sunday.setText("일요일 진료시간 : " + hospital.getOpenTime(2) + " ~ " + hospital.getClosedTime(2));
        }

        // 표시해야할 위도, 경도
        latLng = hospital.getLatLng();
        FragmentManager hosFm = getSupportFragmentManager();
        MapFragment hosMapFragment = (MapFragment) hosFm.findFragmentById(R.id.info_map);

        NaverMapOptions hosOptions = new NaverMapOptions()
                .locationButtonEnabled(true)
                .compassEnabled(true)
                .tiltGesturesEnabled(false);

        if (hosMapFragment == null) {
            hosMapFragment = MapFragment.newInstance(hosOptions);
            hosFm.beginTransaction().add(R.id.info_map, hosMapFragment).commit();
        }

        hosMapFragment.getMapAsync(this);

        // 현재위치
        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
    }
    private void changeView(int index){
        LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.content_info);
        LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.content_map);

        switch(index){
            case 0:
                linearLayout1.setVisibility(View.VISIBLE);
                linearLayout2.setVisibility(View.INVISIBLE);
                break;
            case 1:
                linearLayout1.setVisibility(View.INVISIBLE);
                linearLayout2.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) {
                naverMap.setLocationTrackingMode(LocationTrackingMode.Face);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

    @Override  // 네이버 맵에서 오버레이 추가, 상호작용하는 등 기능 대부분을 이 클래스 에서 제공
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

        // 초기 카메라 위치
        CameraPosition cameraPosition = new CameraPosition(latLng, 16);
        naverMap.setMinZoom(6.0);
        naverMap.setMaxZoom(18.0);

        UiSettings uiSettings = naverMap.getUiSettings();
        // ui설정
        uiSettings.setCompassEnabled(true);
        uiSettings.setScaleBarEnabled(true);
        uiSettings.setZoomControlEnabled(true);
        uiSettings.setLocationButtonEnabled(true);
        uiSettings.setTiltGesturesEnabled(false);

        naverMap.setCameraPosition(cameraPosition);
        naverMap.setLocationSource(locationSource);

        // 마커 생성
        Marker marker = new Marker();
        marker.setPosition(latLng);
        marker.setCaptionText(hospital.getHospitalName());
        if(hospital.getOpenClosed().equals("진료중")) marker.setIcon(OverlayImage.fromResource(R.drawable.custom_icon_open));
        else marker.setIcon(OverlayImage.fromResource(R.drawable.custom_icon_closed));
        marker.setWidth(120);
        marker.setHeight(150);
        marker.setMap(naverMap);
    }
}