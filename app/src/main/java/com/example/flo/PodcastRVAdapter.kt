package com.example.flo

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.data.entities.Album
import com.example.flo.databinding.AlbumRvItemBinding


// 팟캐스트 RecyclerView
class PodcastViewHolder(val binding: AlbumRvItemBinding) : RecyclerView.ViewHolder(binding.root)

class PodcastRVAdapter(val datas: MutableList<Album>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int = datas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        PodcastViewHolder(
            AlbumRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("podcast", "onBindViewHolder : $position")
        val binding = (holder as PodcastViewHolder).binding
        binding.todayAlbumIv.setImageResource(datas[position].coverImg!!) //album image
        binding.todayAlbumTitleTv.text = datas[position].title // album title
        binding.todayAlbumSingerTv.text=datas[position].singer // album singer
        binding.homeTodayAlbumItemRoot.setOnClickListener {
            Log.d("podcast","item : ${position}")
        }

    }
}
