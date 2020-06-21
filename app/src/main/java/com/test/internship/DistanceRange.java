package com.test.internship;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
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
import androidx.core.app.NotificationCompat;
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
    static LatLng nowLatLng;
    static LatLng centerLatLng;
    boolean okayFlag;
    boolean smsFlag;


    public int num = 0;
    private String CHANNEL_ID = "channel1";
    private String CHANNEL_NAME = "Channel1";

    NotificationManager manager;
    NotificationCompat.Builder builder;

    String person1_n, person1_p, person2_n, person2_p, person3_n, person3_p, person4_n, person4_p, person5_n, person5_p;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distance_range);

        radiusEdit=findViewById(R.id.radiusEdit);
        switchRadius=(Switch)findViewById(R.id.switchRadius);
        mapRadius = findViewById(R.id.mapRadius);
        layoutRadius = findViewById(R.id.layoutRadius);
        buttonOkay = findViewById(R.id.okay);
        buttonRegister=findViewById(R.id.buttonRegister);
        okayFlag = false;
        saveData = false;
        smsFlag = false;
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
                okayFlag=true;
                if ( Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( DistanceRange.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                            MY_PERMISSIONS_REQUEST_LOCATION );
                }
                else{
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 600000, 1, gpsLocationListener);
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 600000, 1, gpsLocationListener);
                }
            }
            else{
                smsFlag = false;
                okayFlag = false;
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
        else{
            if(!switchRadius.isChecked()){
                okayFlag = false;
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

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
        // okay 버튼 리스너 연결
        buttonOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(num==0) {
                    Toast.makeText(getApplicationContext(), "보호자 정보를 먼저 등록해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    okayFlag = true;
                    Toast.makeText(getApplicationContext(), "등록되었습니다.", Toast.LENGTH_SHORT).show();
                    onMapReady(naverMap);
                    okayFlag=false;
                    save();
                }
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
                    layoutRadius.setVisibility(View.VISIBLE);
                    mapRadius.setVisibility(View.VISIBLE);
                    buttonOkay.setVisibility(View.VISIBLE);
                    if ( Build.VERSION.SDK_INT >= 23 &&
                            ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions( DistanceRange.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                                MY_PERMISSIONS_REQUEST_LOCATION );
                    }
                    else{
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 600000, 1, gpsLocationListener);
                    }
                    save();
                }
                else{
                    okayFlag = false;
                    lm.removeUpdates(gpsLocationListener);
                    regCircle.setMap(null);
                    regMarker.setMap(null);
                    circle.setMap(null);
                    marker.setMap(null);
                    layoutRadius.setVisibility(View.INVISIBLE);
                    mapRadius.setVisibility(View.INVISIBLE);
                    buttonOkay.setVisibility(View.INVISIBLE);
                    save();
                }
            }
        });
    }

    //알림창
    public void showNoti(){
        builder = null;
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //버전 오레오 이상일 경우
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            manager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT));
            builder = new NotificationCompat.Builder(this,CHANNEL_ID);
        }
        //하위 버전일 경우
        else{
            builder = new NotificationCompat.Builder(this);
        }
        builder.setContentTitle("반경 벗어남 알림");
        builder.setContentText("설정된 반경을 벗어났습니다!! 보호자에게 알림을 전송합니다!");
        builder.setSmallIcon(R.mipmap.app_icon);
        Notification notification = builder.build();
        manager.notify(1,notification);
    }

    //문자전송
    public void sendSMS(String phoneNo, String name) {
        try {
            PendingIntent sentIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent("SMS_SENT_ACTION"), 0);
            PendingIntent deliveredIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent("SMS_DELIVERED_ACTION"), 0);

            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    switch(getResultCode()){
                        case Activity.RESULT_CANCELED:
                            // 작업이 취소됨
                            Toast.makeText(getApplicationContext(), "전송이 취소되었습니다", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            // 전송 실패
                            Toast.makeText(getApplicationContext(), "전송 실패 (RESULT_ERROR_GENERIC_FAILURE)", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            // 문자 지원 단말 아님
                            Toast.makeText(getApplicationContext(), "단말기가 문자 서비스를 지원하지 않습니다", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            // 무선 꺼짐
                            Toast.makeText(getApplicationContext(), "통신이 꺼져있습니다", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            // PDU 실패
                            Toast.makeText(getApplicationContext(), "PDU Null", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }, new IntentFilter("SMS_SENT_ACTION"));

            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            Toast.makeText(getApplicationContext(), "SMS 도착 완료", Toast.LENGTH_SHORT).show();
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(getApplicationContext(), "SMS 도착 실패", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }, new IntentFilter("SMS_DELIVERED_ACTION"));

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, name + "님 보호대상자님께서 설정한 이동 반경을 벗어나셨습니다 !!\n 현재 위도: " + nowLatLng.latitude + ", 경도: " + nowLatLng.longitude,
                    sentIntent, deliveredIntent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 현재위치가 바뀌면 호출되는 리스너너
    final LocationListener gpsLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            nowLatLng = new LatLng(latitude, longitude);
            centerLatLng = new LatLng(regMarker.getPosition().latitude, regMarker.getPosition().longitude);
            double dis = nowLatLng.distanceTo(centerLatLng); //m단위를 double로 반환
            if (dis > radius) {
                if (switchRadius.isChecked() == true && smsFlag == true) {
                    showNoti();
                    if(person1_n != null && person1_p != null){
                        sendSMS(person1_p, person1_n);
                    }
                    if(person2_n != null && person2_p != null){
                        sendSMS(person2_p, person2_n);
                    }
                    if(person3_n != null && person3_p != null){
                        sendSMS(person3_p, person3_n);
                    }
                    if(person4_n != null && person4_p != null){
                        sendSMS(person4_p, person4_n);
                    }
                    if(person5_n != null && person5_p != null){
                        sendSMS(person5_p, person5_n);
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
        circle.setMap(null);
        marker.setMap(null);
        if(switchRadius.isChecked()==true&&okayFlag==true){
            smsFlag = true;
            okayFlag = false;
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
        editor.putBoolean("OKAY", okayFlag);
        editor.putBoolean("SMS", smsFlag);
        editor.apply();
    }

    // 데이터 로딩
    private void load(){
        num = appData.getInt("NUM", 0);
        switchState= appData.getBoolean("ISCHECKED",false);
        saveData = appData.getBoolean("SAVEDATA", false);
        radius = appData.getFloat("RADIUS", 0.2F);
        centerLat = appData.getFloat("LAT", 0F);
        centerLng = appData.getFloat("LNG", 0F);
        okayFlag = appData.getBoolean("OKAY", false);
        smsFlag = appData.getBoolean("SMS", false);

        person1_n = appData.getString("PERSON1_NAME",null);
        person1_p = appData.getString("PERSON1_PHONE", null);
        person2_n = appData.getString("PERSON2_NAME", null);
        person2_p = appData.getString("PERSON2_PHONE", null);
        person3_n = appData.getString("PERSON3_NAME", null);
        person3_p = appData.getString("PERSON3_PHONE", null);
        person4_n = appData.getString("PERSON4_NAME", null);
        person4_p = appData.getString("PERSON4_PHONE", null);
        person5_n = appData.getString("PERSON5_NAME", null);
        person5_p = appData.getString("PERSON5_PHONE", null);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        load();
        if(saveData){
            switchRadius.setChecked(switchState);

            if(switchRadius.isChecked()==true){
                layoutRadius.setVisibility(View.VISIBLE);
                mapRadius.setVisibility(View.VISIBLE);
                buttonOkay.setVisibility(View.VISIBLE);
                okayFlag=true;
            }
            else{
                smsFlag = false;
                okayFlag = false;
                regCircle.setMap(null);
                regMarker.setMap(null);
                circle.setMap(null);
                marker.setMap(null);
                layoutRadius.setVisibility(View.INVISIBLE);
                mapRadius.setVisibility(View.INVISIBLE);
                buttonOkay.setVisibility(View.INVISIBLE);
            }
        }
        else{
            if(!switchRadius.isChecked()){
                smsFlag=false;
                okayFlag = false;
                layoutRadius.setVisibility(View.INVISIBLE);
                mapRadius.setVisibility(View.INVISIBLE);
                buttonOkay.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        load();
        if(saveData){
            switchRadius.setChecked(switchState);

            if(switchRadius.isChecked()==true){
                layoutRadius.setVisibility(View.VISIBLE);
                mapRadius.setVisibility(View.VISIBLE);
                buttonOkay.setVisibility(View.VISIBLE);
                okayFlag=true;
                smsFlag=true;
            }
            else{
                smsFlag = false;
                okayFlag = false;
                regCircle.setMap(null);
                regMarker.setMap(null);
                circle.setMap(null);
                marker.setMap(null);
                layoutRadius.setVisibility(View.INVISIBLE);
                mapRadius.setVisibility(View.INVISIBLE);
                buttonOkay.setVisibility(View.INVISIBLE);
            }
        }
        else{
            if(!switchRadius.isChecked()){
                okayFlag = false;
                layoutRadius.setVisibility(View.INVISIBLE);
                mapRadius.setVisibility(View.INVISIBLE);
                buttonOkay.setVisibility(View.INVISIBLE);
            }
        }
    }
}