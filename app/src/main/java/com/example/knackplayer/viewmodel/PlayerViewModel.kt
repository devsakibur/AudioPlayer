package com.example.knackplayer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.knackplayer.model.Audio
import com.example.knackplayer.repository.AudioRepository


class PlayerViewModel(private val audioRepository: AudioRepository):ViewModel() {

    val audioList : LiveData<List<Audio>> = audioRepository.audioFilesLiveData

    fun loadAudioFiles() {
       audioRepository.loadAudioFiles()
    }

}