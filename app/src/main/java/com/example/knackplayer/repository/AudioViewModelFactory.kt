package com.example.knackplayer.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.knackplayer.viewmodel.PlayerViewModel

class AudioViewModelFactory(private val audioRepository: AudioRepository) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            return PlayerViewModel(audioRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
