package com.developer_harshpatel.demo.ui.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer_harshpatel.demo.data.local.UserDatabase
import com.developer_harshpatel.demo.data.model.History
import com.developer_harshpatel.demo.data.repository.HistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DataBaseViewModel(application: Application, userId: Int) : ViewModel() {

    val listHistory: LiveData<List<History>>
    val repository: HistoryRepository

    init {
        val dao = UserDatabase.getDatabase(application).getHistoryDao()
        repository = HistoryRepository(dao, userId)
        listHistory = repository.listHistory
    }

    fun addHistory(history: History) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(history)
    }
}