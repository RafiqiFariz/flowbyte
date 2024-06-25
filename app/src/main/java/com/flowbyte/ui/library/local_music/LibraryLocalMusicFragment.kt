package com.flowbyte.ui.library.local_music

import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.flowbyte.activities.SongActivity
import com.flowbyte.adapter.MusicAdapter
import com.flowbyte.data.LibraryMenuItem
import com.flowbyte.ui.library.playlist_music.LibraryPlaylistMusicFragment
import com.flowbyte.R
import com.flowbyte.adapter.RecyclerNavLibraryAdapter
import com.flowbyte.databinding.FragmentLibraryLocalMusicBinding

class LibraryLocalMusicFragment : Fragment() {

    private var _binding: FragmentLibraryLocalMusicBinding? = null
    private lateinit var musicAdapter: MusicAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val libraryLocaMusicViewModel =
            ViewModelProvider(this).get(LibraryLocaMusicViewModel::class.java)

        _binding = FragmentLibraryLocalMusicBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val navLibraryMenu = listOf(
            LibraryMenuItem("Local Music"),
            LibraryMenuItem("Playlist")
        )

        val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recylerNavLibrary.layoutManager = linearLayoutManager
        val adapter = RecyclerNavLibraryAdapter(navLibraryMenu) { menuItem ->
            Log.d("LibraryLocalMusicFragment", "Item clicked: $menuItem")
            handleNavItemClick(menuItem.name)
        }
        binding.recylerNavLibrary.adapter = adapter

        // Set up RecyclerView for music files
        val musicLayoutManager = LinearLayoutManager(requireContext())
        binding.recylerPlaylist.layoutManager = musicLayoutManager
        musicAdapter = MusicAdapter(getAllMp3Files(), { audioFile ->
            // Handle item click: Open SongActivity and pass selected song URI
            val intent = Intent(requireContext(), SongActivity::class.java)
            intent.putExtra("song_uri", audioFile.uri.toString())
            intent.putExtra("song_name", audioFile.title)
            intent.putExtra("song_artist", audioFile.artist)
            startActivity(intent)
        }, { audioFile ->
            // Handle item long click: Perform the desired action on long click
            // Example: Show a toast message or a dialog
            Log.d("LibraryLocalMusicFragment", "Item long clicked: ${audioFile.title}")
            // You can add further actions here if needed
        })
        binding.recylerPlaylist.adapter = musicAdapter

        return root
    }

    private fun handleNavItemClick(menuItem: String) {
        val fragment = when (menuItem) {
            "Local Music" -> LibraryLocalMusicFragment()
            "Playlist" -> LibraryPlaylistMusicFragment()
            else -> null
        }
        Log.d("LibraryLocalMusicFragment", "Item clicked: $fragment")
        fragment?.let {
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.nav_host_fragment_activity_main, it) // Ensure you have a container in your layout
                .addToBackStack(null)
                .commit()
        }
    }

    private fun getAllMp3Files(): List<AudioFile> {
        val audioList: MutableList<AudioFile> = mutableListOf()
        Log.d("LibraryLocalMusicFragment", "Fetching all MP3 files")

        // Helper function to query the MediaStore and add results to the list
        fun queryMediaStore(uri: Uri, folderPath: String? = null) {
            val contentResolver: ContentResolver = requireActivity().contentResolver
            val projection: Array<String> = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA // Add DATA to get the file path
            )
            val selection = "${MediaStore.Audio.Media.MIME_TYPE}=?"
            val selectionArgs = arrayOf("audio/mpeg")
            val cursor: Cursor? = contentResolver.query(uri, projection, selection, selectionArgs, null)
            cursor?.use {
                val idColumn: Int = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val titleColumn: Int = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val artistColumn: Int = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val dataColumn: Int = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA) // File path

                while (it.moveToNext()) {
                    val id: Long = it.getLong(idColumn)
                    val title: String = it.getString(titleColumn)
                    val artist: String = it.getString(artistColumn)
                    val data: String = it.getString(dataColumn)

                    Log.d("LibraryLocalMusicFragment", "Found file: $data")

                    val contentUri: Uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id.toString())
                    audioList.add(AudioFile(contentUri, title, artist))
                }
            } ?: Log.d("LibraryLocalMusicFragment", "Cursor is null for URI: $uri")
        }

        // Query both external and internal storage
        queryMediaStore(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
        queryMediaStore(MediaStore.Audio.Media.INTERNAL_CONTENT_URI)

        // Explicitly query the Downloads folder
        val downloadsPath: String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path
        queryMediaStore(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, downloadsPath)

        Log.d("LibraryLocalMusicFragment", "Fetched ${audioList.size} MP3 files")
        return audioList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class AudioFile(val uri: Uri, val title: String, val artist: String)
