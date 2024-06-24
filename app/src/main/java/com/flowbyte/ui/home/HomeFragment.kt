package com.flowbyte.ui.home

import com.flowbyte.R
import android.util.Log
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.MenuItem
import android.view.ViewGroup
import android.content.Intent
import com.bumptech.glide.Glide
import android.view.MenuInflater
import com.flowbyte.core.Resource
import android.view.LayoutInflater
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseUser
import com.flowbyte.activities.SettingsActivity
import androidx.appcompat.app.AppCompatActivity
import com.flowbyte.databinding.FragmentHomeBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.flowbyte.ui.home.playlist_detail.HomePlaylistDetailActivity
import com.flowbyte.adapter.RecyclerViewPlaylistsAdapter
import com.flowbyte.data.models.playlist.Item
import com.flowbyte.utils.GridSpacingItemDecoration
import com.flowbyte.utils.HorizontalSpaceItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private lateinit var homeViewModel: HomeViewModel
    private var user: FirebaseUser? = null
    private lateinit var adapter: RecyclerViewPlaylistsAdapter
    private lateinit var adapter2: RecyclerViewPlaylistsAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setupActionBar()
        setupUserProfile()

        homeViewModel.authorize()
        homeViewModel.spotifyToken.observe(viewLifecycleOwner) { token ->
            Log.d("Token Spotify: ", token.toString())
        }

        homeViewModel.fetchFeaturedPlaylists(11)
        setupPlaylists()
        homeViewModel.playlists.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val items1 = resource.data?.playlists?.items?.take(4)
                    items1?.let { adapter.updateData(it) }

                    val items2 =
                        resource.data?.playlists?.items?.drop(4)?.take(5)
                    items2?.let { adapter2.updateData(it) }

                    val items3 = resource.data?.playlists?.items?.takeLast(2)
                    items3?.let {
                        Glide.with(this).load(it[0].images[0].url).into(binding.imgPlaylistBottom1)
                        binding.titlePlaylistBottom1.text = it[0].name

                        Glide.with(this).load(it[1].images[0].url).into(binding.imgPlaylistBottom2)
                        binding.titlePlaylistBottom2.text = it[1].name
                    }
                }

                is Resource.Error -> {
                    Log.e("HomeFragment", "Error fetching playlists: ${resource.message}")
                }
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupActionBar() {
        val activity = (requireActivity() as AppCompatActivity?)

        activity?.setSupportActionBar(_binding?.toolbar)
        activity?.supportActionBar?.setDisplayShowTitleEnabled(false)

        activity?.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                if (menu.size() != 0) return
                menuInflater.inflate(R.menu.custom_toolbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_settings -> {
                        val settingsIntent = Intent(requireContext(), SettingsActivity::class.java)
                        startActivity(settingsIntent)
                        return true
                    }
                }
                return false
            }
        }, viewLifecycleOwner)
    }

    private fun setupUserProfile() {
        // Atur profile picture dan nama user
        user = Firebase.auth.currentUser

        if (user?.photoUrl != null)
            Glide.with(this).load(user?.photoUrl).into(binding.profileImage)
        binding.username.text = user?.displayName ?: "User"
    }

    private fun setupPlaylists() {
        val gridLayoutManager = GridLayoutManager(requireContext(), 2) // 2 columns
        binding.recyclerViewPlaylists.layoutManager = gridLayoutManager

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.grid_layout_margin)
        binding.recyclerViewPlaylists.addItemDecoration(
            GridSpacingItemDecoration(2, spacingInPixels, true, 0)
        )

        adapter = RecyclerViewPlaylistsAdapter(
            { this },
            emptyList(),
            object : RecyclerViewPlaylistsAdapter.OnItemClickListener {
                override fun onItemClick(position: Int, item: Item) {
                    val intent = Intent(requireContext(), HomePlaylistDetailActivity::class.java)
                    intent.putExtra("id", item.id)
                    startActivity(intent)
                }
            },
            R.layout.playlist_card_components
        )

        binding.recyclerViewPlaylists.adapter = adapter

        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvPlaylistsHorizontal.layoutManager = linearLayoutManager

        val horizontalSpacing = resources.getDimensionPixelSize(R.dimen.page_margin)
        binding.rvPlaylistsHorizontal.addItemDecoration(
            HorizontalSpaceItemDecoration(horizontalSpacing)
        )

        adapter2 = RecyclerViewPlaylistsAdapter(
            { this },
            emptyList(),
            object : RecyclerViewPlaylistsAdapter.OnItemClickListener {
                override fun onItemClick(position: Int, item: Item) {
                    val intent = Intent(requireContext(), HomePlaylistDetailActivity::class.java)
                    intent.putExtra("id", item.id)
                    startActivity(intent)
                }
            },
            R.layout.home_item_horizontal_list
        )

        binding.rvPlaylistsHorizontal.adapter = adapter2
    }
}