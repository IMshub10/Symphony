package com.example.symphony.Room.Database

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.symphony.Room.Dao.MyContactsDao
import com.example.symphony.Room.Model.MyContacts

@Database(
    entities = [MyContacts::class],
    version = 2,
    exportSchema = false
)
public  abstract class MyContactsDatabase : RoomDatabase() {
    abstract fun myContactsDao(): MyContactsDao?

    companion object {
        private var instance: MyContactsDatabase? = null
        @Synchronized
        public fun getInstance(context: Context): MyContactsDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyContactsDatabase::class.java, "my_contacts_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()
            }
            return instance
        }

        private val roomCallback: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbTask(instance!!)
                    .execute()
            }
        }
    }
    private class PopulateDbTask(database: MyContactsDatabase) :
        AsyncTask<Void?, Void?, Void?>() {
        private val myContactsDao: MyContactsDao
        init {
            myContactsDao = database.myContactsDao()!!
        }

        override fun doInBackground(vararg params: Void?): Void? {
            return null
        }
    }
}