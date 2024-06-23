package com.flowbyte.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flowbyte.data.LibraryMenuItem
import com.flowbyte.databinding.NavLibraryBinding

class RecyclerNavLibraryAdapter(
    private val navLibrary: List<LibraryMenuItem>,
    private val onItemClick: (LibraryMenuItem) -> Unit
) : RecyclerView.Adapter<RecyclerNavLibraryAdapter.NavLibraryViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NavLibraryViewHolder {
        val binding = NavLibraryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NavLibraryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NavLibraryViewHolder, position: Int) {
        holder.bind(navLibrary[position])
    }

    override fun getItemCount(): Int {
        return navLibrary.size
    }

    inner class NavLibraryViewHolder(private val binding: NavLibraryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(libraryMenuItem: LibraryMenuItem) {
            binding.buttonNavLibrary.text = libraryMenuItem.name
            binding.buttonNavLibrary.setOnClickListener {
                Log.d("RecyclerNavLibraryAdapter", "Item clicked: ${libraryMenuItem})")
                onItemClick(libraryMenuItem)
            }
        }
    }
}
