package com.example.pra10_databinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.pra10_databinding.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var text = "Hello!!!"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // binding 세팅
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        // 현재 binding시킨 xml의 variable name
        binding.main = this
    }

    fun btnClick (view: View) {
        Log.e("TAG", "눌림")
        text = "Hello Binding"
        binding.invalidateAll()
    }
}