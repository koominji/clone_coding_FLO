package com.kmj.albumactivity_prac

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.kmj.albumactivity_prac.AlbumVPAdapter
import com.kmj.albumactivity_prac.databinding.ActivityAlbumFragmentBinding

class AlbumFragment : Fragment() {
    lateinit var binding: ActivityAlbumFragmentBinding

    private val information =  arrayListOf("수록곡","상세정보","영상")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= ActivityAlbumFragmentBinding.inflate(inflater,container,false)

        binding.albumBackIv.setOnClickListener {

        }

        val albumAdapter = AlbumVPAdapter(this)
        binding.albumContentVp.adapter=albumAdapter

        TabLayoutMediator(binding.albumContentTb,binding.albumContentVp){
                tab,position ->
            tab.text=information[position]
        }.attach()

        return binding.root
    }
}