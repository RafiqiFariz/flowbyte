package com.flowbyte.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flowbyte.data.LibraryMenuItem
import com.flowbyte.databinding.NavLibraryBinding

class RecylerNavLibraryAdapter(
    private val navLibrary: List<LibraryMenuItem>,
    private val onItemClick: (LibraryMenuItem) -> Unit
) : RecyclerView.Adapter<RecylerNavLibraryAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = NavLibraryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(navLibrary[position])
    }

    override fun getItemCount(): Int {
        return navLibrary.size
    }

    inner class MyViewHolder(private val binding: NavLibraryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(libraryMenuItem: LibraryMenuItem) {
            binding.buttonNavLibrary.text = libraryMenuItem.name
            binding.buttonNavLibrary.setOnClickListener {
                Log.d("RecylerNavLibraryAdapter", "Item clicked: ${libraryMenuItem})")
                onItemClick(libraryMenuItem)
            }
        }
    }
}
