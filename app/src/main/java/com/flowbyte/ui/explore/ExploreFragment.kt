package com.flowbyte.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.flowbyte.R
import com.flowbyte.adapter.OnGenreClickListener
import com.flowbyte.adapter.RecyclerViewListGenreAdapter
import com.flowbyte.data.ListGenre
import com.flowbyte.data.GenreResponse
import com.flowbyte.databinding.FragmentExploreBinding
import com.flowbyte.service.deezer.ApiClient
import com.flowbyte.service.deezer.DeezerApiService
import com.flowbyte.ui.artist_based_by_genre.ArtistBasedByGenreFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExploreFragment : Fragment(), OnGenreClickListener {

    private var _binding: FragmentExploreBinding? = null
    private lateinit var adapter: RecyclerViewListGenreAdapter

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as AppCompatActivity?)?.supportActionBar?.hide()

        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Setting up RecyclerView with GridLayoutManager for the first RecyclerView
        binding.recyclerViewALLGenre.layoutManager = GridLayoutManager(requireContext(), 2) // 2 columns

        fetchGenres()

        return root
    }

    private fun fetchGenres() {
        val apiService = ApiClient.retrofit.create(DeezerApiService::class.java)
        val call = apiService.getGenres()

        call.enqueue(object : Callback<GenreResponse> {
            override fun onResponse(call: Call<GenreResponse>, response: Response<GenreResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val genres = it.data

                        // Update UI with fetched genres
                        updateRecyclerViews(genres)
                    }
                }
            }

            override fun onFailure(call: Call<GenreResponse>, t: Throwable) {
                // Handle error
            }
        })
    }

    private fun updateRecyclerViews(genres: List<ListGenre>) {
        // Filter out genres where the name is "all"
        val filteredGenres = genres.filter { it.name.lowercase() != "all" }

        val adapter = RecyclerViewListGenreAdapter(filteredGenres, this)
        binding.recyclerViewALLGenre.adapter = adapter
    }

    override fun onGenreClick(genre: ListGenre) {
        val fragment = getSpecificFragment(genre)
        val transaction = parentFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true) // Ensure atomic back stack operations
        transaction.replace(R.id.nav_host_fragment_activity_main, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    private fun getSpecificFragment(genre: ListGenre): Fragment {
        // Return the fragment you want to navigate to, using the genre information if necessary
        return ArtistBasedByGenreFragment.newInstance(genre.id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
