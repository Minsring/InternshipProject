package com.test.internship

import android.content.Intent
import android.os.Bundle
import androidx.annotation.UiThread
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.UiSettings
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import ted.gun0912.clustering.clustering.Cluster

import java.util.ArrayList
import java.util.Locale

import ted.gun0912.clustering.naver.TedNaverClustering

class CustomMarkerClusterClass : FragmentActivity(), OnMapReadyCallback {
    private var locationSource: FusedLocationSource? = null
    private var naverMap: NaverMap? = null
    internal var hospitals: ArrayList<HospitalInformation>? = null
    internal var latlng: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.subject_list_map)
        val getIntent = intent
        hospitals = intent.getSerializableExtra("열린병원리스트") as ArrayList<HospitalInformation>
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
        val fm = supportFragmentManager

        latlng = hospitals!![0].latLng

        // 초기 위치 및 맵 타입 설정 // 신평면사무소 근처
        val options = NaverMapOptions()
                .camera(CameraPosition(latlng!!, 15.0))
                .mapType(NaverMap.MapType.Basic)
                .locationButtonEnabled(true)
                .compassEnabled(true)
                .stopGesturesEnabled(false)
                .tiltGesturesEnabled(false)
        var mapFragment = fm.findFragmentById(R.id.map) as MapFragment?

        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance(options)
            fm.beginTransaction().add(R.id.map, mapFragment).commit()
        }

        mapFragment!!.getMapAsync(this)

        // 사용자 위치 받아오기
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    // 사용자 위치 받아오기
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (locationSource!!.onRequestPermissionsResult(
                        requestCode, permissions, grantResults)) {
            if (!locationSource!!.isActivated) {
                naverMap!!.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults)
    }

    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        // 지도 타입 설정
        naverMap.mapType = NaverMap.MapType.Basic
        naverMap.locale = Locale("ko")
        // 지도에 표시할 부가적 정보
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, true)
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, true)
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_MOUNTAIN, true)

        // 건물명 같은 심볼의 크기 조절 (0~2)
        naverMap.symbolScale = 1.5f


        // 카메라 위치를 한반도 인근으로 제한
        naverMap.extent = LatLngBounds(LatLng(31.43, 122.37), LatLng(44.35, 132.0))
        naverMap.minZoom = 6.0
        naverMap.maxZoom = 18.0

        // LocationButton을 누르면 사용자의 현위치로 이동
        naverMap.locationSource = locationSource


        // 컨트롤 설정
        val uiSettings = naverMap.uiSettings

        uiSettings.isCompassEnabled = true
        uiSettings.isLocationButtonEnabled = true

        TedNaverClustering.with<HospitalInformation>(this, naverMap)
                .items(hospitals!!)
                .make()
    }

    companion object {
        private val LOCATION_PERMISSION_REQUEST_CODE = 1000
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