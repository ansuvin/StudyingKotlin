package com.example.pra04_seoultoilet

import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

// ClusterItem 을 구현하는 클래스
// 생성자에게 전달받은 데이터를 반환할수 있게 구현
// 멤버 프로퍼티는 인터페이스 Getter와 이름이 다르게 지정

class MyItem (val _position : LatLng, val _title: String, val _snippet: String, val _icon: BitmapDescriptor)
    : ClusterItem{
    override fun getPosition(): LatLng {
        return _position
    }

    override fun getTitle(): String {
        return _title
    }

    override fun getSnippet(): String {
        return _snippet
    }

    fun getIcon(): BitmapDescriptor {
        return _icon
    }

    // 검색에서 아이템을 찾기 위해, GPS에 위도, 경도, 제목, 설명이 모두 같으면 같은 객체로 취급하기
    override fun equals(other: Any?): Boolean {
        if(other is MyItem){
            return (other.position.latitude == position.latitude
                    && other.position.longitude == position.longitude
                    && other.title == _title
                    && other.snippet == _snippet)
        }
        return false
    }

    // equals()를 오버라이트 한 경우 반드시 필요함
    // -> 같은 객체는 같은 해시코드를 반환해야하기 때문
    override fun hashCode(): Int {
        var hash = _position.latitude.hashCode() * 31
        hash = hash * 31 + _position.longitude.hashCode()
        hash = hash * 31 + title.hashCode()
        hash = hash * 31 + snippet.hashCode()
        return hash
    }
}