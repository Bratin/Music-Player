package com.bratin.musicplayer.data.models

import android.net.Uri

data class RawSongData(
    var id: Long,
    var title: String,
    var mediaUri: Uri,
    var duration: String,
    var durationMillis: Long
)