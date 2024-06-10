package com.flowbyte.ui.song

import android.content.ContentResolver
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.flowbyte.R
import com.flowbyte.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    private var _binding: ActivitySongBinding? = null
    private lateinit var _player: ExoPlayer

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(_binding?.root)

        _player = ExoPlayer.Builder(this)
            .setAudioAttributes(AudioAttributes.DEFAULT, true)
            .build()
        _binding?.playerControlView?.player = _player

//        val mediaItem = MediaItem.fromUri("https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/000/936/royalty-1619082033-7RC2AlRdd1.mp3")
        val dummyMusic = Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(packageName)
            .path(R.raw.royalty.toString())
            .build()
        val mediaItem = MediaItem.fromUri(dummyMusic)
        _player.setMediaItem(mediaItem)
        _player.prepare()
        _player.play()

        _player.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                Toast.makeText(applicationContext, "Error playing media", Toast.LENGTH_SHORT).show()
                super.onPlayerError(error)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        _player.playWhenReady = true
    }

    override fun onStop() {
        super.onStop()
        _player.playWhenReady = false
    }

    override fun onDestroy() {
        super.onDestroy()
        _player.release()
    }
}