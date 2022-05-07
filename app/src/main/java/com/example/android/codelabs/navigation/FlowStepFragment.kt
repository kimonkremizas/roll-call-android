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

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.ThemedSpinnerAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import appLogic.AppState
import helpers.HelperFunctions
import kotlinx.android.synthetic.main.flow_step_one_fragment.*
import kotlinx.android.synthetic.main.navigation_activity.*
import kotlinx.android.synthetic.main.overview_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import models.Lesson
import services.LessonService
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * Presents how multiple steps flow could be implemented.
 */
class FlowStepFragment : Fragment() {

    private var currentLesson:Lesson = Lesson()
    val lessonService: LessonService = LessonService()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        // TODO STEP 8 - Use type-safe arguments - remove previous line!
        val safeArgs: FlowStepFragmentArgs by navArgs()
        val flowStepNumber = safeArgs.flowStepNumber
        // TODO END STEP 8

        return when (flowStepNumber) {
            2 -> inflater.inflate(R.layout.flow_step_two_fragment, container, false)
            else -> inflater.inflate(R.layout.flow_step_one_fragment, container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LessonService.onCurrentLessonChanged += { lesson -> FillLessonCard(lesson) }

        FillLessonCard(AppState.CurrentLesson)

        swipeRefresh?.setOnRefreshListener {
            RefreshLesson()
        }

        btnCheckIn?.setOnClickListener {
            CheckIn()
        }

         /*view.findViewById<View>(R.id.next_button).setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.next_action)
        )*/
    }

    private fun RefreshLesson() {
        lifecycleScope.launch(Dispatchers.IO) {
            val lessonService: LessonService = LessonService()
            lessonService.GetCurrentLesson(AppState.CurrentUser)
        }
    }

    private fun FillLessonCard(lesson: Lesson)
    {
        lifecycleScope.launch(Dispatchers.Main) {

            currentLesson = lesson

            if(currentLesson.Id == 0) {
                twLessonWhen?.text = "Your upcoming lesson"
                twLessonName?.text = "No upcoming lesson found."
                twLessonTime?.text = ""
                twRollCallStatus?.text = ""
                twLessonCampus?.text = ""
                twTeacherName?.text = ""
                btnCheckIn?.isVisible = false
                cardCheckIn?.isVisible = false
                return@launch
            }

            val int: Int = 45
            val duration: Duration = int.toDuration(DurationUnit.MINUTES)
            val instant1: Instant = lesson.StartTime.toInstant(TimeZone.UTC)
            val instant2: Instant = Clock.System.now()
            val instant3: Instant = instant1.plus(duration)
            val dateTime3: kotlinx.datetime.LocalDateTime = instant3.toLocalDateTime(TimeZone.UTC)

            if (instant1 < instant2) {
                twLessonWhen?.text = "Your current lesson"
            } else {
                twLessonWhen?.text = "Your upcoming lesson"
            }
            twLessonName?.text = lesson.SubjectName
            twLessonCampus?.text = lesson.CampusName

            twLessonTime?.text = "${lesson.StartTime.dayOfMonth}${HelperFunctions.GetDayEnding(lesson.StartTime.dayOfMonth)} " +
                    "${lesson.StartTime.month} ${lesson.StartTime.hour}:${lesson.StartTime.minute}" +
                    "- ${dateTime3.hour}:${dateTime3.minute}"
            //twLessonTime?.text = lesson.StartTime.toString() + " - " + instant1.plus(duration)
            twTeacherName?.text = lesson.TeacherName

            if (lesson.Code == null) {
                btnCheckIn?.isVisible = false
                cardCheckIn?.isVisible = false
                twRollCallStatus?.text = "Roll call has not started yet."
                twRollCallStatus.setTextColor(Color.parseColor("#2E2E2E"))
            } else {
                btnCheckIn?.isVisible = true
                cardCheckIn?.isVisible = true
                CheckInCounter()
                twRollCallStatus?.text = "Roll call has started."
                twRollCallStatus.setTextColor(Color.parseColor("#E43838"))
            }

            if(swipeRefresh?.isRefreshing == true) {
                swipeRefresh?.setRefreshing(false)
            }

            lifecycleScope.launch(Dispatchers.IO) {
                val result: Boolean = lessonService.CheckIfCheckedIn(AppState.CurrentUser, AppState.CurrentLesson)

                if(result == true) {
                    lifecycleScope.launch(Dispatchers.Main) {
                        btnCheckIn?.isVisible = false
                        cardCheckIn?.isVisible = false
                        twRollCallStatus?.text = "You have checked in."
                        twRollCallStatus.setTextColor(Color.parseColor("#2E2E2E"))
                    }
                }
            }
        }
    }

    private fun CheckIn() {
        var code: Int = 0

        if(editTextNumber?.text.toString() != "") {
            code = Integer.parseInt(editTextNumber?.text.toString())
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val result: String = lessonService.CheckIntoLesson(AppState.CurrentUser, AppState.CurrentLesson, code)

            when(result) {
                "Incorrect" -> { ShowToast("Incorrect code.") }
                "Success" -> {
                    ShowToast("You have successfully checked in.")
                    lessonService.GetCurrentLesson(AppState.CurrentUser)
                }
                "Fail" -> { ShowToast("Check in failed.")}
                else -> { ShowToast("Something went wrong.")}
            }
        }
    }

    private fun ShowToast(text: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            val toast = Toast.makeText(activity?.applicationContext, text, Toast.LENGTH_LONG)
            toast.show()
        }
    }

    private fun CheckInCounter() {
        lifecycleScope.launch(Dispatchers.IO) {
            var instant1: Instant
            var instant2: Instant
            var difference:Duration
            val int: Int = 10
            val duration: Duration = int.toDuration(DurationUnit.MINUTES)
            while (cardCheckIn?.isVisible == true) {
                instant1 = AppState.CurrentLesson.CodeTime?.toInstant(TimeZone.UTC)!!
                instant2 = Clock.System.now()
                difference = instant1.plus(duration) - instant2
                if(difference.inWholeSeconds > 0){
                    difference.toComponents { x, y, _ -> UpdateCounterText(x, y)}
                    delay(1000)
                }
                else {
                    CounterTimeOut()
                }
            }
        }
    }

    private fun UpdateCounterText(minutes: Long, seconds: Int) {
        lifecycleScope.launch(Dispatchers.Main) {
            twRollCallCountdown?.text = "${minutes}:${seconds}"
            btnCheckIn.isEnabled = true
        }
    }

    private fun CounterTimeOut() {
        lifecycleScope.launch(Dispatchers.Main) {
            twRollCallCountdown?.text = "Time out"
            btnCheckIn.isEnabled = false
        }
    }
}
