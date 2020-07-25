package com.example.symphony.Room.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.symphony.Room.Model.MyContacts

@Dao
interface MyContactsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(myContacts: MyContacts?)

    @Update
    fun update(myContacts: MyContacts?)

    @Delete
    fun delete(myContacts: MyContacts?)

    @Query("Delete From my_contacts_table ")
    fun deleteAllMyContacts()

    @Query("SELECT * FROM my_contacts_table ")
    fun getAllMyContacts(): LiveData<List<MyContacts>>
}