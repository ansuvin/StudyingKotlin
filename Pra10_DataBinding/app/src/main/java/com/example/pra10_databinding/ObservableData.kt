package com.example.pra10_databinding

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class ObservableData: BaseObservable() {
    @get:Bindable
    var site : String = ""
    set(value) {
        field = value
        notifyPropertyChanged(BR.site)
    }
}
