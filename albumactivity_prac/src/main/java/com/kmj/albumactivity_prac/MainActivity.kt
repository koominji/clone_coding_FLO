package com.kmj.albumactivity_prac

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_fragment)
        val fragmentManager : FragmentManager =supportFragmentManager
        val transaction : FragmentTransaction = fragmentManager.beginTransaction()
        val fragment = AlbumFragment()
        transaction.add(R.id.fragment_content, fragment)
        transaction.commit()
    }
}