package com.flowbyte.ui.artist_based_by_genre

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.flowbyte.adapter.RecyclerViewArtistAdapter
import com.flowbyte.data.ListArtist
import com.flowbyte.data.ListArtistBasedByGenreReponse
import com.flowbyte.databinding.FragmentArtistBasedByGenreBinding
import com.flowbyte.service.deezer.ApiClient
import com.flowbyte.service.deezer.DeezerApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArtistBasedByGenreFragment : Fragment() {

    private var _binding: FragmentArtistBasedByGenreBinding? = null
    private val binding get() = _binding!!

    private var genreID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            genreID = it.getInt(ARG_GENRE_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistBasedByGenreBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up RecyclerView
        binding.recyclerViewALLArtists.layoutManager = GridLayoutManager(requireContext(), 2) // 2 columns
//        adapter = RecyclerViewArtistAdapter(ListArtist)
//
//        binding.recyclerViewALLArtists.adapter = adapter

        fetchArtists()

        return root
    }

    private fun fetchArtists() {
        val apiService = ApiClient.retrofit.create(DeezerApiService::class.java)
        val call = apiService.getArtistByGenre()

        call.enqueue(object : Callback<ListArtistBasedByGenreReponse> {
            override fun onResponse(call: Call<ListArtistBasedByGenreReponse>, response: Response<ListArtistBasedByGenreReponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val artist = it.data

                        // Update UI with fetched genres
                        updateRecyclerViews(artist)
                    }
                }
            }

            override fun onFailure(call: Call<ListArtistBasedByGenreReponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun updateRecyclerViews(artist: List<ListArtist>) {

        val adapter = RecyclerViewArtistAdapter(artist, this)
        binding.recyclerViewALLArtists.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_GENRE_ID = "genre_id"

        fun newInstance(genreID: Int): ArtistBasedByGenreFragment {
            val fragment = ArtistBasedByGenreFragment()
            val args = Bundle().apply {
                putInt(ARG_GENRE_ID, genreID)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
