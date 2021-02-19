package com.example.pra12_fingerprint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.pra12_fingerprint.databinding.ActivityMainBinding
import me.aflak.libraries.FingerprintCallback
import me.aflak.libraries.FingerprintDialog

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.activity = this

    }

    fun onFingerClick() {
        Log.e("click", "onFingerClick()")
        FingerprintDialog.initialize(this)
                .title("지문인증")
                .message("지문으로 인증합니다.")
                .callback(object : FingerprintCallback{
                    override fun onAuthenticationSuccess() {
                        Toast.makeText(applicationContext, "인증 성공했습니당", Toast.LENGTH_SHORT).show()
                    }

                    override fun onAuthenticationCancel() {
                        Toast.makeText(applicationContext, "인증 실패했습니당", Toast.LENGTH_SHORT).show()
                    }

                })
                .show()
    }
}