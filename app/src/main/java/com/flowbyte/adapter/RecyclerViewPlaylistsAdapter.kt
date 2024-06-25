package com.flowbyte.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.flowbyte.R
import com.flowbyte.data.models.playlist.Item

class RecyclerViewPlaylistsAdapter(
    private val getSpecificFragment: () -> Fragment,
    private var playlists: List<Item>,
    private val itemClickListener: OnItemClickListener,
    private val layoutId: Int
) : RecyclerView.Adapter<RecyclerViewPlaylistsAdapter.PlaylistViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int, item: Item)
    }

    fun updateData(newPlaylists: List<Item>) {
        playlists = newPlaylists
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val item = playlists[position]
        holder.title.text = playlists[position].name
        Glide.with(holder.imgCover.context)
            .load(playlists[position].images[0].url)
            .transform(CenterCrop(), RoundedCorners(16)) // Optional: apply transformations
            .override(300, 300) // Resize the image to a specific size (width, height)
            .into(holder.imgCover)

        holder.cardView.setOnClickListener {
//            Toast.makeText(holder.itemView.context, playlists[position].name, Toast.LENGTH_LONG).show()
            itemClickListener.onItemClick(position, item)
        }

        holder.bindingAdapterPosition
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.cardText)
        val imgCover: ImageView = itemView.findViewById(R.id.imageCard)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }
}
