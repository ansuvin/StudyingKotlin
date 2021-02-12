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
        binding.viewModel = this

        // viewModel 가져오기
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(CounterViewModel::class.java)

        viewModel.counter.observe(this, Observer<Int>() {
            binding.counterTV.text = "${it} "
        })
    }

    fun onAddButtonClick(view: View) {
        viewModel.increase()
    }

    fun onSubButtonClick(view: View) {
        viewModel.decrease()
    }

}
