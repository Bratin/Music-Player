package com.bratin.musicplayer.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bratin.musicplayer.data.utils.formatTimeMillis
import com.bratin.musicplayer.databinding.MusicPlayerViewBinding
import com.bratin.musicplayer.domain.models.SongData

/**
 * Music Player View, shows music playing status
 */
class MusicPlayerView constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs, 0) {

    var onPlayBtnClicked: (() -> Unit)? = null

    private val binding = MusicPlayerViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun setMusicData(data: SongData) {
        binding.apply {
            tvTitle.text = data.title
            btPlay.setOnClickListener {
                onPlayBtnClicked?.invoke()
            }
            seek.max = data.durationMillis.toInt()
        }
    }

    fun updateMusicUI(isMusicPlaying: Boolean) {
        binding.btPlay.text = if (isMusicPlaying) "Pause" else "Play"
    }

    fun updateTime(time: Int) {
        binding.apply {
            tvDuration.text = formatTimeMillis(time.toLong())
            seek.progress = time
        }
    }
}
