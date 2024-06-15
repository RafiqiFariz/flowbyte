package com.flowbyte.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.flowbyte.activities.SettingsActivity
import com.flowbyte.databinding.FragmentHomeBinding
import com.flowbyte.activities.SongActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

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

//        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
        }

        binding.cardView1.setOnClickListener {
            val intent = Intent(this.requireContext(), SongActivity::class.java)
            startActivity(intent)
        }

        binding.cardView2.setOnClickListener {
            Log.d("HomeFragment", "btn_settings clicked")
            val settingsIntent = Intent(this.requireContext(), SettingsActivity::class.java)
            startActivity(settingsIntent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}