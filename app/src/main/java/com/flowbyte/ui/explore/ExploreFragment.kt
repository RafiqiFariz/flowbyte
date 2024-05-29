package com.flowbyte.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.flowbyte.data.ListGenre
import com.flowbyte.R
import com.flowbyte.Adapter.RecyclerViewListGenreAdapter
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
        (requireActivity() as AppCompatActivity?)?.supportActionBar?.hide()
        val exploreViewModel = ViewModelProvider(this).get(ExploreViewModel::class.java)

        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Assuming you have a list of genres to display
        val listGenres = listOf(
            ListGenre("Genre 1", R.drawable.genre),
            ListGenre("Genre 2", R.drawable.genre),
            ListGenre("Genre 3", R.drawable.genre),
            ListGenre("Genre 4", R.drawable.genre)
        )

        val allGenres = listOf(
            ListGenre("Genre 1", R.drawable.genre),
            ListGenre("Genre 2", R.drawable.genre),
            ListGenre("Genre 3", R.drawable.genre),
            ListGenre("Genre 4", R.drawable.genre),
            ListGenre("Genre 5", R.drawable.genre),
            ListGenre("Genre 6", R.drawable.genre),
            ListGenre("Genre 7", R.drawable.genre),
            ListGenre("Genre 8", R.drawable.genre)
        )

        // Setting up RecyclerView with LinearLayoutManager for the first RecyclerView
        val gridLayoutManager1 = GridLayoutManager(requireContext(), 2) // 2 columns
        binding.recyclerViewGenre.layoutManager = gridLayoutManager1

        // Creating adapter with a lambda to provide the fragment instance
        adapter = RecyclerViewListGenreAdapter({ this }, listGenres)
        binding.recyclerViewGenre.adapter = adapter

        // Setting up RecyclerView with GridLayoutManager for the second RecyclerView
        val gridLayoutManager2 = GridLayoutManager(requireContext(), 2) // 2 columns
        binding.recyclerViewALLGenre.layoutManager = gridLayoutManager2

        // Creating adapter with a lambda to provide the fragment instance
        adapter = RecyclerViewListGenreAdapter({ this }, allGenres)
        binding.recyclerViewALLGenre.adapter = adapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
