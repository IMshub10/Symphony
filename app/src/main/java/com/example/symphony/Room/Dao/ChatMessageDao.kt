package com.example.symphony.Room.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.symphony.Room.Model.ChatMessage

@Dao
interface ChatMessageDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(chatMessage: ChatMessage?)

    @Update
    fun update(chatMessage:ChatMessage?)

    @Delete
    fun delete(chatMessage: ChatMessage?)

    @Query("Delete From my_chat_message_table ")
    fun deleteAllMyChatMessages()

    @Query("SELECT * FROM my_chat_message_table WHERE ((RecieverKey=:receiverKey AND SenderKey =:senderKey)OR (RecieverKey=:senderKey AND SenderKey=:receiverKey )) ORDER BY timestamp")
    fun getAllMyChatMessages(receiverKey:String,senderKey:String): LiveData<List<ChatMessage>>
}