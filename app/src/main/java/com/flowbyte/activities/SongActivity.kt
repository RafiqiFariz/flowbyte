package com.flowbyte.activities

import android.content.ComponentName
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.annotation.OptIn
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.flowbyte.R
import com.flowbyte.databinding.ActivitySongBinding
import com.flowbyte.service.PlaybackService
import com.flowbyte.ui.menu_bottom_sheet.MenuBottomSheetFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors

class SongActivity : AppCompatActivity() {
    private lateinit var _binding: ActivitySongBinding
    private lateinit var _controllerFuture: ListenableFuture<MediaController>

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(_binding.root)

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
//        hideSystemUI()
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