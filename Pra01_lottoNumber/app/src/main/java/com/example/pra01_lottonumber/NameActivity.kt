package com.example.pra01_lottonumber

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class NameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name)

        val goResultButton = findViewById<Button>(R.id.goButton)
        val backButton = findViewById<Button>(R.id.backButton)

        goResultButton.setOnClickListener {
            startActivity(Intent(this, ResultActivity::class.java))
        }

        backButton.setOnClickListener {
            finish()
        }
    }
}