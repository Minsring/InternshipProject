package com.test.internship;

import android.Manifest;
import android.content.Context;
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
    TimerTask alarmTimer;
    int switchFlag;
    private static SharedPreferences appData;
    Timer timer;
    private final int MY_PERMISSIONS_REQUEST_LOCATION=1001;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timer=new Timer();
        setContentView(R.layout.distance_range);
        radiusEdit=findViewById(R.id.radiusEdit);
        switchRadius=(Switch)findViewById(R.id.switchRadius);
        savedata = false;
        marker = new Marker();
        circle=new CircleOverlay();
        polygon=new PolygonOverlay();
        switchFlag=0;
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        appData = getSharedPreferences("appData", MODE_PRIVATE);

        load();
        if(savedata){
            switchRadius.setChecked(switchState);
//            if(switchRadius.isChecked()){
//                save();
//                if ( Build.VERSION.SDK_INT >= 23 &&
//                        ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
//                    System.out.println("1");
//                    ActivityCompat.requestPermissions( DistanceRange.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
//                            MY_PERMISSIONS_REQUEST_LOCATION );
//                    System.out.println("10000000000000000000000000000000000000");
//                }
//                else{
//                    System.out.println("10000000000000000000000000000000000000");
//                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                            1000,
//                            1,
//                            gpsLocationListener);
//                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                            1000,
//                            1,
//                            gpsLocationListener);
//                    System.out.println("4");
//                }
//            }

        }
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

        FragmentManager fm = getSupportFragmentManager();

        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map123);

        mapFragment.getMapAsync(this);

        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);



        button = findViewById(R.id.okay);
        button.setOnClickListener(buttonListener);

        switchRadius.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    save();

                    if ( Build.VERSION.SDK_INT >= 23 &&
                            ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                        System.out.println("1");
                        ActivityCompat.requestPermissions( DistanceRange.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                                MY_PERMISSIONS_REQUEST_LOCATION );
                    }
                    else{
                        System.out.println("------------------------------------------");
                        System.out.println(isChecked);
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                1000,
                                1,
                                gpsLocationListener);
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                1000,
                                1,
                                gpsLocationListener);
                        System.out.println("4");
                    }

                }
                else{
                    save();
                    lm.removeUpdates(gpsLocationListener);
                }
            }
        });

    }

    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            String provider = location.getProvider();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            LatLng nowLatLng=new LatLng(latitude,longitude);
            LatLng centerLatLng=new LatLng(centerLat,centerLng);

            double dis = nowLatLng.distanceTo(centerLatLng); //m단위를 double로 반환
            System.out.println(dis);
            System.out.println(radius);
            System.out.println(switchState);
            if(dis>radius){
                Toast.makeText(getApplicationContext(), nowLatLng.latitude + ", " + nowLatLng.longitude+" 범위를 벗어났습니다.",
                        Toast.LENGTH_LONG).show();
            }
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };




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
        if(requestCode==MY_PERMISSIONS_REQUEST_LOCATION){           //위치 허락받으려고 한다면,
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"승인 허가",Toast.LENGTH_LONG).show(); //허가 되었다.
            }
            else{
                Toast.makeText(this,"아직 승인 안됐다",Toast.LENGTH_LONG).show(); //아직 허가 안됨
            }
            return;
        }
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
        editor.putInt("FLAG", switchFlag);
        editor.apply();
    }
    private void load(){
        switchState= appData.getBoolean("ISCHECKED",false);
        savedata = appData.getBoolean("SAVEDATA", false);
        radius = appData.getFloat("RADIUS", 0.2F);
        centerLat = appData.getFloat("LAT", 36.8851976F);
        centerLng = appData.getFloat("LNG", 126.7735638F);
        switchFlag = appData.getInt("FLAG", 0);
    }


    @Override
    public void onPause() {
        super.onPause();
        save();
    }
    @Override
    public void onStop() {
        super.onStop();
        save();
    }
}