package com.developer_harshpatel.demo.data.repository

import androidx.lifecycle.LiveData
import com.developer_harshpatel.demo.data.dao.HistoryDao
import com.developer_harshpatel.demo.data.model.History

class HistoryRepository(private val historyDao: HistoryDao, userId: Int) {

    val listHistory: LiveData<List<History>> = historyDao.getHistory(userId)

    suspend fun insert(history: History) {
        historyDao.insert(history)
    }
}