package com.example.pra01_lottonumber

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val randomCard = findViewById<CardView>(R.id.randomCard)
        val constellationCard = findViewById<CardView>(R.id.constellationCard)
        val nameCard = findViewById<CardView>(R.id.nameCard)

        randomCard.setOnClickListener {
            val intent = Intent(this, ResultActivity::class.java)

            // intent의 결과 데이터를 전달 Int리스트를 전달 -> "putIntegerArrayListExtra"사용
            //intent.putIntegerArrayListExtra("result", ArrayList(getRandomLottoNumbeers()))
            intent.putIntegerArrayListExtra("result", ArrayList(getShuffleLottoNumbers()))

            startActivity(intent)
        }

        constellationCard.setOnClickListener {
            startActivity(Intent(this, ConstellationActivity::class.java))
        }

        nameCard.setOnClickListener {
            startActivity(Intent(this, NameActivity::class.java))
        }

    }

    // Random을 이용하여 램덤으로 1~45 번호중 하나의 번호를 생성하는 함수
    fun getRandomLottoNumber() : Int {     // Int 값을 반환
        return Random().nextInt(45)+1
    }

    fun getRandomLottoNumbeers() : MutableList<Int> {
        val lottoNumbers = mutableListOf<Int>()     // List 만들기

        for(i in 1..6) {    // 6번 반복
            // 램덤 번호 임시 저장 변수
            var number = 0
            do{
                number = getRandomLottoNumber()
            }while (lottoNumbers.contains(number))      // rottonNumbers에 number 변수의 값이 없을때까지 반복

            lottoNumbers.add(number)    // 중복 번호가 없으면 추가
        }

        return lottoNumbers
    }

    // Shuffle을 이용하여 번호를 섞은후 6개의 번호를 자르는 방법
    fun getShuffleLottoNumbers(): MutableList<Int> {
        val list = mutableListOf<Int>()

        for(number in 1..45){   // 1~45까지 돌면서 리스트에 로또 번호 저장
            list.add(number)
        }

        list.shuffle()  // 리스트 무작위로 섞기

        return list.subList(0,6)    // 리스트를 0~5 까지 잘라서 반환
    }
}