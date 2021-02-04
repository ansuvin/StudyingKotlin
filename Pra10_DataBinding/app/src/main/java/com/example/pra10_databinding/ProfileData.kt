package com.example.pra10_databinding

data class ProfileData(
    var profile: String,
    var name: String,
    var age: Int
) {
    override fun toString(): String {
        return "ProfileData(name='$name', age=$age)"
    }
}
