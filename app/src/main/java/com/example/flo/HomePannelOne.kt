package com.example.flo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.ActivityHomePannelOneBinding

class HomePannelOne : Fragment() {
    lateinit var binding:ActivityHomePannelOneBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= ActivityHomePannelOneBinding.inflate(inflater,container,false)
        return binding.root
    }
}