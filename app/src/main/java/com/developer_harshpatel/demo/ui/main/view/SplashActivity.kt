package com.developer_harshpatel.demo.ui.main.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.developer_harshpatel.demo.R
import com.developer_harshpatel.demo.data.model.SplashModel
import com.developer_harshpatel.demo.ui.main.viewmodel.SplashViewModel

class SplashActivity : AppCompatActivity() {

    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        setupViewModel()
        setupObserver()
    }

    private fun setupViewModel() {
        splashViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
    }

    private fun setupObserver() {
        splashViewModel.initSplashScreen()
        val observer = Observer<SplashModel> {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        splashViewModel.liveData.observe(this, observer)
    }
}