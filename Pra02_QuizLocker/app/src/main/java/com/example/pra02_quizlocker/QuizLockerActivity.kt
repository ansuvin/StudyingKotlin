package com.example.pra02_quizlocker

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_quiz_locker.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class QuizLockerActivity : AppCompatActivity() {

    var quiz: JSONObject? = null

    val wrongAnswerPref by lazy { getSharedPreferences("wrongAnswer", Context.MODE_PRIVATE) }
    val correctAnswerPref by lazy { getSharedPreferences("correctAnswer", Context.MODE_PRIVATE) }

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
        quizLabel.text = quiz?.getString("question")
        choice1.text = quiz?.getString("choice1")
        choice2.text = quiz?.getString("choice2")

        // 정/오답 횟수 보이기
        val id = quiz?.getInt("id").toString() ?:""
        correctCountLabel.text = "정답횟수: "+correctAnswerPref.getInt(id, 0)
        wrongCountLabel.text = "오답횟수: "+ wrongAnswerPref.getInt(id,0)

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
        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        val leftImg = findViewById<ImageView>(R.id.leftImageView)
        val rightImg = findViewById<ImageView>(R.id.rightImageView)
        quiz?.let {     // null이 아니면
            when {
                // 답이 같으면
                choie == it.getString("answer") -> {
                    val id = it.getInt("id").toString()
                    var count = correctAnswerPref.getInt(id, 0)
                    count++
                    correctAnswerPref.edit().putInt(id, count).apply()
                    correctCountLabel.text ="정답횟수: "+count

                    finish()
                }
                else -> {
                    // 틀리면
                    val id = it.getInt("id").toString()
                    var count = wrongAnswerPref.getInt(id , 0)
                    count++
                    wrongAnswerPref.edit().putInt(id, count).apply()
                    wrongCountLabel.text= "오답횟수: "+count

                    // UI 초기화
                    leftImg.setImageResource(R.drawable.padlock)
                    rightImg.setImageResource(R.drawable.padlock)
                    seekBar?.progress = 50

                    // 진동울리기
                    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    // SDK 버전에 따라 다름
                    if (Build.VERSION.SDK_INT >= 26) {
                        // 1초 동안 100의 세기 (최고 255) 로 1회 진동
                        vibrator.vibrate(VibrationEffect.createOneShot(1000,100))
                    } else {
                        vibrator.vibrate(1000)
                    }
                }
            }
        }
    }
}