package com.kmj.albumactivity_prac

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kmj.albumactivity_prac.databinding.ActivityDetailFragmentBinding

class DetailFragment : Fragment() {
    lateinit var binding: ActivityDetailFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityDetailFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }
}