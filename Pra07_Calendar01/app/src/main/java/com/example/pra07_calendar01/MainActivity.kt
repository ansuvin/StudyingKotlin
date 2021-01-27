package com.example.pra07_calendar01

import android.app.Activity
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.applandeo.materialcalendarview.EventDay
import com.prolificinteractive.materialcalendarview.CalendarDay
import jp.co.recruit_mp.android.lightcalendarview.WeekDay
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    val CAL_TAG = "CALENDAR"

    val calList = ArrayList<CalendarDay> ()

    val events = ArrayList<EventDay>()
    var selectedDates : List<Calendar>? = arrayListOf()

    // region CalendarView1
    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (i: Int in 1..5) {
            calList.add(CalendarDay(2021,i,i+20))
        }
        for (calDay in calList) {
            calendarView.addDecorators(CurrentDayDecorator(applicationContext, calDay))
        }

        calendarView.setOnDateChangedListener { widget, date, selected ->
            showToast("${date.year}년 ${date.month}월 ${date.day}일  DATE=${date}")
            Log.e(CAL_TAG, "${date.year}년 ${date.month}월 ${date.day}일  DATE=${date}")
            textView.text = "${date.year}년 ${date.month}월 ${date.day}일  DATE=${date}"

        }


    }

    fun convertDateToTimestamp(_year: Int, _month: Int, _day: Int): Long {
        val month = _month.toString().convertSingleToDoubleDigit().toInt()
        val day = _day.toString().convertSingleToDoubleDigit().toInt()
        val date = "$_year-$month-$day"
        return date.convertDateToTimestamp()
    }

    fun String.convertSingleToDoubleDigit(): String = if (this.length < 2) "0$this" else this

    fun String.convertDateToTimestamp(): Long =
            SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).parse(this).time
*/
    //endregion

    // region CalendarView2
    /*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendarView.setWeekDayFilterColor(WeekDay.SUNDAY, Color.RED)
        calendarView.setDayFilterColor(WeekDay.FRIDAY, Color.RED)

    }
     */
    // endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val calendar = Calendar.getInstance()

        events.add(EventDay(calendar, R.drawable.ic_arrow_left, Color.RED))

        calendarView.setEvents(events)

        selectedDates = calendarView.selectedDates
        Log.e(CAL_TAG, selectedDates.toString())
        calendar.set(2021,1,28)
        calendarView.setDate(calendar)

        calendarView.setOnDayClickListener {
            val clickedDayCalendar = it.calendar
            selectedDates = listOf(clickedDayCalendar)
            events.add(EventDay(clickedDayCalendar, R.drawable.ic_arrow_right))
            Log.e(CAL_TAG, clickedDayCalendar.toString())
            calendarView.setEvents(events)
        }
    }

    fun showToast(str: String) {
        Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()
    }

}