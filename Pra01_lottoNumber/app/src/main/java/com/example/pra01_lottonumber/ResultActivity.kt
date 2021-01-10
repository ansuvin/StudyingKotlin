package com.example.pra01_lottonumber

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class ResultActivity : AppCompatActivity() {

    val lottoImageStartId = R.drawable.ball_01

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val resultLabel = findViewById<TextView>(R.id.resultLabel)

        // intent로부터 전달받은 결과 배열 가져오기
        val result = intent.getIntegerArrayListExtra("result")
        val name = intent.getStringExtra("name")

        resultLabel.text = "랜덤으로 생성된\n로또번호입니다."

        // 만약 name이 전달되었다면
        if(!TextUtils.isEmpty(name)){
            resultLabel.text = "${name}님의\n${SimpleDateFormat("yyyy년 MM월 dd일").format(Date())}\n로또번호입니다."
        }

        // 전달받은 결과가 있는 경우에만 실행
        result?.let {
            // 전달 받은 결과를 정력하여 전달한다. (sorteby)
            updateLottoBallImage(result = result.sortedBy { it })
        }
    }

    fun updateLottoBallImage(result: List<Int>) {   // List<Int>를 인수로 한다.
        // 결과 사이즈가 6개 미만일 경우 에러 발생 위험으로 바로 return
        if(result.size < 6) return

        val imageview01 = findViewById<ImageView>(R.id.imageView01)
        val imageview02 = findViewById<ImageView>(R.id.imageView02)
        val imageview03 = findViewById<ImageView>(R.id.imageView03)
        val imageview04 = findViewById<ImageView>(R.id.imageView04)
        val imageview05 = findViewById<ImageView>(R.id.imageView05)
        val imageview06 = findViewById<ImageView>(R.id.imageView06)

        // ball_01 아이디에 결과값 -1 을 하면 목표하는 이미지 됨
        imageview01.setImageResource(lottoImageStartId + (result[0]-1))
        imageview02.setImageResource(lottoImageStartId + (result[1]-1))
        imageview03.setImageResource(lottoImageStartId + (result[2]-1))
        imageview04.setImageResource(lottoImageStartId + (result[3]-1))
        imageview05.setImageResource(lottoImageStartId + (result[4]-1))
        imageview06.setImageResource(lottoImageStartId + (result[5]-1))

    }
}