package com.example.flo

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.data.entities.Album
import com.example.flo.databinding.AlbumRvItemBinding


// 오늘의 음악 RecyclerView

class AlbumRVAdapter(val datas: ArrayList<Album>) :
    RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {

    // 클릭 인터페이스 정의
    interface MyItemClickListener{
        fun onItemClick(album: Album)
        fun onRemoveAlbum(position:Int)
        fun onPlayBtnClick(album: Album)
    }

    // 리스너 객체를 전달받는 함수랑 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    fun addItem(album: Album){
        datas.add(album)
        notifyDataSetChanged()
    }
    fun removeItem(position:Int){
        datas.removeAt(position)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = datas.size

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumRVAdapter.ViewHolder {
        val binding : AlbumRvItemBinding = AlbumRvItemBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: AlbumRVAdapter.ViewHolder, position: Int) {
        Log.d("??", "onBindViewHolder : $position")
        val binding = (holder as ViewHolder).binding
        holder.bind(datas[position])
        binding.homeTodayAlbumItemRoot.setOnClickListener {
            Log.d("??", "${position}")
        }

        // itemView의 클릭이벤트
        holder.itemView.setOnClickListener{
            mItemClickListener.onItemClick(datas[position])

       }

        // 재생 버튼 클릭이벤트
        binding.todayAlbumPlayIv.setOnClickListener {
            mItemClickListener.onPlayBtnClick(datas[position])
        }

        // title을 클릭하면 아이템 remove
//        holder.binding.todayAlbumTitleTv.setOnClickListener{
//            mItemClickListener.onRemoveAlbum(position)
//        }

    }
    inner class ViewHolder(val binding: AlbumRvItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(album: Album){
            binding.todayAlbumIv.setImageResource(album.coverImg!!) //album image
            binding.todayAlbumTitleTv.text = album.title // album title
            binding.todayAlbumSingerTv.text=album.singer // album singer
        }
    }

}
