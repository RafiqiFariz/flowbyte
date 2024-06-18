package com.flowbyte.activities

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.OptIn
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.flowbyte.R
import com.flowbyte.databinding.ActivitySongBinding
import com.flowbyte.service.PlaybackService
import com.flowbyte.ui.menu_bottom_sheet.MenuBottomSheetFragment
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors

class SongActivity : AppCompatActivity() {
    private lateinit var _binding: ActivitySongBinding
    private lateinit var _controllerFuture: ListenableFuture<MediaController>
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var songUri: String
    private lateinit var songTitle: String
    private lateinit var songArtist: String

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        // Initialize ExoPlayer
        exoPlayer = ExoPlayer.Builder(this).build()
        _binding.playerControlView.player = exoPlayer

        // Get song URI from intent
        songUri = intent.getStringExtra("song_uri") ?: return
        songTitle = intent.getStringExtra("song_name")!!
        songArtist = intent.getStringExtra("song_artist")!!

        val textView = findViewById<View>(R.id.textView) as TextView
        textView.text = songTitle

        val artistNameTextView = findViewById<View>(R.id.artistName) as TextView
        artistNameTextView.text = songArtist
//        val mediaItem = MediaItem.fromUri(Uri.parse(songUri))
//        exoPlayer.setMediaItem(mediaItem)
//        exoPlayer.prepare()
//        exoPlayer.playWhenReady = true

        // Start PlaybackService with the song URI
        val playbackServiceIntent = Intent(this, PlaybackService::class.java).apply {
            putExtra("song_uri", songUri)
            putExtra("song_name", songTitle)
            putExtra("song_artist", songArtist)
        }
        startService(playbackServiceIntent)

        // ini cuma buat testing
        // dropdown playing from playlist
        val playlistItems = arrayOf("Rock", "Pop", "Jazz", "Classical", "Hip-Hop")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, playlistItems)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        _binding.playlistSpinner.adapter = adapter

        _binding.btnSongSettings.setOnClickListener {
            val menuBottomSheet = MenuBottomSheetFragment()
            menuBottomSheet.show(supportFragmentManager, menuBottomSheet.tag)
        }

        // hideSystemUI() // Uncomment this line to hide the system UI
    }

    @OptIn(UnstableApi::class)
    override fun onStart() {
        super.onStart()
        val sessionToken = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        _controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        _controllerFuture.addListener(
            { _binding.playerControlView.player = _controllerFuture.get() },
            MoreExecutors.directExecutor()
        )
    }

    override fun onStop() {
        super.onStop()
        MediaController.releaseFuture(_controllerFuture)
        exoPlayer.release()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, _binding.playerControlView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}
