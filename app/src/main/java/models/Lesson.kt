package models

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant


class Lesson {
    var Id:Int = 0
    var SubjectName: String = ""
    var StartTime: Instant = Clock.System.now()
    var Code: Int? = null
    var CodeTime: Instant? = null
    var CampusName: String = ""
    var TeacherName: String = ""

    constructor(id:Int, subjectName: String, startTime: Instant, code: Int?, codeTime: Instant?,
        campusName: String, teacherName: String)

    constructor()
}