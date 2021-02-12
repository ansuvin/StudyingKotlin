package com.example.pra11_mvvm.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.pra11_mvvm.R
import com.example.pra11_mvvm.databinding.ActivityMainBinding
import com.example.pra11_mvvm.viewmodel.CounterViewModel
import com.example.pra11_mvvm.viewmodel.MainViewModel

class MainActivity:AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: CounterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this   // 이 객체가 이 액티비티의 라이프사이클을 참조하면서 데이터가 변경되거나 하면 refresh 하겠다

        // viewModel 가져오기
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(CounterViewModel::class.java)
        binding.viewModel = viewModel
    }

}
