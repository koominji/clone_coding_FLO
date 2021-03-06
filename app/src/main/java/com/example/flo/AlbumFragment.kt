package com.example.flo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.data.entities.Album
import com.example.flo.data.entities.Like
import com.example.flo.databinding.ActivityAlbumFragmentBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment : Fragment() {
    lateinit var binding: ActivityAlbumFragmentBinding
    private var gson: Gson =Gson()

    private val information =  arrayListOf("수록곡","상세정보","영상")

    private var isLiked : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= ActivityAlbumFragmentBinding.inflate(inflater,container,false)

        val albumJson = arguments?.getString("album")
        val album = gson.fromJson(albumJson, Album::class.java)
        isLiked=isLikedAlbum(album.id)

        setInit(album)
        setOnClickListener(album)

        // 뒤로가기 버튼
        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }

        val albumAdapter = AlbumVPAdapter(this)
        binding.albumContentVp.adapter=albumAdapter

        TabLayoutMediator(binding.albumContentTb,binding.albumContentVp){
            tab,position ->
            tab.text=information[position]
        }.attach()

        return binding.root
    }

    private fun setInit(album: Album){
        binding.albumSongCoverIv.setImageResource(album.coverImg!!)
        binding.albumTitleTv.text=album.title.toString()
        binding.albumSingerTv.text=album.singer.toString()
        if(isLiked){
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }
        else{
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun getJwt():Int{
        val spf = activity?.getSharedPreferences("auth",AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt",0)
    }

    private fun likeAlbum(userId:Int, albumId:Int){
        val songDB = SongDatabase.getInstance((requireContext()))!!
        val like = Like(userId,albumId)

        songDB.albumDao().likeAlbum(like)
    }

    private fun isLikedAlbum(albumId:Int):Boolean {
        val songDB = SongDatabase.getInstance((requireContext()))!!
        val userId = getJwt()

        val likeId:Int? = songDB.albumDao().isLikedAlbum(userId,albumId)

        return likeId!=null
    }

    private fun disLikedAlbum(albumId:Int) {
        val songDB = SongDatabase.getInstance((requireContext()))!!
        val userId = getJwt()

        songDB.albumDao().disLikeAlbum(userId,albumId)
    }

    private fun setOnClickListener(album: Album){
        val userId=getJwt()
        binding.albumLikeIv.setOnClickListener {
            if(isLiked){
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
                disLikedAlbum(album.id)
            }
            else{
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId,album.id)
            }

        }
    }

}