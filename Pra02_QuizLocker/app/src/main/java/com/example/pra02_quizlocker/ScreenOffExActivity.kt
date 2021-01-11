package com.example.pra02_quizlocker

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ScreenOffExActivity : AppCompatActivity() {

    var screenOffReceiver: ScreenOffReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_off_ex)

        // screenOffReceiver가 null 인 겅우에만 생성하고 등록
        if(screenOffReceiver == null){
            screenOffReceiver = ScreenOffReceiver()
            val intentFilter = IntentFilter(Intent.ACTION_SCREEN_OFF)
            // 브로드캐스트 리시버를 런타임에 등록
            registerReceiver(screenOffReceiver, intentFilter)
        }
    }
}