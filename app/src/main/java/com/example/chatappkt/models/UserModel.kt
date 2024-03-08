package com.example.chatappkt.models

data class UserModel(
    var name: String,
    var email: String,
    var uid: String
){
    constructor(): this("","","")
}
