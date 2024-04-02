package com.flowbyte.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.flowbyte.ListGenre
import com.flowbyte.R
import com.flowbyte.RecyclerViewListGenreAdapter
import com.flowbyte.databinding.FragmentExploreBinding

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private lateinit var adapter: RecyclerViewListGenreAdapter

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val exploreViewModel = ViewModelProvider(this).get(ExploreViewModel::class.java)

        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Assuming you have a list of genres to display
        val listGenres = listOf(
            ListGenre("Genre 1", R.drawable.genre),
            ListGenre("Genre 2", R.drawable.genre),
            ListGenre("Genre 3", R.drawable.genre)
        )

        // Setting up RecyclerView with GridLayoutManager
        val layoutManager = GridLayoutManager(requireContext(), 2) // 2 columns

        // Creating adapter with a lambda to provide the fragment instance
        adapter = RecyclerViewListGenreAdapter({ this }, listGenres)
        binding.recyclerViewGenre.adapter = adapter
        binding.recyclerViewGenre.layoutManager = layoutManager

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
