package com.example.symphony.Room.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.symphony.Room.Model.ChatMessage
import com.example.symphony.Room.Repository.ChatMessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatMessageViewModel(application: Application) : AndroidViewModel(application) {
    var chaMessRepository: ChatMessageRepository?  = null


    init {
        chaMessRepository = ChatMessageRepository(application)
    }

    fun insert(chatMessage: ChatMessage) {
        CoroutineScope(Dispatchers.IO).launch {
            chaMessRepository!!.insert(chatMessage)
        }
    }

    fun update(chatMessage: ChatMessage) {
        CoroutineScope(Dispatchers.IO).launch {
            chaMessRepository!!.update(chatMessage)
        }
    }

    fun delete(chatMessage: ChatMessage) {
        CoroutineScope(Dispatchers.IO).launch {
            chaMessRepository!!.delete(chatMessage)
        }
    }

    fun deleteAlChatMessages() {
        CoroutineScope(Dispatchers.IO).launch {
            chaMessRepository!!.deleteAll()
        }
    }

    fun getAllChatMessages(): LiveData<List<ChatMessage>> {
        return chaMessRepository!!.getAllMyChatmessages()
    }
}