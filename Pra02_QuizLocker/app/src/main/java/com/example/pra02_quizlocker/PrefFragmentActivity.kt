package com.example.pra02_quizlocker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceFragment

class PrefFragmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pref_fragment)

        // Activity의 컨텐트 뷰를 MyPreFragment로 교체
        fragmentManager.beginTransaction().replace(android.R.id.content, MyPreFragment()).commit()
    }

    // PreferenceFragment: XML로 작성한 Preference를 UI로 보여주는 클래스
    class MyPreFragment : PreferenceFragment(){
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            // Preference 정보가 있는 xml 파일 지정
            addPreferencesFromResource(R.xml.ex_pref)
        }
    }
}