package com.example.pra06_sqlite

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
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
    var listener: (String, String, String) -> Unit = {title, post , time-> }

    // 수정인 경우 title, post를 세팅
    var title = ""
    var post = ""
    var time =""

    var sYear = 0
    var sMonth = 0
    var sDay = 0
    var sHour = 0
    var sMinute = 0

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

        datePicker.setOnDateChangedListener(object : DatePicker.OnDateChangedListener{
            override fun onDateChanged(
                view: DatePicker?,
                year: Int,
                monthOfYear: Int,
                dayOfMonth: Int
            ) {
                sYear = year
                sMonth = monthOfYear + 1
                sDay = dayOfMonth
            }

        })

        timePicker.setOnTimeChangedListener(object : TimePicker.OnTimeChangedListener{
            override fun onTimeChanged(view: TimePicker?, hourOfDay: Int, minute: Int) {
                sHour = hourOfDay
                sMinute = minute
            }

        })

        saveBtn.setOnClickListener {
            dismiss()
            val title = titleField.text.toString()
            val post = postField.text.toString()
            val time = "$sYear-$sMonth-$sDay $sHour:$sMinute"
            if(TextUtils.isEmpty(title) || TextUtils.isEmpty(post)) {
                Toast.makeText(context, "정보를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                // Unit 객체는 invoke 명령으로 호출 가능하다.
                listener.invoke(title, post, time)
            }
        }
        cancelBtn.setOnClickListener { dismiss() }
    }
}