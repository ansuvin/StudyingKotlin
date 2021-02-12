package com.example.pra11_mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    val count = MutableLiveData<Int>() .apply { value = 0 }
}