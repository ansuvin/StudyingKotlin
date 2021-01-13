package com.example.pra03_punchpower

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    val power by lazy {
        intent.getDoubleExtra("power", 0.0) * 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // action bar Title
        title = "펀치력 결과"

        val len = String.format("%.0f", power *1.5).length
        scoreLabel.text = String.format("%.0f", power*1.5).substring(0,len-3)+ " 점"

        restartButton.setOnClickListener {
            finish()
        }


    }
}