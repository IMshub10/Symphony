package com.example.symphony.Room.Repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.symphony.Room.Dao.StatusDao
import com.example.symphony.Room.Database.StatusDatabase
import com.example.symphony.Room.Model.Status


class StatusRepository(application: Application?) {
    private val statusDao: StatusDao

    init {
        val statusDatabase =
            StatusDatabase.getInstance(application!!)
        statusDao = statusDatabase!!.statusDao()!!
    }

    suspend fun insert(status: Status) {
        statusDao.insert(status)
    }

    suspend fun update(status: Status) {
        statusDao.update(status)
    }
    suspend fun updateSeen(key: String,seen: Boolean) {
        statusDao.updateSeen(key,seen)
    }

    suspend fun delete(status: Status) {
        statusDao.delete(status)
    }

    suspend fun deleteAllStatus() {
        statusDao.deleteAllStatus()
    }
    suspend fun deleteByKey(key:String) {
        statusDao.deleteByKey(key)
    }

    fun getAllStatus(key:String,seen:Boolean): LiveData<List<Status>> {
        return statusDao.getAllStatus(key,seen)
    }
    fun getMyStatus(key:String): LiveData<Status> {
        return statusDao.getMyStatus(key)
    }
}