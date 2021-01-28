package com.example.pra07_calendar01

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.Toast
import com.applandeo.materialcalendarview.EventDay
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import com.prolificinteractive.materialcalendarview.CalendarDay
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

    // region CalendarView3
    /*override fun onCreate(savedInstanceState: Bundle?) {
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
    }*/
    // endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = DateFormat.format("MMM", Date(System.currentTimeMillis()))

        // 시작 요일 바꾸기
        compactcalendar_view.setFirstDayOfWeek(Calendar.SUNDAY)

        // String 타입 날짜를 long 타입으로 바꾸기
        var dateStr = "2021-01-03"
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        var date = sdf.parse(dateStr)
        var longDate = date.time

        // 초록색 동그라미 추가
        val ev1 = Event(Color.GREEN, longDate)
        compactcalendar_view.addEvent(ev1)

        // 파랑 동그라미 추가
        val ev2 = Event(Color.BLUE, longDate)
        compactcalendar_view.addEvent(ev2)

        // 동그라미 여러개 추가 (3개까지 가능 그 이상은 ●●+ 로 표시
        dateStr = "2021-01-05"
        date = sdf.parse(dateStr)
        longDate = date.time
        for (i: Int in 1..5) {
            // Event(색, 날짜, Data = 나중에 getEvent로 확인 가능)
            val ev3 = Event(R.color.purple_500, longDate, "이것은 이벤트다 $i")
            compactcalendar_view.addEvent(ev3)
        }

        // 지정된 날짜 getEvent 해오기 (List<Event> 형태)
        val events = compactcalendar_view.getEvents(longDate)
        Log.e(CAL_TAG, "Events: ${events}")

        compactcalendar_view.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date?) {
                val events = compactcalendar_view.getEvents(dateClicked)
                if (events.size != 0) {
                    // dateClicked = 클릭된 날짜
                    // events[position].data = 클릭된 날짜에 position번째 data보기
                    Log.e(CAL_TAG, "DAY was clicked: ${dateClicked} with events ${events}")

                    for (i: Int in 0..events.size - 1) {
                        Log.e(CAL_TAG, "events-data: ${events[i].data}")
                    }
                } else {
                    showToast("적용된 이벤트가 없습니다.")
                }
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                Log.e(CAL_TAG, "Month was scrolled to: ${firstDayOfNewMonth }")
                Log.e(CAL_TAG, "Month E: ${DateFormat.format("MMM", firstDayOfNewMonth)}")
                Log.e(CAL_TAG, "Month N: ${DateFormat.format("MM", firstDayOfNewMonth)}")

                title = DateFormat.format("MMM", firstDayOfNewMonth)
            }

        })

    }

    fun showToast(str: String) {
        Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()
    }

}