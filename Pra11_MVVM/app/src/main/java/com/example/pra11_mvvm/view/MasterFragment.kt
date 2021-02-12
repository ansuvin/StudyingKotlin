package com.example.pra11_mvvm.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pra11_mvvm.viewmodel.SharedViewModel

class MasterFragment: Fragment() {
    private lateinit var viewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
                    .get(SharedViewModel::class.java)
        }
    }
}