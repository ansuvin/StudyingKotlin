package com.example.pra01_lottonumber

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val randomCard = findViewById<CardView>(R.id.randomCard)
        val constellationCard = findViewById<CardView>(R.id.constellationCard)
        val nameCard = findViewById<CardView>(R.id.nameCard)

        randomCard.setOnClickListener {
            startActivity(Intent(this, ResultActivity::class.java))
        }

        constellationCard.setOnClickListener {
            startActivity(Intent(this, ConstellationActivity::class.java))
        }

        nameCard.setOnClickListener {
            startActivity(Intent(this, NameActivity::class.java))
        }

    }
}