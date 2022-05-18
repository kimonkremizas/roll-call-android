package com.example.rollcall.models

import kotlinx.datetime.LocalDateTime


class Lesson {
    var Id:Int = 0
    var SubjectName: String = ""
    var StartTime: LocalDateTime = LocalDateTime(2022, 1, 1, 0, 0, 0)
    var Code: Int? = null
    var CodeTime: LocalDateTime? = null
    var CampusName: String = ""
    var TeacherName: String = ""

    constructor(id:Int, subjectName: String, startTime: LocalDateTime, code: Int?, codeTime: LocalDateTime?,
        campusName: String?, teacherName: String?)
    {
        Id = id
        SubjectName = subjectName
        StartTime = startTime
        Code = code
        CodeTime = codeTime
        if(campusName == null)
            CampusName = ""
        else CampusName = campusName
        if(teacherName == null)
            TeacherName = ""
        else TeacherName = teacherName
    }

    constructor()

    constructor(id:Int, subjectName: String, code: Int?, campusName: String, teacherName: String)
}