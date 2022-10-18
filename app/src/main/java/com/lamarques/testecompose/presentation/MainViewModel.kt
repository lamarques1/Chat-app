package com.lamarques.testecompose.presentation

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.lamarques.testecompose.chat.models.ChatMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    private val _listMessages = MutableLiveData<List<ChatMessage>>(arrayListOf())

    fun handleListMessage(): LiveData<List<ChatMessage>> = _listMessages

    init {
        clearMessages()
    }

    fun sendMessage(message: String) {
        if (message.trim().isEmpty()) return
        if (message.trim() == "clear") {
            clearMessages()
            return
        }

        addMessageToList(message)
        saveMessageOnSharedPrefs()
    }

    private fun saveMessageOnSharedPrefs() {
        val gson = Gson()
        val messagesJson = gson.toJson(_listMessages.value ?: "[]")
        val editor = sharedPreferences.edit()
        editor.putString(KEY_SHARED_PREFS, messagesJson)
        editor.apply()
    }

    private fun addMessageToList(message: String) {
        val time = SimpleDateFormat("hh:mm").format(Date())
        val chatMessage = ChatMessage(time, message, 0)
        _listMessages.value = _listMessages.value?.toMutableList()?.apply {
            add(chatMessage)
        }
    }

    private fun clearMessages() {
        val gson = Gson()
        val messagesJson = gson.toJson("[]")
        val editor = sharedPreferences.edit()
        editor.putString(KEY_SHARED_PREFS, messagesJson)
        editor.apply()
    }

    companion object {
        const val KEY_SHARED_PREFS = "Messages"
    }
}