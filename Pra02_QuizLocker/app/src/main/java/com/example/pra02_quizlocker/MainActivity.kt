package com.example.pra02_quizlocker

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.MultiSelectListPreference
import android.preference.PreferenceFragment
import android.preference.SwitchPreference

class MainActivity : AppCompatActivity() {

    val fragment = MyPreferenceFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentManager.beginTransaction().replace(R.id.preferenceContent, fragment).commit()
    }

    class MyPreferenceFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            // 환경설정 리소스 파일 적용
            addPreferencesFromResource(R.xml.pref)

            // 퀴즈 종류 요약정보에, 현재 선택된 항목을 보여주는 코드
            val categoryPref = findPreference("category") as MultiSelectListPreference
            categoryPref.summary = categoryPref.values.joinToString(", ")

            // 환경설정 정보값이 변경될 때에도 요약 정보를 변경하도록
            categoryPref.setOnPreferenceChangeListener { preference, newValue ->

                // newValue 파라미터가 hashSet으로 캐스팅이 실패하면
                val newValueSet = newValue as? HashSet<*> ?: return@setOnPreferenceChangeListener true

                // 선택된 퀴즈 종류로 요약정보 보여주기
                categoryPref.summary = newValue.joinToString(", ")

                true

            }

            // 잠금화면 사용 스위치 객체
            val useLockScreenPref = findPreference("useLockScreen") as SwitchPreference
            useLockScreenPref.setOnPreferenceClickListener {
                when {
                    // 퀴즈 잠금화면 사용이 체크된 경우 LockScreenService 실행
                    useLockScreenPref.isChecked -> {
                        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
                            activity.startForegroundService(Intent(activity, LockScreenService::class.java))
                        }else{
                            activity.startService(Intent(Intent(activity, LockScreenService::class.java)))
                        }
                    }
                    // 사용 해제되면 중지
                    else -> activity.stopService(Intent(activity, LockScreenService::class.java))
                }
                true
            }

            if (useLockScreenPref.isChecked){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    activity.startForegroundService(Intent(activity, LockScreenService::class.java))
                } else {
                    activity.startService(Intent(activity, LockScreenService::class.java))
                }
            }
        }
    }

}