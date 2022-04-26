package com.developer_harshpatel.demo.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.developer_harshpatel.demo.data.api.ApiHelper
import com.developer_harshpatel.demo.data.repository.DashboardRepository
import com.developer_harshpatel.demo.ui.main.viewmodel.DashboardViewModel

// ViewModelProviders internally manage for us and call our primary constructor of ViewModel and
// create the instance of ViewModel and give the instance back
class DashboardViewModelProvider(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(DashboardRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}