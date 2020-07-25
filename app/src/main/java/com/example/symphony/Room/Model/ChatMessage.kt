package com.example.symphony.Room.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_chat_message_table")
class ChatMessage(
    @field:PrimaryKey var key: String,
    val SenderKey:String,
    val Sender: String,
    val RecieverKey: String,
    val Reciever: String,
    val phone: String,
    val createDate: String,
    val messagetext: String,
    val timestamp: String,
    val viewType: Int
)