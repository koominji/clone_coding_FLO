package com.example.flo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.ActivitySavedMusicFragmentBinding

class SavedMusicFragment : Fragment() {
   lateinit var binding:ActivitySavedMusicFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivitySavedMusicFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }
}