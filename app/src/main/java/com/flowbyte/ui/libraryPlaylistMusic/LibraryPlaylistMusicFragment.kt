package com.flowbyte.ui.libraryPlaylistMusic

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.flowbyte.R
import com.flowbyte.adapter.MusicAdapter
import com.flowbyte.adapter.RecylerNavLibraryAdapter
import com.flowbyte.data.LibraryMenuItem
import com.flowbyte.databinding.FragmentLibraryLocalMusicBinding
import com.flowbyte.databinding.FragmentLibraryPlaylistMusicBinding
import com.flowbyte.ui.libraryLocalMusic.LibraryLocalMusicFragment


class LibraryPlaylistMusicFragment : Fragment() {

    private var _binding: FragmentLibraryPlaylistMusicBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLibraryPlaylistMusicBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val navLibraryMenu = listOf(
            LibraryMenuItem("Local Music"),
            LibraryMenuItem("Playlist")
        )

        val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recylerNavLibrary.layoutManager = linearLayoutManager
        val adapter = RecylerNavLibraryAdapter(navLibraryMenu) { menuItem ->
            Log.d("LibraryLocalMusicFragment", "Item clicked: $menuItem")
            handleNavItemClick(menuItem.name)
        }
        binding.recylerNavLibrary.adapter = adapter

        // Inflate the layout for this fragment
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}