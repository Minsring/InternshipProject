package com.test.internship


import android.content.Intent
import android.graphics.Color
import android.graphics.PointF
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import ted.gun0912.clustering.naver.TedNaverClustering
import java.util.*

class CustomMarkerClusterClass : FragmentActivity(), OnMapReadyCallback {
    private var locationSource: FusedLocationSource? = null
    private var naverMap: NaverMap? = null
    internal var open_hospitals: ArrayList<HospitalInformation>? = null
    internal var closed_hospitals: ArrayList<HospitalInformation>? = null
    internal var subject: String? = null
    internal var latlng: LatLng? = null
    internal var simple: ConstraintLayout? = null
    internal var mapTitle: TextView? = null
    internal var simple_name: TextView? = null
    internal var simple_add: TextView? = null
    internal var simple_dis: TextView? = null
    internal var simple_call: Button? = null
    internal var simple_info: Button? = null
    internal var sendHospital: HospitalInformation? = null
    internal var myBuckets: IntArray = intArrayOf(10, 20, 50, 100, 200, 500, 1000)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.subject_list_map)

        mapTitle = findViewById(R.id.mapTitle)
        simple = findViewById(R.id.simple)
        simple_name = findViewById(R.id.simple_name)
        simple_add = findViewById(R.id.simple_add)
        simple_dis = findViewById(R.id.simple_dis)
        simple_call = findViewById(R.id.simple_call)
        simple_info = findViewById(R.id.simple_info)

        simple?.setVisibility(View.INVISIBLE)
        simple_call?.setEnabled(false)
        simple_info?.setEnabled(false)

        val getIntent = intent
        open_hospitals = intent.getSerializableExtra("열린병원리스트") as ArrayList<HospitalInformation>
        closed_hospitals = intent.getSerializableExtra("닫은병원리스트") as ArrayList<HospitalInformation>
        subject = intent.getStringExtra("진료과")
        mapTitle?.setText("진료중인 "+subject)
        
        val fm = supportFragmentManager

//        if(hospitals==null){
//            latlng = LatLng(31.43, 122.37)
//        }
//        else{
//            latlng = hospitals!![0].latLng
//        }
        latlng = open_hospitals!![0].latLng
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

        simple_info?.setOnClickListener(listener)
        simple_call?.setOnClickListener(listener)
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

        // 지도 클릭시 simple창 숨기기
        naverMap.setOnMapClickListener{ pointF: PointF, latLng: LatLng ->
            simple?.setVisibility(View.INVISIBLE)
            simple_info?.setEnabled(false)
            simple_call?.setEnabled(false)
            naverMap.setContentPadding(0, 0, 0, 0)
        }
        naverMap.setOnMapLongClickListener{ pointF: PointF, latLng: LatLng ->
            simple?.setVisibility(View.INVISIBLE)
            simple_info?.setEnabled(false)
            simple_call?.setEnabled(false)
            naverMap.setContentPadding(0, 0, 0, 0)
        }

        TedNaverClustering.with<HospitalInformation>(this, naverMap)
                .items(open_hospitals!!)
                .customMarker { clusterItem: HospitalInformation ->
                    Marker(clusterItem.getLatLng()).apply {
                        icon = OverlayImage.fromResource(R.drawable.custom_icon_open)
                        width = 120
                        height = 150;
                        zIndex = 100;
                        captionText=clusterItem.getHospitalName();
                        isHideCollidedSymbols = true                //심벌이랑 겹치는 부분 숨길까 말까
                    }
                }
                .markerClickListener { hospital: HospitalInformation ->
                    val position = hospital.getLatLng()
                    simple_name?.setText(hospital.getHospitalName())
                    simple_add?.setText(hospital.getAddress())
                    simple_dis?.setText(hospital.getDistance())
                    simple?.setBackgroundResource(R.color.strawberryMilk)
                    simple?.setVisibility(View.VISIBLE)
                    simple?.bringToFront()
                    simple_info?.setEnabled(true)
                    simple_call?.setEnabled(true)
                    sendHospital = hospital
                    naverMap.setContentPadding(0, 0, 0, 300)
                }
                .clusterBackground{Color.rgb(255,4,152)}
                .clusterClickListener { cluster ->
                    val position = cluster.position
                    Toast.makeText(this, "${cluster.size}개 클러스터", Toast.LENGTH_SHORT).show()
                }
                .minClusterSize(2)
                .clusterBuckets(myBuckets)  // 묶이는 단위 수정하고 싶으면 myBucket건들기
                .make()

        TedNaverClustering.with<HospitalInformation>(this, naverMap)
                .items(closed_hospitals!!)
                .customMarker { clusterItem: HospitalInformation ->
                    Marker(clusterItem.getLatLng()).apply {
                        icon = OverlayImage.fromResource(R.drawable.custom_icon_closed)
                        width = 120
                        height = 150;
                        zIndex = 1;
                        captionText=clusterItem.getHospitalName();
                        isHideCollidedSymbols = true                //심벌이랑 겹치는 부분 숨길까 말까
                    }
                }
                .markerClickListener { hospital: HospitalInformation ->
                    val position = hospital.getLatLng()
                    simple_name?.setText(hospital.getHospitalName())
                    simple_add?.setText(hospital.getAddress())
                    simple_dis?.setText(hospital.getDistance())
                    simple?.setBackgroundResource(R.color.minokGray)
                    simple?.setVisibility(View.VISIBLE)
                    simple?.bringToFront()
                    simple_info?.setEnabled(true)
                    simple_call?.setEnabled(true)
                    sendHospital = hospital
                    naverMap.setContentPadding(0, 0, 0, 300)
                }
                .clusterBackground { Color.LTGRAY }
                .clusterClickListener { cluster ->
                    val position = cluster.position
                    Toast.makeText(this, "${cluster.size}개 클러스터", Toast.LENGTH_SHORT).show()
                }
                .minClusterSize(2)
                .clusterBuckets(myBuckets)  // 묶이는 단위 수정하고 싶으면 myBucket건들기
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

    internal var listener: View.OnClickListener = View.OnClickListener { v ->
        var intent: Intent? = null
        when (v.id) {
            R.id.simple_info -> {
                intent = Intent(applicationContext, HospitalScreen::class.java)
                intent.putExtra("병원", sendHospital)
            }
            R.id.simple_call -> {
                val number = sendHospital?.getCallNumber()
                intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number")) //다이얼로 연결
            }
        }//                    startActivity(intent);
        if (intent != null) startActivity(intent)    // 다른 처리 없다면 여기서 한번에 화면 전환
    }
}