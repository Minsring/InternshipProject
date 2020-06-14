package com.test.internship;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
public class asd123 extends AppCompatActivity implements OnMapReadyCallback {

    LatLng latLng;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.asd123);
        FragmentManager fm = getSupportFragmentManager();
        NaverMapOptions options = new NaverMapOptions()
                .camera(new CameraPosition(new LatLng(36.0928011, 128.424746), 8))
                .mapType(NaverMap.MapType.Basic);

        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map123);
        if(mapFragment == null){
            mapFragment = MapFragment.newInstance(options);
            fm.beginTransaction().add(R.id.map123, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
        //주석주석






    }


    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

    }
}
