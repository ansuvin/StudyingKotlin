package com.example.pra10_databinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pra10_databinding.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // 라이브 데이터 선언
    val liveText = MutableLiveData<String>()
    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.apply {
            lifecycleOwner = this@MainActivity // binding에 LifeCycleOwner을 지정해줘야 LiveData가 실시간으로 변화
            activity = this@MainActivity

            btnChange.setOnClickListener{
                liveText.value = "Hello LiveData! ${++count}"
            }
        }
        liveText.value="Hello DataBinding"
    }

}