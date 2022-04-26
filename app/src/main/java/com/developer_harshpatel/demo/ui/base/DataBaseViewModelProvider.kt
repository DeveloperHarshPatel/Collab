package com.developer_harshpatel.demo.ui.base

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.developer_harshpatel.demo.ui.main.viewmodel.DataBaseViewModel


class DataBaseViewModelProvider(private val mApplication: Application, private val userId: Int) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DataBaseViewModel(mApplication, userId) as T
    }
}