package com.example.symphony.Room.Repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.symphony.Room.Dao.MyContactsDao
import com.example.symphony.Room.Database.MyContactsDatabase
import com.example.symphony.Room.Database.MyContactsDatabase.Companion.getInstance
import com.example.symphony.Room.Model.MyContacts

class MyContactsRepository(application: Application?) {
    private val myContactsDao: MyContactsDao

    init {
        val myContactsDatabase =
            getInstance(application!!)
        myContactsDao = myContactsDatabase!!.myContactsDao()!!
    }

    suspend fun insert(myContacts: MyContacts){
        myContactsDao.insert(myContacts)
    }
    suspend fun update(myContacts: MyContacts){
        myContactsDao.update(myContacts)
    }
    suspend fun updateContact(askey: String?,
                              lastMess: String?,
                              createDate: String?,
                              timestam: String?){
        myContactsDao.updateContact(askey,lastMess,createDate,timestam)
    }
    suspend fun updateMember(askey: String?,
                              lastMess: String?,
                             messageCount: Int,
                              createDate: String?,
                              timestam: String?){
        myContactsDao.updateMember(askey,lastMess, messageCount,createDate,timestam)
    }
    suspend fun updateMemberMessage(askey: String?,
                             messageCount: Int){
        myContactsDao.updateMemberMessage(askey, messageCount)
    }
    suspend fun delete(myContacts: MyContacts){
        myContactsDao.delete(myContacts)
    }
    suspend fun deleteAll(){
        myContactsDao.deleteAllMyContacts()
    }
     fun getAllMyContacts():LiveData<List<MyContacts>>{
        return myContactsDao.getAllMyContacts()
    }
    fun getContactsForChatFragment(check:String):LiveData<List<MyContacts>>{
        return myContactsDao.getContactsForChatFragment(check)
    }
}