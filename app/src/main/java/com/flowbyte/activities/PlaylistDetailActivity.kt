package com.flowbyte.activities

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flowbyte.R
import com.flowbyte.adapter.MusicAdapter
import com.flowbyte.ui.libraryLocalMusic.AudioFile

class PlaylistDetailActivity : AppCompatActivity() {
    private lateinit var musicAdapter: MusicAdapter
    private val musicList = mutableListOf<AudioFile>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_detail)

        val playlistName = intent.getStringExtra("playlist_name")
        val textView = findViewById<TextView>(R.id.playlist_name_text_view)
        textView.text = playlistName

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_music)
        recyclerView.layoutManager = LinearLayoutManager(this)
        musicAdapter = MusicAdapter(musicList) { audioFile ->
            // Handle item click
        }
        recyclerView.adapter = musicAdapter

        val btnAddMusic = findViewById<Button>(R.id.btn_add_music)
        btnAddMusic.setOnClickListener {
            pickAudioFile()
        }
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
}
