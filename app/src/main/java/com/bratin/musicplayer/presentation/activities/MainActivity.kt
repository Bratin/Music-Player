package com.bratin.musicplayer.presentation.activities

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bratin.musicplayer.R
import com.bratin.musicplayer.databinding.ActivityMainBinding
import com.bratin.musicplayer.presentation.adapter.SongsListAdapter
import com.bratin.musicplayer.presentation.viewmodels.SongsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val fileReadPermission = Manifest.permission.READ_EXTERNAL_STORAGE
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var binding: ActivityMainBinding

    private val songsViewModel: SongsViewModel by viewModels()
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPref =
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                when {
                    isGranted -> showSongsList()
                    shouldShowRequestPermissionRationale(fileReadPermission) -> {
                        showRequestPermissionRationale()
                    }
                    else -> showPermissionDeniedError()
                }
            }
        checkPermissionAndGetSongsIfPossible()

        songsViewModel.getCurrentSong()
            ?.let { binding.musicPlayer.setMusicData(it) }

        binding.musicPlayer.onPlayBtnClicked = {
            songsViewModel.playPauseMusic(this)
            binding.musicPlayer.updateMusicUI(songsViewModel.isPlaying())
        }
    }

    /**
     * Checks file read permission, if not granted ask for it, if granted show songs list
     */
    private fun checkPermissionAndGetSongsIfPossible() {
        when {
            ContextCompat.checkSelfPermission(
                this, fileReadPermission
            ) == PackageManager.PERMISSION_GRANTED -> {
                showSongsList()
            }
            shouldShowRequestPermissionRationale(fileReadPermission) -> {
                showRequestPermissionRationale()
            }
            sharedPref.getBoolean("permission_req_shown", false) -> showPermissionDeniedError()
            else -> requestPermissionLauncher.launch(fileReadPermission)
        }
    }

    private fun showSongsList() {
        songsViewModel.songsLiveData.observe(this) { songList ->
            val songsListAdapter = SongsListAdapter()
            songsListAdapter.setList(songList)
            binding.rvMusicList.adapter = songsListAdapter
        }
        songsViewModel.songProgress.observe(this) { timer ->
            binding.musicPlayer.updateTime(timer)
        }
        songsViewModel.getSongsList()
    }

    override fun onStop() {
        super.onStop()
        songsViewModel.stopMusic()
    }

    private fun showPermissionDeniedError() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setMessage(getString(R.string.permission_error_settings))
        alertDialogBuilder.setPositiveButton("Exit App") { _, _ -> finish() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showRequestPermissionRationale() {
        sharedPref.edit().putBoolean("permission_req_shown", true).apply()
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setMessage(getString(R.string.permission_error))
        alertDialogBuilder.setPositiveButton("Grant Permission") { _, _ ->
            requestPermissionLauncher.launch(fileReadPermission)
        }
        alertDialogBuilder.setNegativeButton("No, Exit App") { _, _ -> finish() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}