package com.example.rollcall.helpers

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import kotlinx.datetime.*
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

object HelperFunctions {
    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun GetDayEnding(day: Int): String {
        when(day) {
            1, 21, 31 -> return "st"
            2, 22 -> return "nd"
            3, 23 -> return "rd"
            else -> return "th"
        }
    }

    fun GetDatePlusMinutes(minutes: Int, original:LocalDateTime): LocalDateTime {
        val duration: Duration = minutes.toDuration(DurationUnit.MINUTES)
        val instant1: Instant = original.toInstant(TimeZone.currentSystemDefault())
        val instant2: Instant = instant1.plus(duration)
        return instant2.toLocalDateTime(TimeZone.currentSystemDefault())
    }

    fun GetMonthName(month: Int): String {
        when (month) {
            1 -> return "January"
            2 -> return "February"
            3 -> return "March"
            4 -> return "April"
            5 -> return "May"
            6 -> return "June"
            7 -> return "July"
            8 -> return "August"
            9 -> return "September"
            10 -> return "October"
            11 -> return "November"
            12 -> return "December"
            else -> return "Something went wrong"
        }
    }
}