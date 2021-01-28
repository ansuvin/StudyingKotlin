package com.example.pra07_calendar01

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_schedule.*
import kotlinx.android.synthetic.main.dialog_schedule.view.*

class ScheduleDialog(): DialogFragment() {
    var listener: (String, String) -> Unit = {date, content ->}

    val TAG = "ScheduleD"

    internal lateinit var preferences: SharedPreferences

    var sYear = 0
    var sMonth = 0
    var sDay = 0

    override fun onStart() {
        super.onStart()
        // 다이얼로그의 넓이와 높이 지정
        val width = resources.getDimensionPixelSize(R.dimen.schedule_dialog_width)
        val height = resources.getDimensionPixelSize(R.dimen.schedule_dialog_height)
        dialog?.window?.setLayout(width, height)
        //dialog?.window?.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tvScheduleDialog.text = "${sYear}년 ${sMonth}월 ${sDay}일"

        var date = "${sYear}-${sMonth}-${sDay}-"
        var time = ""
        timePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            time= "${hourOfDay}-${minute}"
            Log.e("TAG Hour", hourOfDay.toString())
            Log.e("TAG minute", minute.toString())
        }

        view.btnYes.setOnClickListener {
            if (view.editContent.text.toString().length == 0) {
                showToast("내용을 입력하세요")
            } else {
                date+= time
                preferences = requireActivity().getSharedPreferences("user", Activity.MODE_PRIVATE)

                val content = view.editContent.text.toString()
                Log.e(TAG, "$date, $content")
                listener.invoke(date, content)

                dismiss()
            }
        }
        view.btnNo.setOnClickListener {
            dismiss()
        }
    }

    fun showToast(str: String) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
    }

}