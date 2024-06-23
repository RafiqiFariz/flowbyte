package com.flowbyte.activities

import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flowbyte.R
import com.flowbyte.adapter.MusicAdapter
import com.flowbyte.ui.library.local_music.AudioFile
import com.google.firebase.firestore.FirebaseFirestore

class PlaylistDetailActivity : AppCompatActivity() {
    private lateinit var musicAdapter: MusicAdapter
    private val musicList = mutableListOf<AudioFile>()
    private lateinit var firestore: FirebaseFirestore
    private var playlistName: String? = null
    private val allMusicList = mutableListOf<AudioFile>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_detail)

        firestore = FirebaseFirestore.getInstance()

        playlistName = intent.getStringExtra("playlist_name")
        val textView = findViewById<TextView>(R.id.playlist_name_text_view)
        textView.text = playlistName

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_music)
        recyclerView.layoutManager = LinearLayoutManager(this)
        musicAdapter = MusicAdapter(musicList, ::playAudioFile, ::removeAudioFile)
        recyclerView.adapter = musicAdapter

        val btnAddMusic = findViewById<Button>(R.id.btn_add_music)
        btnAddMusic.setOnClickListener {
            showSelectMusicDialog()
        }

        // Load all music from local storage
        loadAllMusicFromLocalStorage()

        // Load music from Firestore
        loadMusicFromFirestore()
    }

    private fun showSelectMusicDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_select_music, null)
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.recyclerView_select_music)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val selectMusicAdapter = MusicAdapter(allMusicList, { audioFile ->
            // Add selected song to the playlist
            musicList.add(audioFile)
            musicAdapter.notifyDataSetChanged()
            saveMusicToFirestore(audioFile)
            Toast.makeText(this, "Music added to playlist", Toast.LENGTH_SHORT).show()
        }, { audioFile ->
            // Do nothing on long click
        })
        recyclerView.adapter = selectMusicAdapter

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        val dialog = builder.create()

        dialogView.findViewById<Button>(R.id.btn_done).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun loadAllMusicFromLocalStorage() {
        allMusicList.clear()
        allMusicList.addAll(getAllMp3Files())
    }

    private fun getAllMp3Files(): List<AudioFile> {
        val audioList = mutableListOf<AudioFile>()
        val contentResolver: ContentResolver = contentResolver
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST
        )
        val selection = "${MediaStore.Audio.Media.MIME_TYPE}=?"
        val selectionArgs = arrayOf("audio/mpeg")
        val cursor = contentResolver.query(uri, projection, selection, selectionArgs, null)
        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val title = it.getString(titleColumn)
                val artist = it.getString(artistColumn)
                val contentUri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id.toString())
                audioList.add(AudioFile(contentUri, title, artist))
            }
        }
        return audioList
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
                Toast.makeText(this, "Music saved to Firestore", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save music to Firestore", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this, "Failed to load music from Firestore", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeAudioFile(audioFile: AudioFile) {
        // Hapus lagu dari playlist
        musicList.remove(audioFile)
        musicAdapter.notifyDataSetChanged()
        // Hapus lagu dari Firestore
        if (playlistName == null) return
        firestore.collection("playlists")
            .document(playlistName!!)
            .collection("songs")
            .whereEqualTo("uri", audioFile.uri.toString())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    document.reference.delete()
                }
                Toast.makeText(this, "Music removed from Firestore", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to remove music from Firestore", Toast.LENGTH_SHORT).show()
            }
    }
}
