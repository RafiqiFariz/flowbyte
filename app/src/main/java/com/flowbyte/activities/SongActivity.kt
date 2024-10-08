package com.flowbyte.activities

import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.OptIn
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.bumptech.glide.Glide
import com.flowbyte.R
import com.flowbyte.databinding.ActivitySongBinding
import com.flowbyte.service.PlaybackService
import com.flowbyte.ui.home.HomeViewModel
import com.flowbyte.ui.menu_bottom_sheet.MenuBottomSheetFragment
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SongActivity : AppCompatActivity() {
    private lateinit var _binding: ActivitySongBinding
    private lateinit var _controllerFuture: ListenableFuture<MediaController>
    private lateinit var songUri: String
    private lateinit var songImg: String
    private lateinit var songTitle: String
    private lateinit var songArtist: String
    private lateinit var homeViewModel: HomeViewModel

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        songImg = intent.getStringExtra("song_img")!!
        songTitle = intent.getStringExtra("song_title")!!
        songArtist = intent.getStringExtra("song_artists")!!
        songUri = intent.getStringExtra("song_uri")!!

        _binding.textView.text = songTitle
        _binding.artistName.text = songArtist

        Glide.with(this).load(songImg).into(_binding.imageView)

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

        val playbackServiceIntent = Intent(this, PlaybackService::class.java).apply {
            putExtra("song_img", songImg)
            putExtra("song_uri", songUri)
            putExtra("song_name", songTitle)
            putExtra("song_artist", songArtist)
        }

        startService(playbackServiceIntent)

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
