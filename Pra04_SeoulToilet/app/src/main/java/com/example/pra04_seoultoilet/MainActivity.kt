package com.example.pra04_seoultoilet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // 권한 목록
    val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    // 승인 요청시 사용용
    val REQUEST_PERMISSION_CODE = 1
    // 기본 뱁 줌 레벨
    val DEFAULT_ZOOM_LEVEL = 17f

    // 서울 시청의 위치를 변수로 선언
    // LatLug 클래스는 위도와 경도를 가짐
    val CITY_HALL = LatLng(37.5662952, 126.97794509999994)

    var googleMap : GoogleMap? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Map View에 onCreate함수 호출
        mapView.onCreate(savedInstanceState)

        // 서비스 권한 체크
        if(hasPermission()){
            initMap()
        } else {
            // 권한 요청하기
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSION_CODE)
        }

        myLocationButton.setOnClickListener{
            onMyLocationButtonClick()
        }

    }

    private fun onMyLocationButtonClick() {
        when {
            hasPermission() -> googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(getMyLocation(), DEFAULT_ZOOM_LEVEL))
            else -> Toast.makeText(applicationContext, "위치 사용권한 설정에 동의해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    // 맵 초기화
    @SuppressLint("MissingPermission")
    private fun initMap() {
        mapView.getMapAsync{
            // 구글맵 객체 저장
            googleMap = it
            // 현재위치로 이동 버튼 비활성화
            it.uiSettings.isMyLocationButtonEnabled = false
            // 위치 사용 권한이 있으면
            when{
                hasPermission() -> {
                    // 현재 위치 표시
                    it.isMyLocationEnabled = true
                    // 현재 위치로 카메라 이동
                    it.moveCamera(CameraUpdateFactory.newLatLngZoom(getMyLocation(), DEFAULT_ZOOM_LEVEL))
                }
                else -> {
                    // 권한이 없으므로 서울시청의 위치로 이동
                    it.moveCamera(CameraUpdateFactory.newLatLngZoom(CITY_HALL, DEFAULT_ZOOM_LEVEL))
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getMyLocation(): LatLng {
        // 위치를 측정하는 Provider를 GPS 센서로 지정
        val locationProvider: String = LocationManager.GPS_PROVIDER
        // 위치 서비스 객체 불러오기
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // 마지막으로 업데이트된 위치 가져오기
        val lastKnownLocation : Location = locationManager.getLastKnownLocation(locationProvider)!!
        // 위도 경도 객체로 반환
        return LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)
    }

    private fun hasPermission(): Boolean {
        for(permission in PERMISSIONS){
            if(ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false
            }
        }
        return true
    }

    //region 맵뷰의 라이플 사이클 함수 호출을 위한 코드
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
    //endregion
}