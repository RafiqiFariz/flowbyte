package com.flowbyte.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flowbyte.R
import com.flowbyte.adapter.MusicAdapter
import com.flowbyte.ui.libraryLocalMusic.AudioFile
import com.google.firebase.firestore.FirebaseFirestore

class PlaylistDetailActivity : AppCompatActivity() {
    private lateinit var musicAdapter: MusicAdapter
    private val musicList = mutableListOf<AudioFile>()
    private lateinit var firestore: FirebaseFirestore
    private var playlistName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_detail)

        firestore = FirebaseFirestore.getInstance()

        playlistName = intent.getStringExtra("playlist_name")
        val textView = findViewById<TextView>(R.id.playlist_name_text_view)
        textView.text = playlistName

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_music)
        recyclerView.layoutManager = LinearLayoutManager(this)
        musicAdapter = MusicAdapter(musicList) { audioFile ->
            playAudioFile(audioFile)
        }
        recyclerView.adapter = musicAdapter

        val btnAddMusic = findViewById<Button>(R.id.btn_add_music)
        btnAddMusic.setOnClickListener {
            pickAudioFile()
        }

        loadMusicFromFirestore()
    }

    private fun pickAudioFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "audio/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        filePickerLauncher.launch(intent)
    }

    private val filePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.also { uri ->
                val audioFile = getAudioFileDetails(uri)
                if (audioFile != null) {
                    musicList.add(audioFile)
                    musicAdapter.notifyDataSetChanged()
                    saveMusicToFirestore(audioFile)
                }
            }
        }
    }

    private fun getAudioFileDetails(uri: Uri): AudioFile? {
        var title: String? = null
        var artist: String? = null
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val titleIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (titleIndex != -1) {
                    title = cursor.getString(titleIndex)
                }
            }
        }
        return if (title != null) {
            AudioFile(uri, title!!, artist ?: "Unknown Artist")
        } else {
            null
        }
    }

    private fun playAudioFile(audioFile: AudioFile) {
        val intent = Intent(this, SongActivity::class.java).apply {
            putExtra("song_uri", audioFile.uri.toString())
            putExtra("song_name", audioFile.title)
            putExtra("song_artist", audioFile.artist)
        }
        startActivity(intent)
    }

    private fun saveMusicToFirestore(audioFile: AudioFile) {
        if (playlistName == null) return
        val musicData = hashMapOf(
            "uri" to audioFile.uri.toString(),
            "title" to audioFile.title,
            "artist" to audioFile.artist
        )
        firestore.collection("playlists")
            .document(playlistName!!)
            .collection("songs")
            .add(musicData)
            .addOnSuccessListener { documentReference ->
                // Handle success
            }
            .addOnFailureListener { e ->
                // Handle failure
            }
    }

    private fun loadMusicFromFirestore() {
        if (playlistName == null) return
        firestore.collection("playlists")
            .document(playlistName!!)
            .collection("songs")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val uri = Uri.parse(document.getString("uri"))
                    val title = document.getString("title") ?: "Unknown Title"
                    val artist = document.getString("artist") ?: "Unknown Artist"
                    val audioFile = AudioFile(uri, title, artist)
                    musicList.add(audioFile)
                }
                musicAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle failure
            }
    }
}
