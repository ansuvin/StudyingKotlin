package com.example.pra11_mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    val name = MutableLiveData<String>()
    val email = MutableLiveData<String>()

    fun onModify() {

    }
}