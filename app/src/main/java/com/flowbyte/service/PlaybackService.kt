package com.flowbyte.service

import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

class PlaybackService : MediaSessionService() {
    private var mediaSession: MediaSession? = null

    @OptIn(UnstableApi::class)
    fun initializeSessionAndPlayer(songUri: String, songName: String, songArtist: String) {
        val audioItem = MediaItem.Builder()
            .setUri(songUri)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(songName)
                    .setArtist(songArtist)
                    .build()
            ).build()

        val player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            exoPlayer.setMediaItem(audioItem)
            exoPlayer.prepare()
            exoPlayer.play()
        }

        mediaSession = MediaSession.Builder(this, player).build()
        Log.d("Test Bro", mediaSession.toString())
        Log.d("Test Bro 2", mediaSession?.token.toString())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val songUri = intent?.getStringExtra("song_uri")
        val songName = intent?.getStringExtra("song_name")
        val songArtist = intent?.getStringExtra("song_artist")
        if (songUri != null && songName != null && songArtist != null) {
            initializeSessionAndPlayer(songUri, songName, songArtist)
        }
        return START_STICKY
    }

    // The user dismissed the app from the recent tasks
    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = mediaSession?.player!!

        if (player.playWhenReady) {
            // Make sure the service is not in foreground.
            player.pause()
        }
        stopSelf()
    }

    @OptIn(UnstableApi::class)
    override fun onUpdateNotification(session: MediaSession, startInForegroundRequired: Boolean) {
        super.onUpdateNotification(session, startInForegroundRequired)

        if (!startInForegroundRequired) {
            mediaSession?.player?.pause()
            stopSelf()
        }
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
}
