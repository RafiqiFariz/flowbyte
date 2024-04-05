package com.flowbyte.ui.library

import RecylerNavLibraryAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.flowbyte.data.LibraryMenuItem
import com.flowbyte.databinding.FragmentLibraryBinding

class LibraryFragment : Fragment() {

    private var _binding: FragmentLibraryBinding? = null
    private lateinit var adapter: RecylerNavLibraryAdapter
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        (requireActivity() as AppCompatActivity?)?.supportActionBar?.hide()
        val libraryViewModel =
                ViewModelProvider(this).get(LibraryViewModel::class.java)

        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val navLibraryMenu = listOf(
            LibraryMenuItem("Genre 1"),
            LibraryMenuItem("Genre 2"),
            LibraryMenuItem("Genre 3"),
            LibraryMenuItem("Genre 4")
        )

        // Setting up RecyclerView with LinearLayoutManager for the first RecyclerView
        val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recylerNavLibrary.layoutManager = linearLayoutManager

        // Creating adapter with a lambda to provide the fragment instance
        adapter = RecylerNavLibraryAdapter({ this }, navLibraryMenu)
        binding.recylerNavLibrary.adapter = adapter

////        val textView: TextView = binding.textNotifications
//        libraryViewModel.text.observe(viewLifecycleOwner) {
////            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}