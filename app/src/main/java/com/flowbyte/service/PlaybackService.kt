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

    override fun onCreate() {
        super.onCreate()
    }

    @OptIn(UnstableApi::class) private fun initializeSessionAndPlayer(songUri: String, songName: String, songArtist: String) {
        val player = ExoPlayer.Builder(this).build()

        mediaSession = MediaSession.Builder(this, player).build()
        Log.d("PlaybackService1", "initializeSessionAndPlayer: $songUri")
        Log.d("PlaybackService2", "initializeSessionAndPlayer: $songName")
        Log.d("PlaybackService3", "initializeSessionAndPlayer: $songArtist")
        val mediaItem = MediaItem.Builder()
            .setUri(songUri)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(songName) // You can modify this to set actual title and artist
                    .setArtist(songArtist) // You can modify this to set actual title and artist
                    .build()
            ).build()

        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val songUri = intent?.getStringExtra("song_uri")
        val songName = intent?.getStringExtra("song_name")
        val songArtist = intent?.getStringExtra("song_artist")
        if (songUri != null) {
            if (songName != null) {
                if (songArtist != null) {
                    initializeSessionAndPlayer(songUri, songName, songArtist)
                }
            }
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
