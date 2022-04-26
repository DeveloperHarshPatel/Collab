package com.developer_harshpatel.demo.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import com.developer_harshpatel.demo.data.repository.AudioRecorderRepository
import com.developer_harshpatel.demo.utils.RecorderState

class AudioRecorderViewModel(val audioRecorderRepository: AudioRecorderRepository): ViewModel() {

    var recorderState: RecorderState = RecorderState.Stopped

    fun startRecording() = audioRecorderRepository.startRecording()

    fun stopRecording() = audioRecorderRepository.stopRecording()

    fun pauseRecording() = audioRecorderRepository.pauseRecording()

    fun resumeRecording() = audioRecorderRepository.resumeRecording()

    fun getRecordingTime() = audioRecorderRepository.getRecordingTime()

    fun getFilePath() = audioRecorderRepository.getFilePath()

}