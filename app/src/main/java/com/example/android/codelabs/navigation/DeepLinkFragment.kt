/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.codelabs.navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import appLogic.AppState
import com.example.android.codelabs.navigation.databinding.DeeplinkFragmentBinding
import helpers.HelperFunctions
import kotlinx.android.synthetic.main.deeplink_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import models.Lesson
import services.LessonService
import java.time.YearMonth
import java.util.*
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * Fragment used to show how to deep link to a destination
 */
class DeepLinkFragment : Fragment() {
    private var _binding: DeeplinkFragmentBinding? = null
    private val binding get() = _binding!!

    private var selectedMonth: Int = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).monthNumber

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DeeplinkFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        twMonthName?.text = HelperFunctions.GetMonthName(selectedMonth) + " 2022"
        AssignDayNumbers(LocalDateTime(2022, selectedMonth,1, 12, 30, 0, 0))
        GetLessons(selectedMonth)

        minusMonth?.setOnClickListener {
            if(selectedMonth == 1)
                selectedMonth = 12
            else selectedMonth--
            twMonthName?.text = HelperFunctions.GetMonthName(selectedMonth) + " 2022"
            AssignDayNumbers(LocalDateTime(2022, selectedMonth,1, 12, 30, 0, 0))
            GetLessons(selectedMonth)
        }

        plusMonth?.setOnClickListener {
            if(selectedMonth == 12)
                selectedMonth = 1
            else selectedMonth++
            twMonthName?.text = HelperFunctions.GetMonthName(selectedMonth) + " 2022"
            AssignDayNumbers(LocalDateTime(2022, selectedMonth,1, 12, 30, 0, 0))
            GetLessons(selectedMonth)
        }
    }

    private fun GetLessons(month: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            val service: LessonService = LessonService()
            val lessons: List<Lesson> = service.GetLessonsByMonth(AppState.CurrentUser, selectedMonth)

            if(lessons.isEmpty()) {
                return@launch
            }

            lifecycleScope.launch(Dispatchers.Main) {
                AssignDayNumbersAndLessons(LocalDateTime(lessons[0].StartTime.year, lessons[0].StartTime.month,
                    1, 1, 0, 0, 0), lessons.sortedBy { x -> x.StartTime })
            }
        }
    }

    private fun AssignDayNumbers(firstDate: LocalDateTime) {
        FlushCalendar()
        val yearMonthObject: YearMonth = YearMonth.of(firstDate.year, firstDate.month)
        val daysInMonth: Int = yearMonthObject.lengthOfMonth()

        var i:Int = 1
        var k:Int = 1
        if(firstDate.dayOfWeek.value != 6 && firstDate.dayOfWeek.value != 7 ){
            k = firstDate.dayOfWeek.value
        }
        while(i <= daysInMonth) {
            val day:Int = LocalDateTime(firstDate.year, firstDate.month,i, 1, 0, 0, 0).dayOfWeek.value
            if(day != 6 && day != 7) {
                GetCalendarBlock(k).text = i.toString()
                i++
                k++
            } else {
                i++
            }
        }
    }

    private fun AssignDayNumbersAndLessons(firstDate: LocalDateTime, lessons:List<Lesson>) {
        FlushCalendar()
        val yearMonthObject: YearMonth = YearMonth.of(firstDate.year, firstDate.month)
        val daysInMonth: Int = yearMonthObject.lengthOfMonth()

        var i:Int = 1
        var k:Int = 1
        while(i <= daysInMonth) {
            val day:Int = LocalDateTime(firstDate.year, firstDate.month,i, 1, 0, 0, 0).dayOfWeek.value
            if(day != 6 && day != 7) {
                GetCalendarBlock(k).text = i.toString()
                var lastDay: Int = 0
                var beginning: String = ""
                for(l in lessons.filter { x -> x.StartTime.dayOfMonth == i }){
                    if(l.StartTime.dayOfMonth == i)
                    {
                        if(lastDay != l.StartTime.dayOfMonth) {
                            GetCalendarBlock(k).text = GetCalendarBlock(k).text.toString() + "\n${l.StartTime.hour}:${l.StartTime.minute}"
                            beginning = GetCalendarBlock(k).text.toString()
                            lastDay = l.StartTime.dayOfMonth
                        }
                        val endTime = HelperFunctions.GetDatePlusMinutes(45, l.StartTime)
                        if(lastDay != l.StartTime.dayOfMonth) {
                            GetCalendarBlock(k).text =  GetCalendarBlock(k).text.toString() + "\n-\n${endTime.hour}:${endTime.minute}"
                        }
                        else {
                            GetCalendarBlock(k).text = beginning + "\n-\n${endTime.hour}:${endTime.minute}"
                        }
                    }
                }
                i++
                k++
            } else {
                i++
            }
        }
    }

    private fun FlushCalendar() {
        var i:Int = 0
        while (i < 26) {
            GetCalendarBlock(i).text = ""
            i++
        }
    }

    private fun GetCalendarBlock(id: Int): TextView {
        when(id) {
            1 -> return day1
            2 -> return day2
            3 -> return day3
            4 -> return day4
            5 -> return day5
            6 -> return day6
            7 -> return day7
            8 -> return day8
            9 -> return day9
            10 -> return day10
            11 -> return day11
            12 -> return day12
            13 -> return day13
            14 -> return day14
            15 -> return day15
            16 -> return day16
            17 -> return day17
            18 -> return day18
            19 -> return day19
            20 -> return day20
            21 -> return day21
            22 -> return day22
            23 -> return day23
            24 -> return day24
            25 -> return day25
            else -> return day1
        }
    }
}
