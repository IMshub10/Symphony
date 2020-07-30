package com.example.symphony.Room.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.symphony.Room.Model.Status

@Dao
interface StatusDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(status: Status)

    @Update
    fun update(status: Status)

    @Query("UPDATE status_table SET seen=:seen WHERE `key`=:key")
    fun updateSeen(key:String,seen:Boolean)

    @Delete
    fun delete(status: Status)

    @Query("Delete From status_table ")
    fun deleteAllStatus()

    @Query("Delete From status_table WHERE `key`=:key")
    fun deleteByKey(key:String)

    @Query("SELECT * FROM status_table WHERE `key`!=:key AND seen=:seen ORDER BY createDate")
    fun getAllStatus(key:String,seen:Boolean):LiveData<List<Status>>

    @Query("SELECT * FROM status_table WHERE `key`=:key")
    fun getMyStatus(key: String):LiveData<Status>

}