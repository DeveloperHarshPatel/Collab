package com.developer_harshpatel.demo.ui.main.view

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer_harshpatel.demo.R
import com.developer_harshpatel.demo.data.model.History
import com.developer_harshpatel.demo.ui.base.App
import com.developer_harshpatel.demo.ui.base.DataBaseViewModelProvider
import com.developer_harshpatel.demo.ui.main.adapter.HistoryAdapter
import com.developer_harshpatel.demo.ui.main.viewmodel.DataBaseViewModel
import kotlinx.android.synthetic.main.activity_detail.*
import java.io.IOException


class DetailActivity : AppCompatActivity(), HistoryAdapter.PlayClickInterface {

    private lateinit var dataBaseViewModel: DataBaseViewModel
    private lateinit var adapter: HistoryAdapter

    private var userName : String = ""
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        userName = intent.getStringExtra("name").toString()
        userId = intent.getIntExtra("id", 0)

        supportActionBar?.title = userName

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = HistoryAdapter(arrayListOf(), this)
        recyclerView.adapter = adapter

        setupViewModel()
        setupObserver()
    }

    private fun setupViewModel() {
        dataBaseViewModel = ViewModelProvider(this,
            DataBaseViewModelProvider(application, userId)
        )[DataBaseViewModel::class.java]
    }

    private fun setupObserver() {
        // provide data from local db
        dataBaseViewModel.listHistory.observe(this, Observer { list ->
            list?.let {
                //updating list.
                renderList(it)
                if (it.isEmpty()) {
                    App.showToast("No History")
                }
            }
        })
    }

    // on play button play recorded audio
    override fun onPlayIconClick(history: History) {
        try {
            val filePath = Uri.parse(history.filePath)
            App.log("" + filePath)

            val intent = Intent(Intent.ACTION_VIEW, filePath)
            intent.setDataAndType(filePath, "video/mp4")
            startActivity(intent)

        } catch (e: Exception) {
            App.showToast("Error while playing audio/video")
            App.log(""+e.message)
        }
    }

    private fun renderList(list: List<History>) {
        adapter.addData(list)
        adapter.notifyDataSetChanged()
    }

    override fun onStop() {
        super.onStop()
        adapter.onStop()
    }
}