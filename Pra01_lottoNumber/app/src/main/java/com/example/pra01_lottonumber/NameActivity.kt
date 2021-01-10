package com.example.pra01_lottonumber

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name)

        val goResultButton = findViewById<Button>(R.id.goButton)
        val backButton = findViewById<Button>(R.id.backButton)
        val editText = findViewById<EditText>(R.id.editText)

        goResultButton.setOnClickListener {
            val intent = Intent(this, ResultActivity::class.java)
            intent.putIntegerArrayListExtra("result", ArrayList(LottoNumberMaker.getLottoNumbersFromHash(editText.text.toString())))
            intent.putExtra("name", editText.text.toString())       // 이름 전달
            startActivity(intent)
        }

        backButton.setOnClickListener {
            finish()
        }
    }
}