package com.example.splitwallet

class Users {
    var username: String? = ""
    var password: String? = ""
    var email: String? = ""

    constructor(){}
    constructor(username: String?, password:String?, email:String?){
        this.username = username
        this.password = password
        this.email = email
    }
}
