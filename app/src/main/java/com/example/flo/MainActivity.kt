package com.example.flo

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.flo.data.entities.Album
import com.example.flo.data.entities.Song
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson
import java.io.InterruptedIOException

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var timer: Timer
    var isPlaying: Boolean = false
    private var mediaPlayer: MediaPlayer? = null

    private var song: Song = Song()
    private var gson: Gson = Gson()


    private val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    private var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_FLO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputDummySongs()
        inputDummyAlbums()

        initPlayList()
        initSong()
        initClickListener()

        startTimer()

        // sharedPreference를 사용하면서 이부분도 필요없어짐!
//        val song = Song(
//            binding.mainMiniplayerTitleTv.text.toString(),
//            binding.mainMiniplayerSingerTv.text.toString(),
//            0, 60, false, false, "music_lilac"
//        )





        binding.mainPlayerCl.setOnClickListener {
            //startActivity(Intent(this,SongActivity::class.java))
//            val intent = Intent(this, SongActivity::class.java)
//            intent.putExtra("title", song.title)
//            intent.putExtra("singer", song.singer)
//            intent.putExtra("second", song.second)
//            intent.putExtra("playTime", song.playTime)
//            intent.putExtra("isPlaying", song.isPlaying)
//            intent.putExtra("music", song.music)
//            startActivity(intent)

            val editor = getSharedPreferences("song", Context.MODE_PRIVATE).edit()
            editor.putInt("songId",song.id)
            editor.apply()

            val intent = Intent(this,SongActivity::class.java)
            startActivity(intent)
        }

   //     Log.d("MAIN/JWT_TO_SERVER",getJwt().toString())

        initBottomNavigation()


        Log.d("Song", song.title + song.singer)


    }

    private fun initPlayList() {
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    fun setMiniPlayerStatus(isPlaying: Boolean) {
        this.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if (isPlaying) {
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
            mediaPlayer?.start()
        } else {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        }
    }

    // timer 시작하는 함수
    private fun startTimer() {
        timer = Timer(60, isPlaying)
        timer.start()
    }

    // 미니플레이어 Timer
    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true) : Thread() {
        private var second: Int = 0
        private var mills: Float = 0f


        override fun run() {
            super.run()
            try {
                while (true) {
                    if (second >= playTime) {
                        break
                    }
                    if (isPlaying) {
                        sleep(50)
                        mills += 50
//                        runOnUiThread {
//                            binding.mainMiniplayerProgress.progress = (((second * 1000.0) / playTime) * 100).toInt()
//                        }
                        runOnUiThread {
                            binding.mainMiniplayerProgress.progress =
                                ((mills / playTime) * 100).toInt()
                        }
                        if (mills % 1000 == 0f) {
                            second++
                        }
                    }
//                    if (isPlaying) {
//                        sleep(50)
//                        mills += 50
//                        runOnUiThread {
//                            Log.d("lifeCycle","${song.playTime}")
//                            Log.d("lifeCycle","progress : ${binding.mainMiniplayerProgress.progress}")
//                            binding.mainMiniplayerProgress.progress =
//                                ((song.second * 100000) / song.playTime)+((mills / playTime) * 100).toInt()
//                        }
//                        if (mills % 1000 == 0f) {
//                            second++
//                        }
//                    }
                }
            } catch (e: InterruptedIOException) {
                Log.d("Song2", "쓰레드가 죽었습니다 ${e.message}")
            }

        }
    }

    private fun initBottomNavigation() {

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    private fun getJwt():String?{
        val spf = this?.getSharedPreferences("auth2", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt", "")

    }

    override fun onStart() {
        super.onStart()
//        // sharedPreference의 이름 -> song
//        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
//        // 그안에 있는 song data 이름 -> songData
//        val songJson = sharedPreferences.getString("songData", null)
//
//        song = if (songJson == null) {
//            Song("라일락", "아이유(IU)", 0, 60, false, false, "music_lilac")
//        } else {
//            gson.fromJson(songJson, Song::class.java)
//        }

        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId",0)

        val songDB = SongDatabase.getInstance(this)!!
        song=if(songId==0){
            songDB.songDao().getSong(1)
        }else{
            songDB.songDao().getSong(songId)
        }
        Log.d("miniPlayerSongId",song.id.toString())
        setMiniPlayer(song)

         }

    // 사용자가 포커스를 잃었을때 음악이 중지
    override fun onPause() {
        Log.d("lifeCycle", "main Activity onPause")
        super.onPause()
        setMiniPlayerStatus(false)
//        setPlayerStatus(false)

        song.second = ((binding.mainMiniplayerProgress.progress * song.playTime) / 100) / 1000

        // song data를 내부저장소에 저장하는 작업
        val sharedPreference = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreference.edit() // 에디터
        val songJson = gson.toJson(song)
        editor.putString("songData", songJson)
        editor.apply()

        Log.d("lifeCycle", "mainActivity onPause:$songJson")
        Log.d(
            "lifeCycle",
            "mainActivity onPause:${(((song.second * 1000.0) / song.playTime) * 100).toInt()}"
        )

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("lifeCycle", "MainActivity onDestroy")
    }

    private fun initSong() {
        val spf = getSharedPreferences("song", Context.MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)

        Log.d("now Song Id", songs[nowPos].id.toString())
        startTimer()
        setMiniPlayer(songs[nowPos])
    }

    private fun getPlayingSongPosition(songId: Int): Int {
        for (i in 0 until songs.size) {
            if (songs[i].id == songId) {
                return i
            }
        }
        return 0
    }

    private fun initClickListener() {

        // play버튼을 눌렀을때 pause버튼이 보이도록
        // 미니 플레이어 재생/일시정지 버튼 클릭시 이벤트
        binding.mainMiniplayerBtn.setOnClickListener {
            setMiniPlayerStatus(true)
            Log.d("miniplayer", "playing : $isPlaying")
            Log.d("lifeCycle", "$song")
        }
        binding.mainPauseBtn.setOnClickListener {
            setMiniPlayerStatus(false)
            Log.d("miniplayer", "paused : $isPlaying , ${song.second}")
            Log.d("lifeCycle", "$song")
        }
        binding.mainNextSongBtn.setOnClickListener {
            moveSong(+1)
        }
        binding.mainPreviousSongBtn.setOnClickListener {
            moveSong(-1)
        }

    }


    private fun moveSong(direct: Int) {
        if(nowPos+direct<0){
            Toast.makeText(this,"first song", Toast.LENGTH_SHORT).show()
            return
        }
        if(nowPos+direct>=songs.size){
            Toast.makeText(this,"Last song", Toast.LENGTH_SHORT).show()
            return
        }

        nowPos+=direct

        timer.interrupt()
        startTimer()

        mediaPlayer?.release()
        mediaPlayer=null

        setMiniPlayer(songs[nowPos])
    }


    private fun setMiniPlayer(song: Song) {
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainMiniplayerProgress.progress = (song.second * 100000) / song.playTime

        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)
        Log.d("lifeCycle", "set miniplayer seekbar : ${song.second} , ${song.second * 1000}")
        mediaPlayer?.seekTo(song.second * 1000)
        Log.d("lifeCycle", "$mediaPlayer")
//(song.second*100000)/song.playTime
        Log.d("lifeCycle", "MainActivity miniplayer:${song}")
        Log.d(
            "lifeCycle",
            "MainActivity miniplayer progress:${(song.second * 100000) / song.playTime}"
        )

        setMiniPlayerStatus(song.isPlaying)

    }

    private fun inputDummySongs() {
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.songDao().getSongs()

        if (albums.isNotEmpty()) return

        songDB.songDao().insert(
            Song(
                "Lilac",
                "아이유(IU)",
                0,
                60,
                false,
                false,
                "music_lilac",
                R.drawable.img_album_cover,
                false
            )
        )
        songDB.songDao().insert(
            Song(
                "Butter",
                "방탄소년단(BTS)",
                0,
                60,
                false,
                false,
                "music_butter",
                R.drawable.img_album_exp,
                false
            )
        )
        songDB.songDao().insert(
            Song(
                "bboom",
                "모모랜드(MOMOLAND)",
                0,
                60,
                false,
                false,
                "music_bboom",
                R.drawable.img_album_exp5,
                false
            )
        )
        val _songs = songDB.songDao().getSongs()
        Log.d("DB", _songs.toString())

    }
    //ROOM_DB
    private fun inputDummyAlbums() {
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.albumDao().getAlbums()

        if (albums.isNotEmpty()) return

        songDB.albumDao().insert(
            Album(
                0,
                "IU 5th Album 'LILAC'", "아이유 (IU)", R.drawable.img_album_exp2
            )
        )

        songDB.albumDao().insert(
            Album(
                1,
                "Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp
            )
        )

        songDB.albumDao().insert(
            Album(
                2,
                "iScreaM Vol.10 : Next Level Remixes", "에스파 (AESPA)", R.drawable.img_album_exp3
            )
        )

        songDB.albumDao().insert(
            Album(
                3,
                "MAP OF THE SOUL : PERSONA", "방탄소년단 (BTS)", R.drawable.img_album_exp4
            )
        )

        songDB.albumDao().insert(
            Album(
                4,
                "GREAT!", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5
            )
        )

    }

}