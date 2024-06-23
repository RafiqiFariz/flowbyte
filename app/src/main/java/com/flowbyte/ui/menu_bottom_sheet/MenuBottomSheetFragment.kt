package com.flowbyte.ui.menu_bottom_sheet

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flowbyte.R
import com.flowbyte.data.MenuItem
import com.flowbyte.adapter.MenuAdapter
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MenuBottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance() = MenuBottomSheetFragment()
    }

    private lateinit var viewModel: MenuBottomSheetViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu_bottom_sheet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MenuBottomSheetViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewMenu)
        recyclerView.layoutManager = FlexboxLayoutManager(requireContext())

        val menuList = listOf(
            MenuItem("Add to playlist", R.drawable.ic_playlist_add_24dp),
            MenuItem("Add to queue", R.drawable.ic_add_to_queue_24dp),
            MenuItem("Remove from playlist", R.drawable.ic_playlist_remove_24dp),
            MenuItem("View Artist", R.drawable.ic_person_outline_24dp),
            MenuItem("View Album", R.drawable.ic_outline_album_24dp),
            MenuItem("Download", R.drawable.ic_outline_file_download_24dp)
        )

        val adapter = MenuAdapter(menuList)
        recyclerView.adapter = adapter
    }
}