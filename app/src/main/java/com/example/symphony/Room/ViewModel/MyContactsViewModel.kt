package com.example.symphony.Room.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.symphony.Room.Model.MyContacts
import com.example.symphony.Room.Repository.MyContactsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MyContactsViewModel(application: Application) : AndroidViewModel(application) {
    var myContactsRepository: MyContactsRepository?  = null


    init {
        myContactsRepository = MyContactsRepository(application)
    }

    fun insert(myContacts: MyContacts) {
        CoroutineScope(IO).launch {
            myContactsRepository!!.insert(myContacts)
        }
    }

    fun update(myContacts: MyContacts) {
        CoroutineScope(IO).launch {
            myContactsRepository!!.update(myContacts)
        }
    }
    fun  updateContact(askey: String?,
                       lastMess: String?,
                       createDate: String?,
                       timestam: String?) {
        CoroutineScope(IO).launch {
            myContactsRepository!!.updateContact(askey,lastMess,createDate,timestam)
        }
    }
    fun   updateMember(askey: String?,
                       lastMess: String?,
                       messageCount: Int,
                       createDate: String?,
                       timestam: String?) {
        CoroutineScope(IO).launch {
            myContactsRepository!!.updateMember(askey,lastMess,messageCount,createDate,timestam)
        }
    }
    fun   updateMemberMessage(askey: String?,
                       messageCount: Int) {
        CoroutineScope(IO).launch {
            myContactsRepository!!.updateMemberMessage(askey,messageCount)
        }
    }
    fun delete(myContacts: MyContacts) {
        CoroutineScope(IO).launch {
            myContactsRepository!!.delete(myContacts)
        }
    }

    fun deleteAllMyContacts() {
        CoroutineScope(IO).launch {
            myContactsRepository!!.deleteAll()
        }
    }

    fun getAllMyContacts(): LiveData<List<MyContacts>> {
        return myContactsRepository!!.getAllMyContacts()
    }
    fun getFriendContact(key:String): LiveData<MyContacts> {
        return myContactsRepository!!.getFriendContact(key)
    }
    fun contactsCount():LiveData<Int>{
        return myContactsRepository!!.contactsCount()
    }

    fun getContactsForChatFragment(check:String):LiveData<List<MyContacts>>{
        return myContactsRepository!!.getContactsForChatFragment(check)
    }
}