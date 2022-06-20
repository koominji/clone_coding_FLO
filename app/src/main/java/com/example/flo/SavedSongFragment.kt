package com.example.flo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.data.entities.Song
import com.example.flo.databinding.ActivitySavedSongFragmentBinding

class SavedSongFragment : Fragment() {
    lateinit var binding: ActivitySavedSongFragmentBinding
    private var songList = ArrayList<SavedSong>()

    lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivitySavedSongFragmentBinding.inflate(inflater, container, false)

        songDB= SongDatabase.getInstance(requireContext())!!


        return binding.root

    }

    override fun onStart() {
        super.onStart()
        initRecyclerView()
    }
    private fun initRecyclerView(){

        val savedSongAdapter = LockerMusicRVAdapter()

        val layoutManager = LinearLayoutManager(context)

        savedSongAdapter.setMyItemClickListener(object : LockerMusicRVAdapter.MyItemClickListener {

            override fun onRemoveAlbum(songId: Int) {
                songDB.songDao().updateIsLikeById(false,songId)
                Log.d("??","${songDB.songDao().getSong(songId)}")
            }
        })
        binding.lockerMusicRv.layoutManager = layoutManager
        binding.lockerMusicRv.adapter = savedSongAdapter

        savedSongAdapter.addSongs(songDB.songDao().getLikeSongs(true)as ArrayList<Song>)


    }
}