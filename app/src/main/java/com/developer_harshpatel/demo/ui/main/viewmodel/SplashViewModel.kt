package com.developer_harshpatel.demo.ui.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.developer_harshpatel.demo.data.model.SplashModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    var liveData: MutableLiveData<SplashModel> = MutableLiveData()

    fun initSplashScreen() {
        viewModelScope.launch {
            delay(3000)
            updateLiveData()
        }
    }

    private fun updateLiveData() {
        val splashModel = SplashModel()
        liveData.value = splashModel
    }
}