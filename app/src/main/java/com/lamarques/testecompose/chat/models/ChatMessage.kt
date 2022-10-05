package com.lamarques.testecompose.chat.models

import android.os.Message
import java.util.*

data class ChatMessage(
    val date: Date,
    val message: String,
    val origin: Int, //0 = Sent - 1 = Received


)