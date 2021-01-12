package com.example.pra02_quizlocker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.preference.PreferenceManager
import android.util.Log

class BootCompleteReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // 부팅이 완료될 때의 메세지인지 확인
        when {
            intent?.action == Intent.ACTION_BOOT_COMPLETED -> {
                Log.e("quizlocker", "부팅이 완료됨")


                context?.let {
                    // 퀴즈 잠금화면이 on인가?
                    val pref = PreferenceManager.getDefaultSharedPreferences(context)
                    val useLockScreen = pref.getBoolean("useLockScreen", false)

                    if(useLockScreen){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                            it.startForegroundService(Intent(context, LockScreenService::class.java))
                        } else {
                            it.startService(Intent(context, LockScreenService::class.java))
                        }
                    }
                }
            }
        }
    }
}