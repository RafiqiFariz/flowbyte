package com.flowbyte.ui.library.local_music

import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
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
        musicAdapter = MusicAdapter(getAllMp3Files()) { audioFile ->
            // Handle item click: Open SongActivity and pass selected song URI
            val intent = Intent(requireContext(), SongActivity::class.java)
            intent.putExtra("song_uri", audioFile.uri.toString())
            intent.putExtra("song_name", audioFile.title)
            intent.putExtra("song_artist", audioFile.artist)
            startActivity(intent)
        }
        binding.recylerPlaylist.adapter = musicAdapter

        return root
    }

    private fun handleNavItemClick(menuItem: Any) {
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
        val audioList = mutableListOf<AudioFile>()

        val contentResolver: ContentResolver = requireActivity().contentResolver
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST
        )
        val selection = "${MediaStore.Audio.Media.MIME_TYPE}=?"
        val selectionArgs = arrayOf("audio/mpeg")
        val cursor: Cursor? = contentResolver.query(uri, projection, selection, selectionArgs, null)
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
        Log.d("data", audioList.toString())
        return audioList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class AudioFile(val uri: Uri, val title: String, val artist: String)
