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

    @Query("SELECT * FROM my_contacts_table WHERE lastMessage!=:check ")
    fun getContactsForChatFragment(check:String) :LiveData<List<MyContacts>>

    @Query("UPDATE my_contacts_table SET lastMessage=:lastMess,createDate=:createDate,messsageCount=messsageCount+1,timestamp=:timestam WHERE `key`=:askey ")
    fun updateContact(
        askey: String?,
        lastMess: String?,
        createDate: String?,
        timestam: String?
    )

    @Query("UPDATE my_contacts_table SET messsageCount=:messageCount WHERE `key`=:askey")
    fun updateMemberMessage(askey: String?, messageCount: Int)

    @Query("UPDATE my_contacts_table SET lastMessage=:lastMess,createDate=:createDate,messsageCount=:messageCount,timestamp=:timestam WHERE `key`=:askey")
    fun updateMember(
        askey: String?,
        lastMess: String?,
        messageCount: Int,
        createDate: String?,
        timestam: String?
    )
}