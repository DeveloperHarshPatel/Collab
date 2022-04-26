package com.developer_harshpatel.demo.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.developer_harshpatel.demo.data.repository.AudioRecorderRepository
import com.developer_harshpatel.demo.ui.main.viewmodel.AudioRecorderViewModel

class RecorderViewModelProvider(private val audioRecorderRepository: AudioRecorderRepository):
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AudioRecorderViewModel(audioRecorderRepository) as T
    }
}