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
            intent.putIntegerArrayListExtra("result", ArrayList(LottoNumberMaker.getShuffleLottoNumbers()))

            startActivity(intent)
        }

        constellationCard.setOnClickListener {
            startActivity(Intent(this, ConstellationActivity::class.java))
        }

        nameCard.setOnClickListener {
            startActivity(Intent(this, NameActivity::class.java))
        }

    }
}