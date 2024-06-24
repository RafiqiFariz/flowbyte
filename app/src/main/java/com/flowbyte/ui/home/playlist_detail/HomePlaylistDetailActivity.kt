package com.flowbyte.ui.home.playlist_detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.flowbyte.activities.SongActivity
import com.flowbyte.adapter.RecyclerViewPlaylistTrackAdapter
import com.flowbyte.core.Resource
import com.flowbyte.data.models.playlist.TrackItem
import com.flowbyte.databinding.ActivityHomePlaylistDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomePlaylistDetailActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityHomePlaylistDetailBinding
    private lateinit var homePlaylistDetailViewModel: HomePlaylistDetailViewModel
    private lateinit var adapter: RecyclerViewPlaylistTrackAdapter
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomePlaylistDetailBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        homePlaylistDetailViewModel =
            ViewModelProvider(this).get(HomePlaylistDetailViewModel::class.java)
        val playlistId = intent.getStringExtra("id") ?: return
        homePlaylistDetailViewModel.setId(playlistId)

        homePlaylistDetailViewModel.details.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.let {
                        it.data?.tracks?.let { it1 -> adapter.updateData(it1.items) }
                        binding.playlistName.text = it.data?.name
                        binding.playlistDescription.text = it.data?.description
                        Glide.with(this).load(it.data?.images?.get(0)?.url)
                            .into(binding.playlistImage)
                    }
                }

                is Resource.Error -> Log.e(
                    "HomePlaylistDetailActivity",
                    "Error fetching playlist detail: ${resource.message}"
                )
            }
        }

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvTracks.layoutManager = linearLayoutManager

        adapter = RecyclerViewPlaylistTrackAdapter(
            { this },
            emptyList(),
            object : RecyclerViewPlaylistTrackAdapter.OnItemClickListener {
                override fun onItemClick(position: Int, trackItem: TrackItem) {
                    val intent = Intent(this@HomePlaylistDetailActivity, SongActivity::class.java)
                    intent.putExtra("song_img", trackItem.track.album.images[0].url)
                    intent.putExtra("song_uri", trackItem.track.previewUrl)
                    intent.putExtra("song_title", trackItem.track.name)
                    intent.putExtra("song_artists", trackItem.track.artists[0].name)
                    startActivity(intent)
                }
            },
        )

        binding.rvTracks.adapter = adapter
    }
}
