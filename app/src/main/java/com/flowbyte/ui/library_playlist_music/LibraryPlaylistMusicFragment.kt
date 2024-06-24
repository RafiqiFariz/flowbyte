package com.flowbyte.ui.libraryPlaylistMusic

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flowbyte.R
import com.flowbyte.adapter.PlaylistAdapter
import com.flowbyte.adapter.RecyclerNavLibraryAdapter
import com.flowbyte.data.LibraryMenuItem
import com.flowbyte.data.Playlist
import com.flowbyte.ui.library.local_music.LibraryLocalMusicFragment
import com.google.firebase.firestore.FirebaseFirestore

class LibraryPlaylistMusicFragment : Fragment() {

    private lateinit var playlistAdapter: PlaylistAdapter
    private val playlists = mutableListOf<Playlist>()
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_library_playlist_music, container, false)

        // Initialize Firestore
        db = FirebaseFirestore.getInstance()

        // Setup RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recylerPlaylist)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        playlistAdapter = PlaylistAdapter(playlists, requireContext())
        recyclerView.adapter = playlistAdapter

        val btnAddNewPlaylist = view.findViewById<LinearLayout>(R.id.btn_add_new_playlist)
        btnAddNewPlaylist.setOnClickListener {
            showNewPlaylistDialog()
        }

        // Setup navigation menu RecyclerView
        val navLibraryMenu = listOf(
            LibraryMenuItem("Local Music"),
            LibraryMenuItem("Playlist")
        )
        val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val navRecyclerView = view.findViewById<RecyclerView>(R.id.recylerNavLibrary)
        navRecyclerView.layoutManager = linearLayoutManager
        val adapter = RecyclerNavLibraryAdapter(navLibraryMenu) { menuItem ->
            Log.d("LibraryLocalMusicFragment", "Item clicked: ${menuItem.name}")
            handleNavItemClick(menuItem.name)
        }
        navRecyclerView.adapter = adapter

        // Load playlists from Firestore
        loadPlaylistsFromFirestore()

        return view
    }

    private fun showNewPlaylistDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_new_playlist, null)
        val playlistNameInput = dialogLayout.findViewById<EditText>(R.id.playlist_name_input)

        builder.setView(dialogLayout)
        val dialog = builder.create()

        dialogLayout.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }

        dialogLayout.findViewById<Button>(R.id.btn_create).setOnClickListener {
            val playlistName = playlistNameInput.text.toString()
            // Add new playlist and update RecyclerView
            val newPlaylist = Playlist(playlistName)
            savePlaylistToFirestore(newPlaylist)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun handleNavItemClick(menuItem: String) {
        val fragment = when (menuItem) {
            "Local Music" -> LibraryLocalMusicFragment()
            "Playlist" -> LibraryPlaylistMusicFragment()
            else -> null
        }
        fragment?.let {
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.nav_host_fragment_activity_main, it) // Ensure you have a container in your layout
                .addToBackStack(null)
                .commit()
        }
    }

    private fun savePlaylistToFirestore(playlist: Playlist) {
        db.collection("playlists")
            .add(playlist)
            .addOnSuccessListener {
                Log.d("Firestore", "Playlist saved successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Failed to save playlist", e)
            }
    }

    private fun loadPlaylistsFromFirestore() {
        db.collection("playlists")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("Firestore", "Listen failed", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    playlists.clear()
                    for (document in snapshot.documents) {
                        val playlist = document.toObject(Playlist::class.java)
                        playlist?.let { playlists.add(it) }
                    }
                    playlistAdapter.notifyDataSetChanged()
                } else {
                    Log.d("Firestore", "Current data: null")
                }
            }
    }
}
