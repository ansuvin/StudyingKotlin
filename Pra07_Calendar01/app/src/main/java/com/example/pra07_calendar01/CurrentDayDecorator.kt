package com.example.pra07_calendar01

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class CurrentDayDecorator(context: Context, currentDay: CalendarDay): DayViewDecorator {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private val drawable: Drawable? = context?.getDrawable(R.drawable.ic_launcher_foreground)
    private var myDay = currentDay

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day == myDay
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun decorate(view: DayViewFacade?) {
        view!!.setBackgroundDrawable(drawable!!)
        view.setDaysDisabled(true)
    }
}