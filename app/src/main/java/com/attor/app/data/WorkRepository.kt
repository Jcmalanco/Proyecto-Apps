package com.attor.app.data

import android.content.Context
import com.attor.app.data.local.AppDatabase
import com.attor.app.util.SampleData

class WorkRepository(context: Context) {
    private val dao = AppDatabase.getInstance(context).workDao()

    suspend fun getAll(): List<Work> {
        return dao.getAllWorks()
    }

    suspend fun getById(id: String): Work? {
        return dao.getWorkById(id)
    }

    suspend fun publish(work: Work) {
        dao.insertWork(work)
    }

    suspend fun getWorksByOwner(ownerName: String): List<Work> {
        return dao.getWorksByOwner(ownerName)
    }

    suspend fun deleteById(id: String) {
        dao.deleteById(id)
    }

    suspend fun seedIfEmpty() {
        if (dao.getCount() == 0) {
            dao.insertWorks(SampleData.allWorks())
        }
    }
}
