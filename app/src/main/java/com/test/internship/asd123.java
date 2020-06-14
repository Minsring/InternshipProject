package com.test.internship;

import android.graphics.PointF;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.Locale;

public class asd123 extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    Marker marker;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asd123);
        FragmentManager fm = getSupportFragmentManager();

        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map123);

        mapFragment.getMapAsync(this);

        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        marker = new Marker();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,  @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull final NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

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

        // 컨트롤 설정
        UiSettings uiSettings = naverMap.getUiSettings();

        uiSettings.setCompassEnabled(true);
        uiSettings.setLocationButtonEnabled(true);


        NaverMap.OnMapClickListener mapListener = new NaverMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
                Toast.makeText(getApplicationContext(), latLng.latitude + ", " + latLng.longitude,
                        Toast.LENGTH_SHORT).show();
                marker.setMap(null);
                marker.setPosition(latLng);
                marker.setMap(naverMap);
            }
        };
        naverMap.setOnMapClickListener(mapListener);


    }
}
