package com.example.pra02_quizlocker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class ScreenOffReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when{
            // 화면이 꺼질 때 오는 브로드캐스트 메세지인 경우
            intent?.action == Intent.ACTION_SCREEN_OFF -> {
                Log.e("TEST", "화면이 꺼짐")
                // 화면이 꺼지면 QuizLockerActivity 실행
                val intent = Intent(context, QuizLockerActivity::class.java)
                // 새로운 Activity 실행
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                // 기존의 Activity 제거
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context?.startActivity(intent)
            }
        }
    }
}