package com.example.pra02_quizlocker

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class QuizLockerActivity : AppCompatActivity() {

    var quiz: JSONObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_locker)

        val quizLabel = findViewById<TextView>(R.id.quizLabel)
        val choice1 = findViewById<TextView>(R.id.choice1)
        val choice2 = findViewById<TextView>(R.id.choice2)
        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        val leftImg = findViewById<ImageView>(R.id.leftImageView)
        val rightImg = findViewById<ImageView>(R.id.rightImageView)

        // 잠금화면보다 위에 위치하귀 위한 설정, 버전별로 달라서 버전별로 적용
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){
            // 잠금화면에서 보여지도록
            setShowWhenLocked(true)
            // 잠금 해제
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            // 잠금화면에서 보여지도록
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
            // 기본 잠금화면 해제
            window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        }
        // 화면을 꺼진 상태로 유지
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        //퀴즈 데이터 가져오기
        val json = assets.open("capital.json").reader().readText()
        val quizArray = JSONArray(json)

        // 퀴즈 고르기
        quiz = quizArray.getJSONObject(Random().nextInt(quizArray.length()))

        // 퀴즈 보이기
        Log.e("TEST",quiz?.get("question").toString())
        quizLabel.text = quiz?.getString("question")
        choice1.text = quiz?.getString("choice1")
        choice2.text = quiz?.getString("choice2")

        // SeekBar 값이 변경되면
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                when {
                    // 우측으로 가면
                    progress > 95 -> {
                        leftImg.setImageResource(R.drawable.padlock)
                        rightImg.setImageResource(R.drawable.unlock)
                    }
                    progress < 5 -> {
                        leftImg.setImageResource(R.drawable.unlock)
                        rightImg.setImageResource(R.drawable.padlock)
                    }
                    else -> {
                        leftImg.setImageResource(R.drawable.padlock)
                        rightImg.setImageResource(R.drawable.padlock)
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                
            }
            // 터치가 끝나면
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val progress = seekBar?.progress ?:50
                when {
                    progress > 95 -> checkChoice(quiz?.getString("choice2") ?: "")
                    progress < 5 -> checkChoice(quiz?.getString("choice1") ?: "")
                    else -> seekBar?.progress = 50
                }
            }

        })

    }

    fun checkChoice(choie: String){
        quiz?.let {     // null이 아니면
            when {
                // 답이 같으면
                choie == it.getString("answer") -> finish()
                else -> {
                    // 틀리면
                    val seekBar = findViewById<SeekBar>(R.id.seekBar)
                    val leftImg = findViewById<ImageView>(R.id.leftImageView)
                    val rightImg = findViewById<ImageView>(R.id.rightImageView)
                    leftImg.setImageResource(R.drawable.padlock)
                    rightImg.setImageResource(R.drawable.padlock)
                    seekBar?.progress = 50
                }
            }
        }
    }
}