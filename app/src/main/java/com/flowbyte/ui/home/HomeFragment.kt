package com.flowbyte.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.flowbyte.R
import com.flowbyte.adapter.RecyclerViewAlbumAdapter
import com.flowbyte.data.SongAlbumItem
import com.flowbyte.databinding.FragmentHomeBinding
import com.flowbyte.activities.SongActivity
import com.flowbyte.activities.SettingsActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var adapter: RecyclerViewAlbumAdapter

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val activity = (requireActivity() as AppCompatActivity?)

        activity?.setSupportActionBar(_binding?.toolbar)
        activity?.supportActionBar?.setDisplayShowTitleEnabled(false)

        val root: View = binding.root

        homeViewModel.text.observe(viewLifecycleOwner) {
            // Update UI if needed
        }
        val listAlbum = listOf(
            SongAlbumItem(R.drawable.genre, "Album 1"),
            SongAlbumItem(R.drawable.genre, "Album 2"),
            SongAlbumItem(R.drawable.genre, "Album 3"),
            SongAlbumItem(R.drawable.genre, "Album 4")
        )

        val gridLayoutManager = GridLayoutManager(requireContext(), 2) // 2 columns
        binding.recyclerViewALLAlbum.layoutManager = gridLayoutManager

        // Creating adapter with a lambda to provide the fragment instance
        adapter = RecyclerViewAlbumAdapter({ this }, listAlbum)
        binding.recyclerViewALLAlbum.adapter = adapter

        binding.cardView1.setOnClickListener {
            val intent = Intent(this.requireContext(), SongActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
