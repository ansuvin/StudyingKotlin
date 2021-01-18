package com.example.pra06_sqlite

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_write.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.hours

class WriteDialog : DialogFragment() {
    // 다이얼로그의 버튼이 늘린경우
    var listener: (String, String, String, Int) -> Unit = {title, post , time, isSoon-> }

    // 수정인 경우 title, post를 세팅
    var title = ""
    var post = ""
    var time =""
    var isSoon = ""

    var sYear = ""
    var sMonth = ""
    var sDay = ""
    var sHour = ""
    var sMinute = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_write, container, false)
    }

    override fun onStart() {
        super.onStart()

        // 다이얼로그의 넓이와 높이 지정
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        titleField.setText(title)
        postField.setText(post)

        datePicker.setOnDateChangedListener(object : DatePicker.OnDateChangedListener {
            override fun onDateChanged(
                view: DatePicker?,
                year: Int,
                monthOfYear: Int,
                dayOfMonth: Int
            ) {
                sYear = year.toString()
                sMonth = if (monthOfYear + 1 >= 10) {
                    (monthOfYear+1).toString()
                } else {
                    "0"+(monthOfYear+1).toString()
                }
                sDay = if (dayOfMonth >=10) {
                    dayOfMonth.toString()
                } else {
                    "0$dayOfMonth"
                }
            }

        })

        timePicker.setOnTimeChangedListener(object : TimePicker.OnTimeChangedListener{
            override fun onTimeChanged(view: TimePicker?, hourOfDay: Int, minute: Int) {
                sHour = if (hourOfDay >=10) {
                    hourOfDay.toString()
                } else {
                    "0$hourOfDay"
                }
                sMinute = if (minute >= 10) {
                    minute.toString()
                } else if (minute == 0){
                    "00"
                } else {
                    "0$minute"
                }
            }

        })

        saveBtn.setOnClickListener {
            dismiss()
            val title = titleField.text.toString()
            val post = postField.text.toString()
            val time = "$sYear-$sMonth-$sDay $sHour:$sMinute"

            val now = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("yyyyMMddhhmm")
            val aaFormat = SimpleDateFormat("aa")
            var nowTime = dateFormat.format(now)
            val notAA = aaFormat.format(now)
            nowTime = if (notAA == "오후") {
                (nowTime.toLong()+1200).toString()
            } else {
                nowTime
            }
            val isSoon = if ("$sYear$sMonth$sDay$sHour$sMinute".toLong()-nowTime.toLong() <=60L){
                1
            } else {
                0
            }

            Log.e("60L", 60L.toString())
            Log.e("isSoon?", ("$sYear$sMonth$sDay$sHour$sMinute".toLong()-nowTime.toLong()).toString())
            Log.e("selecTime", "$sYear$sMonth$sDay$sHour$sMinute".toLong().toString())
            Log.e("curTime", nowTime.toLong().toString())
            Log.e("saveBtn Dialog", "$title, $post, $time, $isSoon")

            if(TextUtils.isEmpty(title) || TextUtils.isEmpty(post)) {
                Toast.makeText(context, "정보를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                // Unit 객체는 invoke 명령으로 호출 가능하다.
                listener.invoke(title, post, time, isSoon)
            }
        }
        cancelBtn.setOnClickListener { dismiss() }
    }
}