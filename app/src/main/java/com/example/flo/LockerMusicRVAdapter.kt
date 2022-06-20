package com.example.flo

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.data.entities.Song
import com.example.flo.databinding.LockerMusicRvItemBinding


// 보관함 음악 RecyclerView
class LockerMusicRVAdapter() :
    RecyclerView.Adapter<LockerMusicRVAdapter.ViewHolder>() {

    private val songs = ArrayList<Song>()

    // 클릭 인터페이스 정의
    interface MyItemClickListener {
        fun onRemoveAlbum(songId: Int)
    }

    // 리스너 객체를 전달받는 함수랑 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun getItemCount(): Int = songs.size

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): LockerMusicRVAdapter.ViewHolder {
        val binding: LockerMusicRvItemBinding = LockerMusicRvItemBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LockerMusicRVAdapter.ViewHolder, position: Int) {
        Log.d("??", "onBindViewHolder : $position")
        holder.bind(songs[position])
        holder.binding.lockerMusicRvItemRoot.setOnClickListener {
            Log.d("??", "${position}")
        }

        // 더보기(...) 버튼 클릭하면 해당 item 삭제
        holder.binding.lockerMusicMoreBtnIv.setOnClickListener {
            mItemClickListener.onRemoveAlbum(songs[position].id)
            removeSong(position)
        }

    }


    fun addSongs(songs: ArrayList<Song>) {
        this.songs.clear()
        this.songs.addAll(songs)

        notifyDataSetChanged()
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun removeSong(position: Int){
        songs.removeAt(position)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: LockerMusicRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(song: Song) {
            binding.lockerMusicCoverIv.setImageResource(song.coverImg!!) //song image
            binding.lockerMusicTitleTv.text = song.title // song title
            binding.lockerMusicSingerTv.text = song.singer // song singer
        }

    }



}
