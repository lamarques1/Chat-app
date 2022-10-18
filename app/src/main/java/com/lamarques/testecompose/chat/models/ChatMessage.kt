package com.lamarques.testecompose.chat.models

data class ChatMessage(
    val time: String,
    val message: String,
    val origin: Int, //0 = Sent - 1 = Received
)