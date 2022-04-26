package com.developer_harshpatel.demo.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.developer_harshpatel.demo.R
import com.developer_harshpatel.demo.ui.base.App
import com.developer_harshpatel.demo.utils.Common
import kotlin.math.log

class LoginValidationRepository(application: Application) {

    var application:Application

    init {
        this.application = application
    }

    // Email Address validation
    // Password validation
    fun validateLoginInputs(email:String, password:String): LiveData<Boolean> {
        val isLoginSuccess = MutableLiveData<Boolean>()
        App.log("validateLoginInputs")
        App.log(email)
        App.log(password)
        App.log("length: " + password.length)
        var statusMsg:String
        if (Common.isEmailValid(email)) {
            if (Common.isPasswordValid(password)) {
                // correct email & password
                // login success
                isLoginSuccess.value = true
                statusMsg = application.getString(R.string.msg_welcome)
            } else {
                isLoginSuccess.value = false
                statusMsg = application.getString(R.string.msg_invalid_pas)
            }
        } else {
            isLoginSuccess.value = false
            statusMsg = application.getString(R.string.msg_invalid_email)
        }
        App.showToast(statusMsg)
        return isLoginSuccess
    }

}