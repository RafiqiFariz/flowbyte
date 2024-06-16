package com.flowbyte.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.flowbyte.R
import com.flowbyte.activities.SettingsActivity
import com.flowbyte.databinding.FragmentHomeBinding
import com.flowbyte.activities.SongActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private var user: FirebaseUser? = null

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

        binding.cardView1.setOnClickListener {
            val intent = Intent(this.requireContext(), SongActivity::class.java)
            startActivity(intent)
        }

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
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}