package com.example.symphony.Room.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_contacts_table")
class MyContacts(
    @field:PrimaryKey var key: String,
    val f_name: String,
    val l_name: String,
    val phone: String,
    val profileImage: String,
    val lastMessage: String,
    val createDate: String,
    val messsageCount: Int,
    val timestamp: String,
    val viewType: Int
)