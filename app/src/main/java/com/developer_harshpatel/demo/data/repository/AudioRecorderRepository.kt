package com.developer_harshpatel.demo.data.repository

import android.content.ContextWrapper
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.developer_harshpatel.demo.ui.base.App
import com.developer_harshpatel.demo.utils.Common
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.coroutines.coroutineContext

// business login for recording audio
class AudioRecorderRepository() {

    companion object {
        @Volatile
        private var instance: AudioRecorderRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: AudioRecorderRepository().also { instance = it }
            }
    }

    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null

    // file path
    private val dir: File = File(Environment.getExternalStorageDirectory().absolutePath + "/soundrecorder")

    private var recordingTime: Long = 0
    private var timer = Timer()
    private val recordingTimeString = MutableLiveData<String>()
    private val filePathString = MutableLiveData<String>()
    private var isStarted: Boolean = false

    init {
        isStarted = false
        try{
            App.log("Initialize")
            // create a File object for the parent directory
            val recorderDirectory = File(Environment.getExternalStorageDirectory().absolutePath+"/soundrecorder")
            // have the object build the directory structure, if needed.
            recorderDirectory.mkdirs()
        }catch (e: IOException){
            e.printStackTrace()
            App.log(""+e.message)
        }
    }

    fun startRecording() {
        isStarted = true
        try {
            initRecorder()
            println("Starting recording!")
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            timer = Timer()
            startTimer()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            App.log(""+e.message)
        } catch (e: IOException) {
            e.printStackTrace()
            App.log(""+e.message)
        }
    }

    fun stopRecording(){
        if (isStarted) {
            filePathString.postValue(output)
        }
        isStarted = false
        mediaRecorder?.stop()
        mediaRecorder?.release()
        stopTimer()
        resetTimer()
//        initRecorder()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun pauseRecording(){
        stopTimer()
        mediaRecorder?.pause()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun resumeRecording(){
        timer = Timer()
        startTimer()
        mediaRecorder?.resume()
    }

    private fun initRecorder() {
        mediaRecorder = MediaRecorder()

        if(dir.exists()){
            output = Environment.getExternalStorageDirectory().absolutePath + "/soundrecorder/recording"+Common.currentTimeStamp()+".mp3"
        }

        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder?.setOutputFile(output)
    }

    // to show record time on display
    private fun startTimer(){
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                recordingTime += 1
                updateDisplay()
            }
        }, 1000, 1000)
    }

    private fun stopTimer(){
        timer.cancel()
    }

    private fun resetTimer() {
        timer.cancel()
        recordingTime = 0
        recordingTimeString.postValue("00:00")
    }

    private fun updateDisplay(){
        val minutes = recordingTime / (60)
        val seconds = recordingTime % 60
        val str = String.format("%d:%02d", minutes, seconds)
        recordingTimeString.postValue(str)
    }

    // it will update time on ui
    fun getRecordingTime() = recordingTimeString

    // to get file path for save in db
    fun getFilePath() = filePathString
}