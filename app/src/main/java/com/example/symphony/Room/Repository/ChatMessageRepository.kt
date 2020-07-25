package com.example.symphony.Room.Repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.symphony.Room.Dao.ChatMessageDao
import com.example.symphony.Room.Database.ChatMessageDatabase
import com.example.symphony.Room.Model.ChatMessage
import com.example.symphony.Room.Model.MyContacts

class ChatMessageRepository(application: Application) {
    private val chatMessageDao: ChatMessageDao

    init {
        val chatMessageDatabase =
            ChatMessageDatabase.getInstance(application!!)
        chatMessageDao = chatMessageDatabase!!.chatMessageDao()!!
    }

    suspend fun insert(chatMessage: ChatMessage){
        chatMessageDao.insert(chatMessage)
    }
    suspend fun update(chatMessage: ChatMessage){
        chatMessageDao.update(chatMessage)
    }
    suspend fun delete(chatMessage: ChatMessage){
        chatMessageDao.delete(chatMessage)
    }
    suspend fun deleteAll(){
        chatMessageDao.deleteAllMyChatMessages()
    }
    fun getAllMyChatmessages(): LiveData<List<ChatMessage>> {
        return chatMessageDao.getAllMyChatMessages()
    }
}