package com.developer_harshpatel.demo.ui.main.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.developer_harshpatel.demo.R
import com.developer_harshpatel.demo.data.model.History
import com.developer_harshpatel.demo.data.repository.AudioRecorderRepository
import com.developer_harshpatel.demo.ui.base.App
import com.developer_harshpatel.demo.ui.base.DataBaseViewModelProvider
import com.developer_harshpatel.demo.ui.base.RecorderViewModelProvider
import com.developer_harshpatel.demo.ui.main.viewmodel.AudioRecorderViewModel
import com.developer_harshpatel.demo.ui.main.viewmodel.DataBaseViewModel
import com.developer_harshpatel.demo.utils.Common
import com.developer_harshpatel.demo.utils.RecorderState
import kotlinx.android.synthetic.main.activity_audio_record.*

class AudioRecordActivity : AppCompatActivity() {
    // for audio recording logic
    private var audioViewModel: AudioRecorderViewModel? = null

    // for save history logic
    private lateinit var dataBaseViewModel: DataBaseViewModel

    private var userName : String = ""
    private var userId: Int = 0

    // to prevent alert dialog open multiple time
    private var isPermissionDialogShowing: Boolean = false

    private var isStarted: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_record)

        userName = intent.getStringExtra("name").toString()
        userId = intent.getIntExtra("id", 0)

        checkPermissions()

        audioViewModel = ViewModelProvider(this,
            RecorderViewModelProvider(AudioRecorderRepository.getInstance()))[AudioRecorderViewModel::class.java]

        dataBaseViewModel = ViewModelProvider(this,
            DataBaseViewModelProvider(application, userId)
        )[DataBaseViewModel::class.java]

        fabStartRecording.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                val permissions = arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(this, permissions,0)
            } else {
                isStarted = true
                startRecording()
            }
        }

        fabStopRecording.setOnClickListener{
            stopRecording()
        }

        fabPauseRecording.setOnClickListener {
            pauseRecording()
        }

        fabResumeRecording.setOnClickListener {
            resumeRecording()
        }

        if(audioViewModel?.recorderState == RecorderState.Stopped){
            fabStopRecording.isEnabled = false
        }

        setupObserver()
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO),
                    0)
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO),
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
            // to check MANAGE_EXTERNAL_STORAGE permission allowed or not.
            // MANAGE_EXTERNAL_STORAGE introduced in Android R
            if (Environment.isExternalStorageManager()) {
                App.log("Can manage storage")
            } else {
                //request for the permission
                    if (!isPermissionDialogShowing) {
                        val alertDialog: AlertDialog.Builder =
                            AlertDialog.Builder(this@AudioRecordActivity)
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

    private fun setupObserver() {
        // use for update recording time
        audioViewModel?.getRecordingTime()?.observe(this, Observer {
            tvRecordingTime.text = it
        })
        // use to get file path
        audioViewModel?.getFilePath()?.observe(this, Observer {
            if (isStarted) {
                val filePath: String = it
                App.log(filePath)
                dataBaseViewModel.addHistory(
                    History(
                        userId,
                        userName,
                        filePath,
                        true,
                        Common.currentTime()
                    )
                )
                App.showToast("Audio Saved")
            }
        })
    }

    private fun startRecording() {
        audioViewModel?.startRecording()

        fabStopRecording.isEnabled = true
        fabStartRecording.visibility = View.INVISIBLE
        fabPauseRecording.visibility = View.VISIBLE
        fabResumeRecording.visibility = View.INVISIBLE
    }

    private fun stopRecording(){
        audioViewModel?.stopRecording()

        fabStopRecording.isEnabled = false
        fabStartRecording.visibility = View.VISIBLE
        fabPauseRecording.visibility = View.INVISIBLE
        fabResumeRecording.visibility = View.INVISIBLE
    }

    private fun pauseRecording(){
        audioViewModel?.pauseRecording()

        fabStopRecording.isEnabled = true
        fabStartRecording.visibility = View.INVISIBLE
        fabPauseRecording.visibility = View.INVISIBLE
        fabResumeRecording.visibility = View.VISIBLE
    }

    private fun resumeRecording(){
        audioViewModel?.resumeRecording()
        fabStopRecording.isEnabled = true
        fabStartRecording.visibility = View.INVISIBLE
        fabPauseRecording.visibility = View.VISIBLE
        fabResumeRecording.visibility = View.INVISIBLE
    }

}