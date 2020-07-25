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
    suspend fun delete(myContacts: MyContacts){
        myContactsDao.delete(myContacts)
    }
    suspend fun deleteAll(){
        myContactsDao.deleteAllMyContacts()
    }
     fun getAllMyContacts():LiveData<List<MyContacts>>{
        return myContactsDao.getAllMyContacts()
    }
}