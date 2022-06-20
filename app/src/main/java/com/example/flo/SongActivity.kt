package com.example.flo

import android.content.Context
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.flo.data.entities.Song
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson
import java.io.InterruptedIOException

class SongActivity : AppCompatActivity() {
    lateinit var binding: ActivitySongBinding

    // lateinit var song: Song
    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null
    private var gson: Gson = Gson()

    private val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    private var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayList()
        initSong()
        initClickListener()
        //setPlayer(song)


        // 한곡재생 버튼 클릭시 이벤트
        binding.btnSongUnrepeatIv.setOnClickListener {
            repeatedStatus(true)
            Log.d("repeatBtn", "unRepeat : $songs[nowPos].isRepeated")
        }
        binding.btnSongRepeatIv.setOnClickListener {
            repeatedStatus(false)
            Log.d("repeatBtn", "repeat : $songs[nowPos].isRepeated")
        }


        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
            binding.songTitleTv.text = intent.getStringExtra("title")
            binding.songSingerTv.text = intent.getStringExtra("singer")
        }
    }

    override fun onStart() {
        super.onStart()
        // sharedPreference의 이름 -> song
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        // 그안에 있는 song data 이름 -> songData
        val songJson = sharedPreferences.getString("songData", null)

        songs[nowPos] = if (songJson == null) {
            Song("라일락", "아이유(IU)", 0, 60, false, false, "music_lilac")
        } else {
            gson.fromJson(songJson, Song::class.java)
        }
        Log.d("??","songJSong : ${songJson}")
        Log.d("??","song nowPos : ${songs[nowPos]}")
        Log.d("??","song nowPos db : ${songDB.songDao().getSong(nowPos)}")
        //setPlayer(songs[nowPos])
        setPlayer(songDB.songDao().getSong(nowPos+1))
        Log.d("lifeCycle", "songActivity onStart:${songJson}")
    }


    // 사용자가 포커스를 잃었을때 음악이 중지
    override fun onPause() {
        Log.d("lifeCycle", "song Activity onPause")
        super.onPause()
        setPlayerStatus(false)

        songs[nowPos].second =
            ((binding.songProgressBar.progress * songs[nowPos].playTime) / 100) / 1000

        // song data를 내부저장소에 저장하는 작업
        val sharedPreference = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreference.edit() // 에디터
        val songJson = gson.toJson(songs[nowPos])
        editor.putInt("songId", songs[nowPos].id)
        //editor.putString("songData",songJson)
        editor.apply()

        Log.d("lifeCycle", "songActivity onPause:$songJson")
        Log.d(
            "lifeCycle",
            "songActivity onPause:${(((songs[nowPos].second * 1000.0) / songs[nowPos].playTime) * 100).toInt()}"
        )

    }

    override fun onDestroy() {
        Log.d("lifeCycle", "song Activity onDestroy")
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제
    }

    private fun initPlayList() {
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun initClickListener() {

        binding.btnPageDownIv.setOnClickListener {
            finish()
        }

        // play버튼을 눌렀을때 pause버튼이 보이도록
        binding.btnSongPlayIv.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.btnSongPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }
        binding.btnSongNextIv.setOnClickListener {
            moveSong(+1)
        }
        binding.btnSongPreviousIv.setOnClickListener {
            moveSong(-1)
        }
        binding.btnLikeIv.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }
    }

    private fun setLike(isLike:Boolean){
        songs[nowPos].isLike=!isLike
        songDB.songDao().updateIsLikeById(!isLike,songs[nowPos].id)

        if(!isLike){
            binding.btnLikeIv.setImageResource(R.drawable.ic_my_like_on)
            Log.d("??","like")
        }
        else{
            binding.btnLikeIv.setImageResource(R.drawable.ic_my_like_off)
            Log.d("??","unlike")
        }
    }

    private fun moveSong(direct: Int) {
        if(nowPos+direct<0){
            Toast.makeText(this,"first song",Toast.LENGTH_SHORT).show()
            return
        }
        if(nowPos+direct>=songs.size){
            Toast.makeText(this,"Last song",Toast.LENGTH_SHORT).show()
            return
        }

        nowPos+=direct

        timer.interrupt()
        startTimer()

        mediaPlayer?.release()
        mediaPlayer=null

        setPlayer(songs[nowPos])
    }

    private fun initSong() {
//        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
//
//            song = Song(
//                intent.getStringExtra("title")!!,
//                intent.getStringExtra("singer")!!,
//                intent.getIntExtra("second", 0),
//                intent.getIntExtra("playTime", 0),
//                intent.getBooleanExtra("isPlaying", false),
//                intent.getBooleanExtra("isRepeated", false),
//                intent.getStringExtra("music")!!
//            )
//        }
        val spf = getSharedPreferences("song", Context.MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)

        Log.d("now Song Id", songs[nowPos].id.toString())
        startTimer()
        setPlayer(songs[nowPos])
    }

    private fun getPlayingSongPosition(songId: Int): Int {
        for (i in 0 until songs.size) {
            if (songs[i].id == songId) {
                return i
            }
        }
        return 0
    }

    private fun setPlayer(song: Song) {
        binding.songTitleTv.text = song.title
        binding.songSingerTv.text = song.singer
        binding.progressTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.endTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songProgressBar.progress = (song.second * 100000) / song.playTime
//        binding.songProgressBar.progress = (song.second * 1000 / song.playTime)
        binding.songCoverIv.setImageResource(song.coverImg!!)
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)
        mediaPlayer?.seekTo(song.second * 1000)

        if(song.isLike){
            binding.btnLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }
        else{
            binding.btnLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
        setPlayerStatus(song.isPlaying)
    }

    // 재생, 일시정지 버튼
    fun setPlayerStatus(isPlaying: Boolean) {
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying
        if (isPlaying) {
            binding.btnSongPlayIv.visibility = View.GONE
            binding.btnSongPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        } else {
            Log.d("song", "${songs[nowPos].second}")
            binding.btnSongPlayIv.visibility = View.VISIBLE
            binding.btnSongPauseIv.visibility = View.GONE
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        }
    }

    // 한곡재생 버튼
    fun repeatedStatus(isRepeated: Boolean) {
        songs[nowPos].isRepeated = isRepeated

        if (isRepeated) {
            binding.btnSongUnrepeatIv.visibility = View.GONE
            binding.btnSongRepeatIv.visibility = View.VISIBLE
        } else {
            binding.btnSongUnrepeatIv.visibility = View.VISIBLE
            binding.btnSongRepeatIv.visibility = View.GONE
        }
    }

    private fun startTimer() {
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer.start()
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true) : Thread() {
        private var second: Int = songs[nowPos].second
        private var mills: Float = 0f
        var progress: Int = (songs[nowPos].second * 100000) / songs[nowPos].playTime
        override fun run() {
            super.run()
            try {
                while (true) {
                    if (second >= playTime) {
                        if (songs[nowPos].isRepeated) {
                            second = 0
                            mills = 0f
                            binding.songProgressBar.progress = ((mills / playTime) * 100).toInt()
                            continue
                        } else break
                    }
                    if (isPlaying) {
                        sleep(50)
                        mills += 50
//                        runOnUiThread {
//                            progress+=(((second*1000.0) / playTime) * 100).toInt()
//                            binding.songProgressBar.progress = progress
//                        }
                        runOnUiThread {
                            binding.songProgressBar.progress = ((mills / playTime) * 100).toInt()
                        }
                        if (mills % 1000 == 0f) {
                            runOnUiThread {
                                binding.progressTimeTv.text =
                                    String.format("%02d:%02d", second / 60, second % 60)
                            }
                            second++
                        }
                    }
                }
            } catch (e: InterruptedIOException) {
                Log.d("Song2", "쓰레드가 죽었습니다 ${e.message}")
            }

        }
    }

}