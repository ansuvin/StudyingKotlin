package com.example.pra02_quizlocker

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream

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
                !isExternalStorageWritable() -> {
                    Toast.makeText(applicationContext, "외부 저장장치가 없습니다.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // 내부 저장소 사용
                    //saveToInnerStorage(text, filename)
                    // 외부 저장소 사용
                    savetoExternalStorage(text, filename)
                }
            }
        }

        loadBtn.setOnClickListener {
            try{
                // 내부 저장소 사용
                //textFeild.setText(loadFromInnerStorage(filename))
                // 외부 저장소 사용
                textFeild.setText(loadFromExteralStroage(filename))
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

    fun isExternalStorageWritable(): Boolean{
        when{
            // 외부저장장치를 사용할 수 있는지 체크하는 함수
            Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED -> return true
            else -> return false
        }
    }

    // 외부저장장치에서 앱 전용데이터로 사용할 파일 객체를 반환
    fun getAppDataFileFromExternalStorage(filename: String): File {
        // KITKAT 버전 부터는 앱 전용 디렉토리 상수 타입인 Environment.DIRECTORY_DOCUMENTS 지원
        val dir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        }else{
            // 하위 버전에서는 직접 디렉토리 이름을 입력
            File(Environment.getExternalStorageDirectory().absolutePath+"/Documents")
        }
        // 디렉토리의 경로중 없는 디렉토리가 있다면 생성한다. dir? "?" <- null일 수 있다. "!!" <- null이 아니다
        dir?.mkdirs()
        return  File("${dir?.absolutePath}${File.separator}${filename}")
    }

    // 외부저장소 앱 전용 디렉토리에 파일로 저장
    fun savetoExternalStorage(text: String, filename:String){
        val fileOutputStream = FileOutputStream(getAppDataFileFromExternalStorage(filename))

        // 파일 출력 스트림에 text를 바이트로 변환하여 write
        fileOutputStream.write(text.toByteArray())

        // 닫기
        fileOutputStream.close()
    }

    // 외부저장소 앱 전용 디렉토리에서 파일 데이터 불러오기
    fun loadFromExteralStroage(filename: String): String{
        // 파일에 저장되 내용을 읽어서 String 형태로 반환
        return FileInputStream(getAppDataFileFromExternalStorage(filename)).reader().readText()
    }
}