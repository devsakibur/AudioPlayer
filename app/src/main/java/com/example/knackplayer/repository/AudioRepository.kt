package com.example.knackplayer.repository

import android.content.ContentResolver
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import com.example.knackplayer.model.Audio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AudioRepository(private val contentResolver: ContentResolver) {


    val audioFilesLiveData: MutableLiveData<List<Audio>> = MutableLiveData()
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    fun loadAudioFiles() {
        // Use a background thread to query and load audio files
        coroutineScope.launch{
            val audioFiles = queryAudioFiles()
            audioFilesLiveData.postValue(audioFiles)
        }
    }


    private fun queryAudioFiles(): List<Audio> {
        val audioFiles = mutableListOf<Audio>()
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0 AND ${MediaStore.Audio.Media.MIME_TYPE} = ?"
        val selectionArgs = arrayOf("audio/mpeg")

        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                val title = it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                val artist = it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                val path = it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))

                val audioFile = Audio(id, title, artist, path)
                audioFiles.add(audioFile)
            }
        }

        return audioFiles
    }



}