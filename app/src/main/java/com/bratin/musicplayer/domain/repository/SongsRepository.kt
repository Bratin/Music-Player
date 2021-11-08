package com.bratin.musicplayer.domain.repository

import com.bratin.musicplayer.domain.models.SongData

interface SongsRepository {
    fun getSongsList(): ArrayList<SongData>
}