package com.developer_harshpatel.demo.ui.main.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.developer_harshpatel.demo.R
import com.developer_harshpatel.demo.ui.base.App
import com.developer_harshpatel.demo.ui.main.viewmodel.LoginViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        supportActionBar?.hide()

        btnLogin.setOnClickListener {
            val view = this.currentFocus
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as
                    InputMethodManager
            if (view != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
            }

            val email:String = etEmail.text.toString()
            val password:String = etPassword.text.toString()
            App.log(email)
            App.log(password)

            // this observer provide boolean value for allow login or not
            // onchange called when value of isLoginSuccess change
            loginViewModel.validateCredentials(email,password).observe(this, object:Observer<Boolean> {
                override fun onChanged(isLoginSuccess: Boolean) {
                    if (isLoginSuccess) {
                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            })
        }
    }
}