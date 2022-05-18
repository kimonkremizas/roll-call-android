package com.example.rollcall.models

class User {
    var Id : Int = 0
    var Email : String = ""
    var FirstName : String = ""
    var LastName : String = ""
    var Token : String = ""

    constructor(id: Int, email: String, firstName: String, lastName: String, token: String)
    {
        Id = id
        Email = email
        FirstName = firstName
        LastName = lastName
        Token = token
    }

    constructor()

    fun FullName(): String {
        return "$FirstName $LastName"
    }
}