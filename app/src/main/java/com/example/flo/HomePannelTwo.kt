package com.example.flo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.ActivityHomePannelOneBinding
import com.example.flo.databinding.ActivityHomePannelTwoBinding

class HomePannelTwo : Fragment() {
    lateinit var binding: ActivityHomePannelTwoBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= ActivityHomePannelTwoBinding.inflate(inflater,container,false)
        return binding.root
    }
}