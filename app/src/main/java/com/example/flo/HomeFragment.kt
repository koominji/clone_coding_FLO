package com.example.flo

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.afollestad.viewpagerdots.DotsIndicator
import com.example.flo.data.entities.Album
import com.example.flo.databinding.FragmentHomeBinding
import com.google.gson.Gson


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private var albumList = ArrayList<Album>()
    private lateinit var songDB:SongDatabase


    var currentPage: Int = 0

    //핸들러 설정
    //ui 변경하기
    val handler = Handler(Looper.getMainLooper()) {
        setPage()
        true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        // HomePannel의 배경 fragment
        val homePannelVPAdapter = HomePannelVPAdapter(this)
        homePannelVPAdapter.addFragment(HomePannelOne())
        homePannelVPAdapter.addFragment(HomePannelTwo())
        homePannelVPAdapter.addFragment(HomePannelThree())
        binding.homePannelVp.adapter=homePannelVPAdapter
        binding.homePannelVp.orientation=ViewPager2.ORIENTATION_HORIZONTAL

        // HomePannel의 indicator
        val indicator = binding.homePannelIndicator
        indicator.count=3
        indicator.selection=0

        binding.homePannelVp.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                indicator.selection=position
            }
        })


        // HomeFragment의 배너
        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        //뷰페이저 넘기는 쓰레드
        val thread = Thread(PagerRunnable())
        thread.start()

        // 더미 데이터
//        albumList.apply {
//            add(Album(R.drawable.img_album_exp2, "IU 5th Album 'LILAC'", "아이유(IU)"))
//            add(Album(R.drawable.butter, "BUTTER", "방탄소년단(BTS)"))
//            add(Album(R.drawable.img_album_exp3, "NEXT LEVEL", "에스파(AESPA)"))
//            add(Album(R.drawable.img_album_exp4, "MAP OF THE SOUL", "방탄소년단(BTS)"))
//            add(Album(R.drawable.img_album_exp5, "GREAT!", "모모랜드(MOMOLAND)"))
//            add(Album(R.drawable.img_album_exp6, "WEEKEND", "태연(TAEYEON)"))
//        }

        // 데이터 리스트 생성 더미 데이터
        songDB= SongDatabase.getInstance(requireContext())!!
        albumList.addAll(songDB.albumDao().getAlbums())



        // home "오늘의 음악" homeTodayAlbumRv Adapter연결
        val albumAdapter = AlbumRVAdapter(albumList)
        binding.homeTodayAlbumRv.adapter = albumAdapter

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.homeTodayAlbumRv.layoutManager = layoutManager

        albumAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener {

            override fun onItemClick(album: Album) {
                Log.d("??","onItemClicked")
                changeAlbumFragment(album)
            }

            override fun onPlayBtnClick(album: Album) {
                Log.d("??","playBtnClicked")

            }

            override fun onRemoveAlbum(position: Int) {
                albumAdapter.removeItem(position)
            }
        })


        // home "팟캐스트" mutableListOf
        val podcastDatas = mutableListOf<Album>()
        val podcast1 = Album( 0,"제목", "가수",R.drawable.img_potcast_exp)
        for (i in 1..3) {
            podcastDatas.add(podcast1)
        }

        // "팟캐스트" podcastRV와 Adapter 연결
        val podcastAdapter = PodcastRVAdapter(podcastDatas)
        binding.homePodcastRv.adapter = podcastAdapter

        val podcastLayoutManager = LinearLayoutManager(context)
        podcastLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.homePodcastRv.layoutManager = podcastLayoutManager


        // HomeFragment video collection
        val videoDatas = mutableListOf<String>()
        for (i in 1..3) {
            videoDatas.add("$i")
        }
        // podcastRV와 Adapter 연결
        val homeVideoRVAdapter = HomeVideoRVAdapter(videoDatas)
        binding.homeVideoRv.adapter = homeVideoRVAdapter


        return binding.root
    }


    private fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()
    }
    private fun changeMiniPlayerSong(album: Album){
        R.id.main_miniplayer_singer_tv
    }

    // 해당 album fragment로 이동
//    private fun changeAlbumFragment(album: Album) {
//        Log.d("??","changeAlbumFrag")
//        (context as MainActivity).supportFragmentManager.beginTransaction()
//            .replace(R.id.main_frm, AlbumFragment())
//            .commitAllowingStateLoss()
//    }


    //페이지 변경하기
    fun setPage() {
        if (currentPage == 4) currentPage = 0
        binding.homeBannerVp.setCurrentItem(currentPage, true)
        currentPage += 1
    }

    //4초 마다 배너 넘기기
    inner class PagerRunnable : Runnable {
        override fun run() {
            while (true) {
                Thread.sleep(4000)
                handler.sendEmptyMessage(0)
            }
        }
    }


}