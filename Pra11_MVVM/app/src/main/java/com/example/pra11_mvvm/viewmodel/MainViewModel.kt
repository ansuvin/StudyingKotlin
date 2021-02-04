package com.example.pra11_mvvm.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pra11_mvvm.base.BaseViewModel
import com.example.pra11_mvvm.widget.SingleLiveEvent

class MainViewModel: BaseViewModel() {

    val TAG = "MainViewModel"

    val onSuccessEvent = SingleLiveEvent<Unit>()
    val onFailEvent = SingleLiveEvent<Unit>()
    val btn2Event = SingleLiveEvent<Unit>()

    val title = MutableLiveData<String>()
    val subtitle = MutableLiveData<String>()

    fun button1() {
        Log.e(TAG, "btton1")
        if (title.value != null && subtitle.value != null) {
            if (title.value!!.isNotEmpty() && subtitle.value!!.isNotEmpty()) {
                onSuccessEvent.call()
            } else onFailEvent.call()
        } else onFailEvent.call()
    }

    fun button2() {
        Log.e(TAG, "button2")
        btn2Event.call()
    }

}