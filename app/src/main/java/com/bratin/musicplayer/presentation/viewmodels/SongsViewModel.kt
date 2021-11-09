package com.bratin.musicplayer.presentation.viewmodels

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.os.RemoteException
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bratin.musicplayer.data.utils.formatTimeMillis
import com.bratin.musicplayer.domain.models.SongData
import com.bratin.musicplayer.domain.usecases.SongsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class SongsViewModel @Inject constructor(private val songsUseCases: SongsUseCases) : ViewModel() {
    var songsLiveData = MutableLiveData<ArrayList<SongData>>()
    var songProgress = MutableLiveData<Int>()
    private val mMediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())

    fun getSongsList() = viewModelScope.launch {
        songsLiveData.value = songsUseCases.getSongsList()
    }

    fun playPauseMusic(context: Context) {
        if (mMediaPlayer.isPlaying) {
            mMediaPlayer.pause()
            handler.removeCallbacks(r)
        } else {
            getCurrentSong()?.mediaUri?.let {
                mMediaPlayer.reset()
                mMediaPlayer.setDataSource(context, it)
                mMediaPlayer.prepare()
                mMediaPlayer.start()
                updateTime()
            }
        }
    }

    fun stopMusic() {
        mMediaPlayer.pause()
        mMediaPlayer.reset()
        handler.removeCallbacks(r)
    }

    fun getCurrentSong() = songsLiveData.value?.get(0)

    fun getCurrentProgress() = mMediaPlayer.currentPosition

    fun isPlaying() = mMediaPlayer.isPlaying

    fun updateTime() {
        handler.post(r)
    }

    var r = object : Runnable {
        override fun run() {
            songProgress.value = mMediaPlayer.currentPosition
            handler.postDelayed(this, 100)
        }
    }
}