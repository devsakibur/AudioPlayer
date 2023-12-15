package com.example.knackplayer.ui.main


import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.knackplayer.adapter.AudioRecyclerViewAdapter
import com.example.knackplayer.databinding.FragmentAudioBinding
import com.example.knackplayer.repository.AudioRepository
import com.example.knackplayer.repository.AudioViewModelFactory
import com.example.knackplayer.viewmodel.PlayerViewModel

class AudioFragment : Fragment() {
private lateinit var binding : FragmentAudioBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAudioBinding.inflate(layoutInflater)


        val audioRepository = AudioRepository(requireContext().contentResolver)
        val viewModel = ViewModelProvider(this,
            AudioViewModelFactory(audioRepository))[PlayerViewModel::class.java]


        val mediaPlayer = MediaPlayer()


        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.loadAudioFiles()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }

            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        }
        viewModel.loadAudioFiles()

        viewModel.audioList.observe(this) {
            val songList = it
            var position : Int? = null
            binding.audioRecyclerView.adapter = AudioRecyclerViewAdapter(it){
                position = it
                mediaPlayer.reset()
                mediaPlayer.setDataSource(songList[it].path)
                binding.titleTextView.text = songList[it].title
                mediaPlayer.prepare()
                mediaPlayer.start()
                binding.playButton.visibility = View.INVISIBLE

            }

            binding.nextButton.setOnClickListener {
                mediaPlayer.reset()
                mediaPlayer.setDataSource(songList[position!!+1].path)
                binding.titleTextView.text = songList[position!! +1].title
                mediaPlayer.prepare()
                mediaPlayer.start()
            }
            binding.prevButton.setOnClickListener {
                mediaPlayer.reset()
                mediaPlayer.setDataSource(songList[position!!- 1].path)
                binding.titleTextView.text = songList[position!! - 1].title
                mediaPlayer.prepare()
                mediaPlayer.start()
            }



        }


        if(!mediaPlayer.isPlaying){
            binding.playButton.visibility = View.VISIBLE
            binding.pauseButton.visibility = View.INVISIBLE
        }


        binding.playButton.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
                binding.playButton.visibility = View.INVISIBLE
                binding.pauseButton.visibility = View.VISIBLE
            }
        }

        binding.pauseButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                binding.playButton.visibility = View.VISIBLE
                binding.pauseButton.visibility = View.INVISIBLE
            }
        }




        return binding.root
    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext() , "Storage Permission Granted" , Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext() , "Storage Permission Not Granted" , Toast.LENGTH_SHORT).show()

            }
        }



}