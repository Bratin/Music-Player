package com.bratin.musicplayer.domain.models

import android.net.Uri

data class SongData(
    var id: Long,
    var title: String,
    var mediaUri: Uri,
    var duration: String,
    var durationMillis: Long
)