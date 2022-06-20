package com.kmj.albumactivity_prac

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kmj.albumactivity_prac.databinding.ActivitySongFragmentBinding


class SongFragment : Fragment() {

    lateinit var binding: ActivitySongFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= ActivitySongFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }
}