package com.test.internship


import android.content.Intent
import android.graphics.Color
import android.graphics.PointF
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import ted.gun0912.clustering.naver.TedNaverClustering
import java.util.*

class SubjectListMap : AppCompatActivity(), OnMapReadyCallback {
    private var locationSource: FusedLocationSource? = null
    private var naverMap: NaverMap? = null
    internal var openHospitals: ArrayList<HospitalData>? = null
    internal var closedHospitals: ArrayList<HospitalData>? = null
    internal var subject: String? = null
    internal var latLng: LatLng? = null
    internal var mapTitle: TextView? = null
    internal var simpleLayout: ConstraintLayout? = null
    internal var simpleName: TextView? = null
    internal var simpleAdd: TextView? = null
    internal var simpleDistance: TextView? = null
    internal var simpleCall: Button? = null
    internal var simpleInfo: Button? = null
    internal var sendHospital: HospitalData? = null
    internal var myBuckets: IntArray = intArrayOf(10, 20, 50, 100, 200, 500, 1000)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.subject_list_map)

        // simple 창 설정
        mapTitle = findViewById(R.id.mapTitle)
        simpleLayout = findViewById(R.id.simple)
        simpleName = findViewById(R.id.simple_name)
        simpleAdd = findViewById(R.id.simple_add)
        simpleDistance = findViewById(R.id.simple_dis)
        simpleCall = findViewById(R.id.simple_call)
        simpleInfo = findViewById(R.id.simple_info)

        simpleLayout?.setVisibility(View.INVISIBLE)
        simpleCall?.setEnabled(false)
        simpleInfo?.setEnabled(false)

        // simple 리스너 설정
        simpleInfo?.setOnClickListener(listener)
        simpleCall?.setOnClickListener(listener)

        // 병원 리스트 받아오기
        if(intent.getSerializableExtra("열린병원리스트")!=null){
            openHospitals = intent.getSerializableExtra("열린병원리스트") as ArrayList<HospitalData>
        }
        if(intent.getSerializableExtra("닫은병원리스트")!=null){
            closedHospitals = intent.getSerializableExtra("닫은병원리스트") as ArrayList<HospitalData>
        }

        // 타이틀 설정
        subject = intent.getStringExtra("진료과")
        mapTitle?.setText(subject+" 지도")

        // 만약 모두 오픈 또는 클로즈라면 초기위치는 신평면사무소
        latLng = LatLng(36.47365999,128.50243783)
        if(openHospitals != null) {
            latLng = openHospitals!![0].latLng }
        else{
            latLng = closedHospitals!![0].latLng }

        // 지도 생성
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
                ?: MapFragment.newInstance().also {
                    fm.beginTransaction().add(R.id.map, it).commit()
                }
        mapFragment!!.getMapAsync(this)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

    }

    internal var listener: View.OnClickListener = View.OnClickListener { v ->
        var intent: Intent? = null
        when (v.id) {
            R.id.simple_info -> {
                intent = Intent(applicationContext, HospitalInformation::class.java)
                intent.putExtra("병원", sendHospital)
            }
            R.id.simple_call -> {
                val number = sendHospital?.getCallNumber()
                intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
            }
        }
        if (intent != null) startActivity(intent)
    }

    // 사용자 위치 받아오기
    companion object {
        private val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

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
        naverMap.maxZoom = 20.0

        // LocationButton을 누르면 사용자의 현위치로 이동
        naverMap.locationSource = locationSource

        // 초기 카메라 위치
        val cameraPosition = CameraPosition(latLng!!, 16.0)
        naverMap.cameraPosition = cameraPosition

        // 컨트롤 설정
        val uiSettings = naverMap.uiSettings

        uiSettings.isCompassEnabled = true
        uiSettings.isScaleBarEnabled = true
        uiSettings.isZoomControlEnabled = true
        uiSettings.isLocationButtonEnabled = true
        uiSettings.isTiltGesturesEnabled = false

        // 지도 클릭시 simple창 숨기기
        naverMap.setOnMapClickListener{ pointF: PointF, latLng: LatLng ->
            simpleLayout?.setVisibility(View.INVISIBLE)
            simpleInfo?.setEnabled(false)
            simpleCall?.setEnabled(false)
            naverMap.setContentPadding(0, 0, 0, 0)
        }
        naverMap.setOnMapLongClickListener{ pointF: PointF, latLng: LatLng ->
            simpleLayout?.setVisibility(View.INVISIBLE)
            simpleInfo?.setEnabled(false)
            simpleCall?.setEnabled(false)
            naverMap.setContentPadding(0, 0, 0, 0)
        }

        if(openHospitals != null) {
            TedNaverClustering.with<HospitalData>(this, naverMap)
                    .items(openHospitals!!)
                    .customMarker { clusterItem: HospitalData ->
                        Marker(clusterItem.getLatLng()).apply {
                            icon = OverlayImage.fromResource(R.drawable.custom_icon_open)
                            width = 120
                            height = 150;
                            zIndex = 100;
                            captionText=clusterItem.getHospitalName();
                            isHideCollidedSymbols = true
                        }
                    }
                    .markerClickListener { hospital: HospitalData ->
                        simpleName?.setText(hospital.getHospitalName())
                        simpleAdd?.setText(hospital.getAddress())
                        simpleDistance?.setText(hospital.getDistance())
                        simpleLayout?.setBackgroundResource(R.color.strawberryMilk)
                        simpleLayout?.setVisibility(View.VISIBLE)
                        simpleLayout?.bringToFront()
                        simpleInfo?.setEnabled(true)
                        simpleCall?.setEnabled(true)
                        sendHospital = hospital
                        naverMap.setContentPadding(0, 0, 0, 300)
                    }
                    .clusterBackground{Color.rgb(255,4,152)}
                    .minClusterSize(2)
                    .clusterBuckets(myBuckets)
                    .make()
        }

        if(closedHospitals != null) {
            TedNaverClustering.with<HospitalData>(this, naverMap)
                    .items(closedHospitals!!)
                    .customMarker { clusterItem: HospitalData ->
                        Marker(clusterItem.getLatLng()).apply {
                            icon = OverlayImage.fromResource(R.drawable.custom_icon_closed)
                            width = 120
                            height = 150;
                            zIndex = 1;
                            captionText=clusterItem.getHospitalName();
                            isHideCollidedSymbols = true
                        }
                    }
                    .markerClickListener { hospital: HospitalData ->
                        simpleName?.setText(hospital.getHospitalName())
                        simpleAdd?.setText(hospital.getAddress())
                        simpleDistance?.setText(hospital.getDistance())
                        simpleLayout?.setBackgroundResource(R.color.hospitalGray)
                        simpleLayout?.setVisibility(View.VISIBLE)
                        simpleLayout?.bringToFront()
                        simpleInfo?.setEnabled(true)
                        simpleCall?.setEnabled(true)
                        sendHospital = hospital
                        naverMap.setContentPadding(0, 0, 0, 300)
                    }
                    .clusterBackground { Color.LTGRAY }
                    .minClusterSize(2)
                    .clusterBuckets(myBuckets)
                    .make()
        }
    }


}