package com.example.flo

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.AlbumRvItemBinding
import com.example.flo.databinding.HomePodcastRvItemBinding
import com.example.flo.databinding.HomeVideoRvItemBinding


// 오늘의 음악 RecyclerView
class HomeVideoViewHolder(val binding: HomeVideoRvItemBinding) : RecyclerView.ViewHolder(binding.root)

class HomeVideoRVAdapter(val datas: MutableList<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int = datas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        HomeVideoViewHolder(
            HomeVideoRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("??", "onBindViewHolder : $position")
        val binding = (holder as HomeVideoViewHolder).binding
        binding.homeVideoCoverIv.setImageResource(R.drawable.img_video_exp)
        binding.homeVideoNameTv.text="제목 " +datas[position]
        binding.homeVideoSingerTv.text = "가수 "+datas[position]
        binding.homeVideoRecyclerItem.setOnClickListener {
            Log.d("??", "video item $position")

        }

    }
}