package com.bratin.musicplayer.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bratin.musicplayer.databinding.SongListCellBinding
import com.bratin.musicplayer.domain.models.SongData

class SongsListAdapter : RecyclerView.Adapter<SongsListAdapter.MyViewHolder>() {
    private var songList = mutableListOf<SongData>()
    //var setOnItemClickListener: (() -> Unit)? = null

    fun setList(list: List<SongData>) {
        songList = list as MutableList<SongData>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            SongListCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindDataToView(songList[position])
    }

    override fun getItemCount() = songList.size

    inner class MyViewHolder(var viewBinding: SongListCellBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bindDataToView(songData: SongData) {
            viewBinding.songName.text = songData.title
        }
    }
}