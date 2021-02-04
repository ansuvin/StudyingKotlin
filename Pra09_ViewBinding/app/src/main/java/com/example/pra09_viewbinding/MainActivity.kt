package com.example.pra09_viewbinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pra09_viewbinding.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        binding.title.text = "Hello"
        binding.subtitle.text = "Concise, safe code"
        binding.button.setOnClickListener {
            binding.title.text = "버튼"
            binding.subtitle.text = "눌림"
        }

        setContentView(binding.root)
    }
}