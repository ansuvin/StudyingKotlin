package com.example.pra05_anonymoussns

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    // 파이어베이스의 test 키를 가진 데이터의 참조 객체를 가져온다.
    val ref = FirebaseDatabase.getInstance().getReference("test")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 값의 변경의 있는경우
        ref.addValueEventListener(object : ValueEventListener{
            // 데이터 변경이 감지된 경우
            override fun onDataChange(snapshot: DataSnapshot) {
                // test 키를 가진 데이터 스냅샷에서 값을 읽고 문자열로 변경
                val message = snapshot.value.toString()
                Log.d(TAG, message)
                supportActionBar?.title = message
            }

            // 데이터 읽기가 취소된 경우 (권한이 없는 경우)
            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
                Log.e(TAG,"err: "+ error.message)
            }

        })
    }
}