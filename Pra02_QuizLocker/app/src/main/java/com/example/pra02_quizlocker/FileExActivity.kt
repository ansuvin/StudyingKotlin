package com.example.pra02_quizlocker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.io.FileNotFoundException

class FileExActivity : AppCompatActivity() {

    val filename = "data.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_ex)

        val saveBtn = findViewById<Button>(R.id.saveBtn)
        val loadBtn = findViewById<Button>(R.id.loadBtn)
        val textFeild = findViewById<EditText>(R.id.textField)

        saveBtn.setOnClickListener {
            val text = textFeild.text.toString()
            when{
                TextUtils.isEmpty(text) -> {
                    Toast.makeText(applicationContext, "텍스트가 비어있습니다.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    saveToInnerStorage(text, filename)
                }
            }
        }

        loadBtn.setOnClickListener {
            try{
                textFeild.setText(loadFromInnerStorage(filename))
            } catch (e: FileNotFoundException){     // 파일이 없는 경우 에러메시지를 보여준다.
                Toast.makeText(applicationContext, "저장된 텍스트가 없습니다.", Toast.LENGTH_SHORT).show()
            }

        }
    }

    // 내부저장소 파일트 텍스트를 저장
    fun saveToInnerStorage(text: String, filename: String){
        // 파일 출력 스트림 가져오기
        val fileOutputStream = openFileOutput(filename, Context.MODE_PRIVATE)

        // 파일 출력 스트림에 text를 바이트로 변환하여 write
        fileOutputStream.write(text.toByteArray())

        // 닫기
        fileOutputStream.close()
    }

    // 내부저장소 파일 텍스트 불러오기
    fun loadFromInnerStorage(filename: String): String{
        val fileInputStream = openFileInput(filename)

        // 파일에 저장되 내용을 읽어서 String 형태로 반환
        return fileInputStream.reader().readText()
    }
}