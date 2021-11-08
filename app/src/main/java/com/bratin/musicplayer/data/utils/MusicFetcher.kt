package com.bratin.musicplayer.data.utils

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.bratin.musicplayer.data.models.RawSongData
import java.lang.Exception
import java.util.Locale
import java.util.concurrent.TimeUnit

private const val ONE_HR_IN_MILLIS = 1 * 60 * 60 * 1000

/**
 * Get music files stored in android sd card
 */
fun scanSDCard(context: Context): ArrayList<RawSongData> {
    val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
    val sortOrder = MediaStore.Audio.AudioColumns.TITLE + " ASC"
    val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.DURATION
    )
    var cursor: Cursor? = null
    val alMd: ArrayList<RawSongData> = ArrayList()
    try {
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        cursor = context.contentResolver.query(uri, projection, selection, null, sortOrder)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val md = RawSongData(
                    id = cursor.getLong(0),
                    title = cursor.getString(1),
                    mediaUri = ContentUris.withAppendedId(
                        uri,
                        cursor.getLong(0)
                    ),
                    duration = formatTimeMillis(cursor.getLong(2))
                )
                alMd.add(md)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        cursor?.close()
    }
    return alMd
}

private fun formatTimeMillis(timeMillis: Long): String {
    val formattedTime: String = if (timeMillis < ONE_HR_IN_MILLIS) {
        String.format(
            Locale.getDefault(), "%01d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(timeMillis) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeMillis)),
            TimeUnit.MILLISECONDS.toSeconds(timeMillis) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeMillis))
        )
    } else {
        String.format(
            Locale.getDefault(), "%01d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(timeMillis),
            TimeUnit.MILLISECONDS.toMinutes(timeMillis) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeMillis)),
            TimeUnit.MILLISECONDS.toSeconds(timeMillis) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeMillis))
        )
    }
    return formattedTime
}