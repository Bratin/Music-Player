package com.bratin.musicplayer.domain.usecases

import com.bratin.musicplayer.domain.models.SongData
import com.bratin.musicplayer.domain.repository.SongsRepository
import javax.inject.Inject

class SongsUseCases @Inject constructor(private val repository: SongsRepository) {
    fun getSongsList(): ArrayList<SongData> = repository.getSongsList()
}