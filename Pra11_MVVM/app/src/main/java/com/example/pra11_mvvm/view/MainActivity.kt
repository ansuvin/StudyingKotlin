package com.example.pra11_mvvm.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pra11_mvvm.R
import com.example.pra11_mvvm.databinding.ActivityMainBinding
import com.example.pra11_mvvm.viewmodel.MainViewModel

class MainActivity:AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
                .get(MainViewModel::class.java)

        val nameObserver = Observer { newName: String? -> binding.tvName.text = newName }


    }

}
