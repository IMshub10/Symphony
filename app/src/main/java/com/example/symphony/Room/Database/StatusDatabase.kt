package com.example.symphony.Room.Database

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.symphony.Room.Dao.MyContactsDao
import com.example.symphony.Room.Dao.StatusDao
import com.example.symphony.Room.Model.Status
import com.example.symphony.Services.LocalUserService
import java.util.*


@Database(
    entities = [Status::class], version = 11,exportSchema = false
)
public abstract class StatusDatabase : RoomDatabase() {
    abstract fun statusDao(): StatusDao?

    companion object {
        private var instance: StatusDatabase? = null

        @Synchronized
        public fun getInstance(context: Context): StatusDatabase? {
            if (instance == null) {

                instance = Room.databaseBuilder(
                    context.applicationContext,
                    StatusDatabase::class.java, "status_database"
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

                PopulateDbTask(instance!!)
                    .execute()
            }
        }
    }

    private class PopulateDbTask(database: StatusDatabase) :
        AsyncTask<Void?, Void?, Void?>() {
        private val statusDao: StatusDao = database.statusDao()!!

        override fun doInBackground(vararg params: Void?): Void? {
            return null
        }
    }
}