package com.example.pra11_mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CounterViewModel:ViewModel() {
    val counter = MutableLiveData<Int>().apply {value = 0}

    fun increase() {
        counter.value = counter.value!! + 1
    }

    fun decrease() {
        counter.value = counter.value!! - 1
    }
}