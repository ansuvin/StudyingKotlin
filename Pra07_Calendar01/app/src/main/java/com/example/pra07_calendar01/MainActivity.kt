package com.example.pra07_calendar01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.applikeysolutions.cosmocalendar.selection.OnDaySelectedListener
import com.applikeysolutions.cosmocalendar.selection.RangeSelectionManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val CAL_TAG = "CALENDAR"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendar_view.isShowDaysOfWeek = false
        calendar_view.selectionManager = RangeSelectionManager(OnDaySelectedListener {
            Log.e(CAL_TAG, "selected Dates: ${calendar_view.selectedDates.size}")
            if (calendar_view.selectedDates.size <= 0) return@OnDaySelectedListener
            Log.e(CAL_TAG, "SelectedDays: ${calendar_view.selectedDays}")
        })
    }
}