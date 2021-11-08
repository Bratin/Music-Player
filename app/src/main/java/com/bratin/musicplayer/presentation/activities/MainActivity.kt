package com.bratin.musicplayer.presentation.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) showSongsList()
                else showPermissionDeniedError()
            }
    }

    override fun onResume() {
        super.onResume()
        checkPermissionAndGetSongsIfPossible()
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
                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setMessage(getString(R.string.permission_error))
                alertDialogBuilder.setPositiveButton("Grant Permission") { _, _ ->
                    requestPermissionLauncher.launch(fileReadPermission)
                }
                alertDialogBuilder.setNegativeButton("No, Exit App") { _, _ -> finish() }
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }
            else -> {
                requestPermissionLauncher.launch(fileReadPermission)
            }
        }
    }

    private fun showSongsList() {
        songsViewModel.songsLiveData.observe(this) { songList ->
            val songsListAdapter = SongsListAdapter()
            songsListAdapter.setList(songList)
            binding.rvMusicList.adapter = songsListAdapter
        }
        songsViewModel.getSongsList()
    }

    private fun showPermissionDeniedError() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage(getString(R.string.permission_error_settings))
        alertDialogBuilder.setPositiveButton("Settings") { _, _ ->
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        alertDialogBuilder.setNegativeButton("No, Exit App") { _, _ -> finish() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}