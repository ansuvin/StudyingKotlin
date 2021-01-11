package com.example.pra02_quizlocker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText

class PrefExActivity : AppCompatActivity() {

    // 데이터를 저장할 key
    val nameFieldKey = "nameField"
    val pushCheckBoxKey = "pushCheckBox"

    // Activity 초기화 이후에 사용해야 하기 때문에 lazy 위임을 사용
    val preference by lazy { getSharedPreferences("PrefExActivity", Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pref_ex)

        val saveBtn = findViewById<Button>(R.id.saveBtn)
        val loadBtn = findViewById<Button>(R.id.loadBtn)
        val nameField = findViewById<EditText>(R.id.nameField)
        val pushCheckbox = findViewById<CheckBox>(R.id.pushCheckBox)

        saveBtn.setOnClickListener {
            preference.edit().putString(nameFieldKey, nameField.text.toString()).apply()
            preference.edit().putBoolean(pushCheckBoxKey, pushCheckbox.isChecked).apply()
        }

        loadBtn.setOnClickListener {
            nameField.setText(preference.getString(nameFieldKey, ""))
            pushCheckbox.isChecked = preference.getBoolean(pushCheckBoxKey, false)
        }

    }
}