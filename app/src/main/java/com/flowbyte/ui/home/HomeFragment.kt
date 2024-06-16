package com.flowbyte.ui.home

import com.flowbyte.R
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.MenuItem
import android.view.ViewGroup
import android.content.Intent
import com.bumptech.glide.Glide
import android.view.MenuInflater
import android.view.LayoutInflater
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import com.flowbyte.data.SongAlbumItem
import androidx.lifecycle.ViewModelProvider
import com.flowbyte.activities.SongActivity
import com.google.firebase.auth.FirebaseUser
import com.flowbyte.activities.SettingsActivity
import androidx.appcompat.app.AppCompatActivity
import com.flowbyte.databinding.FragmentHomeBinding
import com.flowbyte.adapter.RecyclerViewAlbumAdapter
import androidx.recyclerview.widget.GridLayoutManager

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private var user: FirebaseUser? = null
    private lateinit var adapter: RecyclerViewAlbumAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
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

        // Atur profile picture dan nama user
        user = Firebase.auth.currentUser

        if (user?.photoUrl != null)
            Glide.with(this).load(user?.photoUrl).into(binding.profileImage)
        binding.username.text = user?.displayName ?: "User"

//        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
        }

        val listAlbum = listOf(
            SongAlbumItem(R.drawable.genre, "Album 1"),
            SongAlbumItem(R.drawable.genre, "Album 2"),
            SongAlbumItem(R.drawable.genre, "Album 3"),
            SongAlbumItem(R.drawable.genre, "Album 4")
        )

        val gridLayoutManager = GridLayoutManager(requireContext(), 2) // 2 columns
        binding.recyclerViewALLAlbum.layoutManager = gridLayoutManager

        adapter = RecyclerViewAlbumAdapter({ this }, listAlbum, object : RecyclerViewAlbumAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                if (position == 0) {
                    val intent = Intent(requireContext(), SongActivity::class.java)
                    startActivity(intent)
                }
            }
        })

        binding.recyclerViewALLAlbum.adapter = adapter

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

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}