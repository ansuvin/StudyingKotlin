package com.example.pra11_mvvm.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.pra11_mvvm.viewmodel.MainViewModel

class MainActivity:AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ViewModel 인스턴스 생성
        viewModel = ViewModelProvider(this,
        ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
    }
}
