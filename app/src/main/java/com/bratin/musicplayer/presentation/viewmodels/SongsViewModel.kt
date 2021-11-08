package com.bratin.musicplayer.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bratin.musicplayer.domain.models.SongData
import com.bratin.musicplayer.domain.usecases.SongsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongsViewModel @Inject constructor(private val songsUseCases: SongsUseCases) : ViewModel() {
    var songsLiveData = MutableLiveData<ArrayList<SongData>>()

    fun getSongsList() = viewModelScope.launch {
        songsLiveData.value = songsUseCases.getSongsList()
    }
}