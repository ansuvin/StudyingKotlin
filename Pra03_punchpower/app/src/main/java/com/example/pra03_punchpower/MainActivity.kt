package com.example.pra03_punchpower

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    // 측정된 최대 펀치력
    var maxPower = 0.0      // float
    // 측정이 시작되었나?
    var isStart = false     // boolean
    // 측정이 시작된 시간
    var startTime  = 0L     // Long

    // Sensor 관리자 객체 , lazy로 늦은 초기화 (실제 사용할때 초기화)
    val sensorManager : SensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    // 센서 이벤트 처리
    val eventListener : SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                // 측정된 센서 값이 선형 가속도 타입이 아니면 바로 끝
                if (event.sensor.type != Sensor.TYPE_LINEAR_ACCELERATION) return@let

                // 좌표값을 제곱하여 음수를 없애고, 값의 차이를 극대화 시키기
                val power =
                    Math.pow(event.values[0].toDouble(), 2.0) + Math.pow(event.values[1].toDouble(), 2.0) + Math.pow(event.values[2].toDouble(), 2.0)

                // 측정된 펀치력이 20을 넘고 아직 측정이 시작되지 않은 경우
                if (power > 20 && !isStart){
                    // 측정시작
                    startTime = System.currentTimeMillis()
                    isStart = true
                }

                // 측정이 시작된 경우
                if(isStart) {
                    // 5초간 최대값 측정
                    if (maxPower < power) maxPower = power
                    stateLabel.text = "펀치력을 측정하고 있습니다."

                    // 측정 후 3초간 지나면 측정 끝
                    if (System.currentTimeMillis() - startTime > 3000){
                        isStart = false
                        punchPowerTestComplete(maxPower)
                    }
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            TODO("Not yet implemented")
        }

    }

    // 최초 생성될때 호출
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // Activity 사라졌다가 다시 보일때
    override fun onStart() {
        super.onStart()
        initGame()
    }

    fun initGame() {
        maxPower = 0.0
        isStart = false
        startTime = 0L
        stateLabel.text = "핸드폰을 손에쥐고 주먹을 내지르세요"

        // 센서의 변화 값을 처리할 리스너 등록
        // TYPE_LINEAR_ACCELERATION 은 중력값을 제외하고 x, y, z 축에 측정된 가속도만 계산되어 나옴
        sensorManager.registerListener(
            eventListener,
            sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    // 펀치력 측정이 완료된 경우
    fun punchPowerTestComplete(power: Double) {
        Log.d("MainActivity", "측정완료: power: "+ String.format("%.5f", power))
        sensorManager.unregisterListener(eventListener)
        val intent = Intent(this@MainActivity, ResultActivity::class.java)
        intent.putExtra("power", power)
        startActivity(intent)
    }

    // Activity가 화면에 사라지면
    override fun onStop() {
        super.onStop()
        try {
            sensorManager.unregisterListener(eventListener)
        }catch (e:Exception) {}
    }
}