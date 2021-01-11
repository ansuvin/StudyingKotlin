package com.example.pra02_quizlocker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class BootCompleteReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // 부팅이 완료될 때의 메세지인지 확인
        when {
            intent?.action == Intent.ACTION_BOOT_COMPLETED -> {
                Log.e("quizlocker", "부팅이 완료됨")
                Toast.makeText(context, "퀴즈 잠금화면: 부팅이 완료됨", Toast.LENGTH_SHORT).show()
            }
        }
    }
}