package com.flowbyte.ui.explore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.flowbyte.R
import com.flowbyte.adapter.RecyclerViewListGenreAdapter
import com.flowbyte.core.Resource
import com.flowbyte.databinding.FragmentExploreBinding
import com.flowbyte.ui.home.playlist_detail.HomePlaylistDetailActivity
import com.flowbyte.utils.GridSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private lateinit var adapter: RecyclerViewListGenreAdapter
    private lateinit var exploreViewModel: ExploreViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as AppCompatActivity?)?.supportActionBar?.hide()

        exploreViewModel = ViewModelProvider(this).get(ExploreViewModel::class.java)
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        val root: View = binding.root

        exploreViewModel.fetchGenres()

        val linearLayoutManager = GridLayoutManager(requireContext(), 2) // 2 columns
        binding.recyclerViewALLGenre.layoutManager = linearLayoutManager

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.grid_layout_margin)
        binding.recyclerViewALLGenre.addItemDecoration(
            GridSpacingItemDecoration(2, spacingInPixels, true, 0)
        )

        adapter = RecyclerViewListGenreAdapter(
            { this },
            emptyList(),
            object : RecyclerViewListGenreAdapter.OnItemClickListener {
                override fun onItemClick(position: Int, item: String) {
                    Log.d("Item CLick Genre: ", item)
//                    val intent = Intent(requireContext(), HomePlaylistDetailActivity::class.java)
//                    intent.putExtra("id", item)
//                    startActivity(intent)
                }
            }
        )

        binding.recyclerViewALLGenre.adapter = adapter

        exploreViewModel.genres.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val items = resource.data?.genres
                    items?.let { adapter.updateData(it) }
                }

                is Resource.Error -> {
                    Log.e("ExploreFragment", "Error fetching genres: ${resource.message}")
                }
            }
        }

        return root
    }

    private fun fetchGenres() {
//        val apiService = ApiClient.retrofit.create(GenreApi::class.java)
//        val call = apiService.getGenres()

//        call.enqueue(object : Callback<GenreResponse> {
//            override fun onResponse(call: Call<GenreResponse>, response: Response<GenreResponse>) {
//                if (response.isSuccessful) {
//                    response.body()?.let {
//                        val genres = it.data
//
//                        // Update UI with fetched genres
//                        updateRecyclerViews(genres)
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<GenreResponse>, t: Throwable) {
//                // Handle error
//            }
//        })
    }

//    private fun updateRecyclerViews(genres: List<ListGenre>) {
//        // Filter out genres where the name is "all"
//        val filteredGenres = genres.filter { it.name.lowercase() != "all" }
//
//        val adapter = RecyclerViewListGenreAdapter(filteredGenres, this)
//        binding.recyclerViewALLGenre.adapter = adapter
//    }

//    override fun onGenreClick(genre: ListGenre) {
//        val fragment = getSpecificFragment(genre)
//        val transaction = parentFragmentManager.beginTransaction()
//        transaction.setReorderingAllowed(true) // Ensure atomic back stack operations
//        transaction.replace(R.id.nav_host_fragment_activity_main, fragment)
//        transaction.addToBackStack(null)
//        transaction.commit()
//    }


//    private fun getSpecificFragment(genre: ListGenre): Fragment {
//        // Return the fragment you want to navigate to, using the genre information if necessary
//        return ArtistBasedByGenreFragment.newInstance(genre.id)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
