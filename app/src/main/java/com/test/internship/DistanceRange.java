package com.test.internship;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

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
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;

import java.util.Locale;

import static com.test.internship.Register.person1_n;
import static com.test.internship.Register.person1_p;
import static com.test.internship.Register.person2_n;
import static com.test.internship.Register.person2_p;
import static com.test.internship.Register.person3_n;
import static com.test.internship.Register.person3_p;
import static com.test.internship.Register.person4_n;
import static com.test.internship.Register.person4_p;
import static com.test.internship.Register.person5_n;
import static com.test.internship.Register.person5_p;

public class DistanceRange extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 1001;
    private static SharedPreferences appData;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    Marker marker;
    Marker regMarker;
    CircleOverlay circle;
    CircleOverlay regCircle;
    EditText radiusEdit;
    Button buttonOkay;
    Button buttonRegister;
    LinearLayout layoutRadius;
    View mapRadius;
    Switch switchRadius;
    float radius;
    float centerLat;
    float centerLng;
    boolean saveData;
    boolean switchState;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distance_range);

        radiusEdit=findViewById(R.id.radiusEdit);
        switchRadius=(Switch)findViewById(R.id.switchRadius);
        mapRadius = findViewById(R.id.mapRadius);
        layoutRadius = findViewById(R.id.layoutRadius);
        buttonOkay = findViewById(R.id.okay);
        buttonRegister=findViewById(R.id.buttonRegister);

        saveData = false;
        marker = new Marker();
        regMarker = new Marker();
        circle = new CircleOverlay();
        regCircle = new CircleOverlay();


        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        appData = getSharedPreferences("appData", MODE_PRIVATE);

        // 저장된 기록 로딩
        load();
        if(saveData){
            switchRadius.setChecked(switchState);

            if(switchRadius.isChecked()==true){
                layoutRadius.setVisibility(View.VISIBLE);
                mapRadius.setVisibility(View.VISIBLE);
                buttonOkay.setVisibility(View.VISIBLE);
                if ( Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( DistanceRange.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                            MY_PERMISSIONS_REQUEST_LOCATION );
                }
                else{
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 1, gpsLocationListener);
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 300000, 1, gpsLocationListener);
                }
            }
            else{
                regCircle.setMap(null);
                regMarker.setMap(null);
                circle.setMap(null);
                marker.setMap(null);
                lm.removeUpdates(gpsLocationListener);
                layoutRadius.setVisibility(View.INVISIBLE);
                mapRadius.setVisibility(View.INVISIBLE);
                buttonOkay.setVisibility(View.INVISIBLE);
            }
        }

        // 접근 권한 설정
        int permissionCheck= ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck!= PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"권한승인필요",Toast.LENGTH_LONG).show();
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                Toast.makeText(this, "위치접근 권한 필요", Toast.LENGTH_SHORT).show();
            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_LOCATION);
                Toast.makeText(this, "위치접근 권한 필요", Toast.LENGTH_LONG).show();
            }
        }

        // Map 생성
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.mapRadius);
        mapFragment.getMapAsync(this);

        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        // okay 버튼 리스너 연결
        buttonOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
                if(person1_n==null && person1_p==null && person2_n==null && person2_p==null
                        && person3_n==null && person3_p==null && person4_n==null && person4_p==null && person5_n==null && person5_p==null) {
                    Toast.makeText(getApplicationContext(), "보호자 정보를 먼저 등록해주세요.", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getApplicationContext(), "등록되었습니다.", Toast.LENGTH_SHORT).show();
                onMapReady(naverMap);
            }
        });

        //보호자 등록 버튼 리스너 달기
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Register.class);
                startActivity(intent);
            }
        });

        // 스위치 리스너 연결
        switchRadius.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    save();

                    layoutRadius.setVisibility(View.VISIBLE);
                    mapRadius.setVisibility(View.VISIBLE);
                    buttonOkay.setVisibility(View.VISIBLE);


                    if ( Build.VERSION.SDK_INT >= 23 &&
                            ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions( DistanceRange.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                                MY_PERMISSIONS_REQUEST_LOCATION );
                    }
                    else{
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 1, gpsLocationListener);
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 300000, 1, gpsLocationListener);
                    }
                }
                else{
                    save();
                    lm.removeUpdates(gpsLocationListener);
                    regCircle.setMap(null);
                    regMarker.setMap(null);
                    circle.setMap(null);
                    marker.setMap(null);
                    layoutRadius.setVisibility(View.INVISIBLE);
                    mapRadius.setVisibility(View.INVISIBLE);
                    buttonOkay.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    // 현재위치가 바뀌면 호출되는 리스너너
    final LocationListener gpsLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            LatLng nowLatLng = new LatLng(latitude,longitude);
            LatLng centerLatLng = new LatLng(centerLat,centerLng);

            double dis = nowLatLng.distanceTo(centerLatLng); //m단위를 double로 반환
            if(dis>radius){
               if(switchRadius.isChecked()==true){
                   Toast.makeText(getApplicationContext(), nowLatLng.latitude + ", " + nowLatLng.longitude+" 범위를 벗어났습니다.",
                           Toast.LENGTH_LONG).show();
                   if(person1_n!=null && person1_p != null){
                       Setting.sendSMS(person1_p, person1_n, 3);
                   }
                   if(person2_n!=null && person2_p != null){
                       Setting.sendSMS(person2_p, person2_n, 3);
                   }
                   if(person3_n!=null && person3_p != null){
                       Setting.sendSMS(person3_p, person3_n, 3);
                   }
                   if(person4_n!=null && person4_p != null){
                       Setting.sendSMS(person4_p, person4_n, 3);
                   }
                   if(person5_n!=null && person5_p != null){
                       Setting.sendSMS(person5_p, person5_n, 3);
                   }
               }
            }
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { }
        @Override
        public void onProviderEnabled(String provider) { }
        @Override
        public void onProviderDisabled(String provider) { }
    };

    // 위치 권한 허가
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,  @NonNull int[] grantResults) {
        if(requestCode==MY_PERMISSIONS_REQUEST_LOCATION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"승인 허가",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this,"승인 불허가",Toast.LENGTH_LONG).show();
            }
            return;
        }
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) {
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // 지도가 생성되면 자동으로 호출
    @UiThread
    @Override
    public void onMapReady(@NonNull final NaverMap naverMap) {
        this.naverMap = naverMap;

        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        load();

        circle.setMap(null);
        marker.setMap(null);

        if(saveData){
            if(switchRadius.isChecked()==true){
                regMarker.setPosition(new LatLng(centerLat, centerLng));
                regMarker.setIcon(MarkerIcons.BLACK);
                regMarker.setIconTintColor(Color.RED);
                regMarker.setMap(naverMap);
                regCircle.setCenter(new LatLng(centerLat, centerLng));
                regCircle.setRadius(radius);
                regCircle.setColor(Color.argb(50,255,0,0));
                regCircle.setOutlineColor(Color.argb(200,255,0,0));
                regCircle.setOutlineWidth(10);
                regCircle.setMap(naverMap);
                circle.setMap(null);
                marker.setMap(null);
            }
        }
        saveData = false;

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


        // 지도 위를 클릭하면 마커가 생기고 화면 중앙으로 카메라 이동
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
            }
        };
        naverMap.setOnMapClickListener(mapListener);
    }

    // 데이터 저장
    public void save(){
        SharedPreferences.Editor editor = appData.edit();
        editor.putBoolean("ISCHECKED",switchRadius.isChecked());
        editor.putBoolean("SAVEDATA", true);
        editor.putFloat("RADIUS", radius);
        editor.putFloat("LAT", centerLat);
        editor.putFloat("LNG", centerLng);
        editor.apply();
    }

    // 데이터 로딩
    private void load(){
        switchState= appData.getBoolean("ISCHECKED",false);
        saveData = appData.getBoolean("SAVEDATA", false);
        radius = appData.getFloat("RADIUS", 0.2F);
        centerLat = appData.getFloat("LAT", 36.4735165F);
        centerLng = appData.getFloat("LNG", 128.5024796F);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}