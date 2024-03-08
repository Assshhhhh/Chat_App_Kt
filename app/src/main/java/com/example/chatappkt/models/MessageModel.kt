package com.example.chatappkt.models

data class MessageModel(
    var message: String,
    var senderId: String
){
    constructor(): this("","")
}
