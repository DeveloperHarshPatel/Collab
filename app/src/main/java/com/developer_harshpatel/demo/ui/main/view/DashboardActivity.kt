package com.developer_harshpatel.demo.ui.main.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer_harshpatel.demo.R
import com.developer_harshpatel.demo.data.api.ApiHelper
import com.developer_harshpatel.demo.data.api.ApiServiceImpl
import com.developer_harshpatel.demo.data.model.History
import com.developer_harshpatel.demo.data.model.User
import com.developer_harshpatel.demo.ui.base.App
import com.developer_harshpatel.demo.ui.base.DashboardViewModelProvider
import com.developer_harshpatel.demo.ui.base.DataBaseViewModelProvider
import com.developer_harshpatel.demo.ui.main.adapter.DashboardAdapter
import com.developer_harshpatel.demo.ui.main.viewmodel.DashboardViewModel
import com.developer_harshpatel.demo.ui.main.viewmodel.DataBaseViewModel
import com.developer_harshpatel.demo.utils.Common
import com.developer_harshpatel.demo.utils.Status
import com.developer_harshpatel.demo.utils.URIPathHelper
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.adapter_dashboard.view.*

class DashboardActivity : AppCompatActivity(), DashboardAdapter.ItemClickInterface, DashboardAdapter.AudioClickInterface,
    DashboardAdapter.VideoClickInterface{

    // for save history of video
    private lateinit var dataBaseViewModel: DataBaseViewModel

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var adapter: DashboardAdapter

    // to prevent alert dialog open multiple time
    private var isPermissionDialogShowing: Boolean = false

    var userId: Int = 0
    var userName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.setIcon(R.drawable.app_logo);
        supportActionBar?.setDisplayUseLogoEnabled(true);

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = DashboardAdapter(arrayListOf(), this, this, this)
        recyclerView.adapter = adapter

        setupViewModel()
        setupObserver()
    }

    private fun setupViewModel(){
        dashboardViewModel = ViewModelProvider(this,
            DashboardViewModelProvider(ApiHelper(ApiServiceImpl()))
        )[DashboardViewModel::class.java]

        dataBaseViewModel = ViewModelProvider(this,
            DataBaseViewModelProvider(application, -1)
        )[DataBaseViewModel::class.java]
    }

    private fun setupObserver() {
        // this observer provide list of user on finishing api call
        dashboardViewModel.getUsers().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    it.data?.let { users -> renderList(users.data) }
                    recyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    it.message?.let { it1 -> App.showToast(it1) }
                }
            }
        })
    }

    private fun renderList(users: List<User>) {
        adapter.addData(users)
        // need for refresh adapter so list can be visible on screen
        adapter.notifyDataSetChanged()
    }

    override fun onItemClick(user: User) {
        val i = Intent(this@DashboardActivity, DetailActivity::class.java)
        i.putExtra("name", user.first_name + " " + user.last_name)
        i.putExtra("id", user.id)
        startActivity(i)
    }

    override fun onAudioIconClick(user: User) {
        if (isPermissionsGranted()) {
            val i = Intent(this@DashboardActivity, AudioRecordActivity::class.java)
            i.putExtra("name", user.first_name + " " + user.last_name)
            i.putExtra("id", user.id)
            startActivity(i)
        } else {
            checkPermissions()
        }
    }

    override fun onVideoIconClick(user: User) {
        if (isPermissionsGranted()) {
            // launch device camera to record video
            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            userName = user.first_name + " " + user.last_name
            userId = user.id
            startForResult.launch(intent)
        } else {
            checkPermissions()
        }
    }

    private fun isPermissionsGranted(): Boolean {

            return (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED)
    }

    private fun checkPermissions() {
        if (!isPermissionsGranted()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA),
                    0)
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA),
                    0)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                App.log("Can manage storage")
            } else {
                //request for the permission
                if (!isPermissionDialogShowing) {
                    val alertDialog: AlertDialog.Builder =
                        AlertDialog.Builder(this@DashboardActivity)
                    alertDialog.setTitle("Permission required")
                    alertDialog.setMessage("Please allow permission to manage eternal storage.")
                    alertDialog.setPositiveButton("Open Settings") { _, _ ->
                        val intent =
                            Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                        val uri: Uri = Uri.fromParts("package", getPackageName(), null);
                        intent.data = uri;
                        startActivity(intent);
                    }
                    val alert = alertDialog.create()
                    alert.setCanceledOnTouchOutside(false)
                    alert.show()
                    isPermissionDialogShowing = true
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.dashboard_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_history -> {
                App.showToast("Menu example")
                true
            }
            R.id.action_search ->{
                App.showToast("Menu example")
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // insteadof startActivityForResult because startActivityForResult is deprecated
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            if (intent?.data != null) {
                val uriPathHelper = URIPathHelper()
                val videoFullPath = uriPathHelper.getPath(this, intent.data!!)
                App.log("" + videoFullPath)
                // save video uri
                dataBaseViewModel.addHistory(History(userId, userName, videoFullPath.toString(), false, Common.currentTime()))
                App.showToast("Video Saved")
            }
        }
    }

}