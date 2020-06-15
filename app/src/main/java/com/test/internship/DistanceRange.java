package com.test.internship;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.PolygonOverlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class DistanceRange extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    Marker marker;
    CircleOverlay circle;
    PolygonOverlay polygon;
    EditText radiusEdit;
    Button button;
    float radius;
    float centerLat;
    float centerLng;
    boolean savedata;
    Switch switchRadius;
    boolean switchState;
    private static SharedPreferences appData;
    Timer timer=new Timer();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distance_range);
        radiusEdit=findViewById(R.id.radiusEdit);
        switchRadius=findViewById(R.id.switchRadius);
        savedata = false;
        marker = new Marker();
        circle=new CircleOverlay();
        polygon=new PolygonOverlay();
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();
        if(savedata){
            switchRadius.setChecked(switchState);
        }

        FragmentManager fm = getSupportFragmentManager();

        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map123);

        mapFragment.getMapAsync(this);

        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);



        button = findViewById(R.id.okay);
        button.setOnClickListener(buttonListener);

//        switchRadius.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            TimerTask alarmTimer;
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                if(isChecked){
//                    save();
//                    alarmTimer=new TimerTask() {
//                        @Override
//                        public void run() {
//                            LatLng nowLatLng = new LatLng(locationSource.getLastLocation().getLatitude(),
//                                    locationSource.getLastLocation().getLongitude());
//
//                            double dis = nowLatLng.distanceTo(new LatLng(centerLat,centerLng));
//                            if(dis>radius){
//                                Toast.makeText(getApplicationContext(), nowLatLng.latitude + ", " + nowLatLng.longitude+" 범위를 벗어났습니다.",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    };
//                    timer.schedule(alarmTimer,0,10000);
//                }
//                else{
//                    alarmTimer.cancel();
//                }
//            }
//        });

    }
//    private Location lastKnownLocation =null;
//    LocationListener locationListener=new LocationListener() {
//        @Override
//        public void onLocationChanged(Location location) {
//            LocationManager lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
//            lastKnownLocation=location;
//        }
//    };

    View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            save();
            Toast.makeText(getApplicationContext(), "등록되었습니다.", Toast.LENGTH_SHORT).show();

        }
    };

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
        load();
        if(savedata){
            marker.setPosition(new LatLng(centerLat, centerLng));
            marker.setMap(naverMap);
            circle.setCenter(new LatLng(centerLat, centerLng));
            circle.setRadius(radius);
            circle.setColor(Color.argb(50,0,255,0));
            circle.setOutlineColor(Color.argb(200,0,255,0));
            circle.setOutlineWidth(10);
            circle.setMap(naverMap);
        }
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

                radius=0.2F;
               if(!(radiusEdit.length()==0)){
                   radius=Float.parseFloat(radiusEdit.getText().toString());
               }
               centerLat = (float)latLng.latitude;
               centerLng = (float)latLng.longitude;
                radius *= 1000;
                marker.setMap(null);
                circle.setMap(null);
                marker.setPosition(latLng);
                marker.setMap(naverMap);
                circle.setCenter(latLng);
                circle.setRadius(radius);
                circle.setColor(Color.argb(50,0,255,0));
                circle.setOutlineColor(Color.argb(200,0,255,0));
                circle.setOutlineWidth(10);
                circle.setMap(naverMap);


                // 카메라 위치 변경
                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(latLng);
                naverMap.moveCamera(cameraUpdate);

                LatLng nowLatLng = new LatLng(locationSource.getLastLocation().getLatitude(),
                        locationSource.getLastLocation().getLongitude());

                double dis = nowLatLng.distanceTo(latLng);
                if(dis>radius){
                    Toast.makeText(getApplicationContext(), latLng.latitude + ", " + latLng.longitude+" 범위를 벗어났습니다.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        };
        naverMap.setOnMapClickListener(mapListener);
    }
    public void save(){
        SharedPreferences.Editor editor = appData.edit();
        editor.putBoolean("ISCHECKED",switchRadius.isChecked());
        editor.putBoolean("SAVEDATA", true);
        editor.putFloat("RADIUS", radius);
        editor.putFloat("LAT", centerLat);
        editor.putFloat("LNG", centerLng);
        editor.apply();
    }
    private void load(){
        switchState= appData.getBoolean("ISCHECKED",false);
        savedata = appData.getBoolean("SAVEDATA", false);
        radius = appData.getFloat("RADIUS", 0.2F);
        centerLat = appData.getFloat("LAT", 36.8851976F);
        centerLng = appData.getFloat("LNG", 126.7735638F);
    }


}
