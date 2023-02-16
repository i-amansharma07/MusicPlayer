package com.example.musixhops.main

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import android.widget.Toast
import com.example.musixhops.R
import com.example.musixhops.databinding.ActivityPlayerBinding
import com.example.musixhops.main.model.MusicModel
import java.util.concurrent.TimeUnit



/*

    1.foreground services and mini player left
    2. forward and backward button implementation

 */

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var runnable: Runnable
    private  var isPlaying = true
    companion object{
        var pMusicList  = ArrayList<MusicModel>()
        var position = 0
        var mediaPlayer:MediaPlayer? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = ActivityPlayerBinding.inflate(layoutInflater)
        binding = view
        setContentView(view.root)

        pMusicList.addAll(MainActivity.musicList)

        position = intent.getIntExtra("position",0)

        setLayout()

        if(mediaPlayer == null){
           createMediaPlayer()
        }

        seekbarSetup()

        binding.next.setOnClickListener {
            mediaPlayer!!.reset()
            prevNextButton(true)
        }

        binding.prev.setOnClickListener {
            mediaPlayer!!.reset()
            prevNextButton(false)
        }

        binding.pause.setOnClickListener {
            if(isPlaying){
                //call pause button
                pauseMusic()
            }else{
                playMusic()
            }
        }


        binding.revert.setOnClickListener {
            Toast.makeText(this, "Pending work", Toast.LENGTH_SHORT).show()
        }
        binding.frwd.setOnClickListener {
            Toast.makeText(this, "Pending work", Toast.LENGTH_SHORT).show()
        }


        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, fromUser: Boolean) {

                if(fromUser) mediaPlayer!!.seekTo(p1)

            }

            override fun onStartTrackingTouch(p0: SeekBar?) = Unit

            override fun onStopTrackingTouch(p0: SeekBar?) = Unit
        })


    }

    private fun prevNextButton(increment: Boolean) {
        if(increment){
            setPosition(increment)
            setLayout()
            createMediaPlayer()

        }else{
            setPosition(increment)
            setLayout()
            createMediaPlayer()
        }

    }

    private fun setPosition(b: Boolean) {
        if(b){
            if (position== pMusicList.size-1){
                position = 0
            }else{
               ++position
            }
        }else{
            if (0== position){
                position = pMusicList.size-1
            }else{
                --position
            }
        }
    }


    private fun playMusic() {
        binding.pause.setImageResource(R.drawable.pause)
        isPlaying = true
        mediaPlayer!!.start()
    }

    private fun pauseMusic() {
        binding.pause.setImageResource(R.drawable.play)
        isPlaying = false
        mediaPlayer!!.pause()
    }

    private fun createMediaPlayer() {
        try{
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.reset()
            mediaPlayer!!.setDataSource(pMusicList[position].path)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
            binding.startTv.text = formatDuration(mediaPlayer!!.currentPosition.toLong())
            binding.endTv.text = formatDuration(mediaPlayer!!.duration.toLong())
            binding.seekbar.progress = 0
            binding.seekbar.max = mediaPlayer!!.duration
        }catch (e:Exception){return}

    }

    private fun setLayout(){
        val music = pMusicList[position]
        if(music.title.length>10){
            val temp= music.title.subSequence(0,10)
            binding.title.text =   StringBuilder().append(temp).append("...").toString()
        }else{
            binding.title.text = music.title
        }
    }

    private fun formatDuration(d: Long):String{
        val minutes = TimeUnit.MINUTES.convert(d, TimeUnit.MILLISECONDS)
        val seconds = (TimeUnit.SECONDS.convert(d, TimeUnit.MILLISECONDS) -
                minutes*TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mediaPlayer!!.reset()
        mediaPlayer = null
        this.finish()
    }

    private fun seekbarSetup(){
        runnable = Runnable {
            if(mediaPlayer!=null){
                binding.startTv.text = formatDuration(mediaPlayer!!.currentPosition.toLong())
                binding.seekbar.progress = mediaPlayer!!.currentPosition
                Handler(Looper.getMainLooper()).postDelayed(runnable,200)
            }
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable,0)
    }

}