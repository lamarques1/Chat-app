package com.lamarques.testecompose.presentation

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lamarques.testecompose.chat.models.ChatMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    private val _listMessages = MutableLiveData<List<ChatMessage>>(arrayListOf())

    fun handleListMessage(): LiveData<List<ChatMessage>> = _listMessages

    init {
        val messagesJson = sharedPreferences.getString(KEY_SHARED_PREFS, "[]")
        val gson = Gson()
        val type = object : TypeToken<List<ChatMessage>?>() {}.type
        val listMessage = gson.fromJson<List<ChatMessage>?>(messagesJson, type)

        _listMessages.value = listMessage ?: arrayListOf()
    }

    fun sendMessage(message: String) {
        if (message.trim().isEmpty()) return
        val chatMessage = ChatMessage(Date(), message, 0)
        _listMessages.value = _listMessages.value?.toMutableList()?.apply {
            add(chatMessage)
        }

        val gson = Gson()
        val messagesJson = gson.toJson(_listMessages.value ?: "[]")
        val editor = sharedPreferences.edit()
        editor.putString(KEY_SHARED_PREFS, messagesJson)
        editor.apply()
    }

    companion object {
        const val KEY_SHARED_PREFS = "Messages"
    }
}