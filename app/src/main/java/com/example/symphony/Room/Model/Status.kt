package com.example.symphony.Room.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "status_table")
class Status(
    @field:PrimaryKey var key: String,
    val creator: String,
    val creatorPhone: String,
    val imageUrl: String,
    val text_message: String,
    val createDate:String,
    val seen: Boolean
)