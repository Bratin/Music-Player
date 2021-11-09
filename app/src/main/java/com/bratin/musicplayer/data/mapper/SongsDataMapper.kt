package com.bratin.musicplayer.data.mapper

import com.bratin.musicplayer.data.models.RawSongData
import com.bratin.musicplayer.domain.models.SongData
import javax.inject.Inject

class SongsDataMapper @Inject constructor() :
    BaseMapper<ArrayList<RawSongData>, ArrayList<SongData>> {

    override fun mapToEntityFromDto(data: ArrayList<RawSongData>): ArrayList<SongData> {
        val songsList = ArrayList<SongData>()
        data.forEach { song ->
            songsList.add(
                SongData(
                    id = song.id,
                    title = song.title,
                    mediaUri = song.mediaUri,
                    duration = song.duration,
                    durationMillis = song.durationMillis
                )
            )
        }
        return songsList
    }
}