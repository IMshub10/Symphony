package com.example.symphony.Room.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.symphony.Room.Model.Status
import com.example.symphony.Room.Repository.StatusRepository
import kotlinx.coroutines.*

class StatusViewModel(application: Application) : AndroidViewModel(application) {
    var statusRepository: StatusRepository?  = null
    private  lateinit var job: CompletableJob

    init {
        statusRepository = StatusRepository(application)
    }

    fun insert(status: Status) {
        job= Job()
        CoroutineScope(Dispatchers.IO+ job).launch {
            statusRepository!!.insert(status)
            job.complete()
        }
    }

    fun update(status: Status) {
        job= Job()
        CoroutineScope(Dispatchers.IO +job).launch {
            statusRepository!!.update(status)
            job.complete()
        }
    }
    fun updateSeen(key: String,seen: Boolean) {
        job= Job()
        CoroutineScope(Dispatchers.IO +job).launch {
            statusRepository!!.updateSeen(key,seen)
            job.complete()
        }
    }

    fun delete(status: Status) {
        job= Job()
        CoroutineScope(Dispatchers.IO +job).launch {
            statusRepository!!.delete(status)
            job.complete()
        }
    }

    fun deleteAllStatus() {
        job= Job()
        CoroutineScope(Dispatchers.IO +job).launch {
            statusRepository!!.deleteAllStatus()
            job.complete()
        }
    }
    fun deleteByKey(key: String) {
        job= Job()
        CoroutineScope(Dispatchers.IO +job).launch {
            statusRepository!!.deleteByKey(key)
            job.complete()
        }
    }
    fun getAllStatus(key:String,seen:Boolean): LiveData<List<Status>> {
        return statusRepository!!.getAllStatus(key,seen)
    }
    fun getMyStatus(key:String): LiveData<Status> {
        return statusRepository!!.getMyStatus(key)
    }

}