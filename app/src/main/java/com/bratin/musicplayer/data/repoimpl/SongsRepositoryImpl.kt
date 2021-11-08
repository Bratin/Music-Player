package com.bratin.musicplayer.data.repoimpl

import android.content.Context
import com.bratin.musicplayer.data.mapper.SongsDataMapper
import com.bratin.musicplayer.data.utils.scanSDCard
import com.bratin.musicplayer.domain.repository.SongsRepository
import javax.inject.Inject

class SongsRepositoryImpl @Inject constructor(
    private val songsDataMapper: SongsDataMapper,
    private val context: Context
) : SongsRepository {

    override fun getSongsList() = songsDataMapper.mapToEntityFromDto(scanSDCard(context))
}