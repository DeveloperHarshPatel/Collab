package com.developer_harshpatel.demo.ui.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.developer_harshpatel.demo.data.repository.LoginValidationRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private var loginValidationRepository: LoginValidationRepository

    init {
        loginValidationRepository = LoginValidationRepository(application)
    }

    fun validateCredentials(email:String, password:String): LiveData<Boolean> {
        return loginValidationRepository.validateLoginInputs(email, password)
    }

}