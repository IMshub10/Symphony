package com.example.symphony.Room.Database

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.symphony.Room.Dao.ChatMessageDao
import com.example.symphony.Room.Model.ChatMessage

@Database(
    entities = [ChatMessage::class],
    version = 1,
    exportSchema = false
)
public  abstract class ChatMessageDatabase : RoomDatabase() {
    abstract fun chatMessageDao(): ChatMessageDao?

    companion object {
        private var instance: ChatMessageDatabase? = null
        @Synchronized
        public fun getInstance(context: Context): ChatMessageDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChatMessageDatabase::class.java, "my_chat_message_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()
            }
            return instance
        }

        private val roomCallback: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
            }
        }
    }
    private class PopulateDbTask private constructor(database: ChatMessageDatabase) :
        AsyncTask<Void?, Void?, Void?>() {
        private val chatMessageDao: ChatMessageDao
        init {
            chatMessageDao = database.chatMessageDao()!!
        }

        override fun doInBackground(vararg params: Void?): Void? {
            return null

        }
    }
}