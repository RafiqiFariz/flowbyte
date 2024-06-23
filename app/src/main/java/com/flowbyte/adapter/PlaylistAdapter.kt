package com.flowbyte.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.flowbyte.R
import com.flowbyte.activities.PlaylistDetailActivity
import com.flowbyte.data.Playlist

class PlaylistAdapter(private val playlists: MutableList<Playlist>, private val context: Context) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    inner class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playlistImage: ImageView = itemView.findViewById(R.id.playlist_image)
        val playlistTitle: TextView = itemView.findViewById(R.id.playlist_title)

        init {
            itemView.setOnClickListener {
                val playlist = playlists[adapterPosition]
                val intent = Intent(context, PlaylistDetailActivity::class.java).apply {
                    putExtra("playlist_name", playlist.name)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_playlist, parent, false)
        return PlaylistViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.playlistTitle.text = playlist.name
        // Set the image for the playlist (using a placeholder for now)
        holder.playlistImage.setImageResource(R.drawable.ic_playlist_placeholder)
    }

    override fun getItemCount() = playlists.size

    fun addPlaylist(playlist: Playlist) {
        playlists.add(playlist)
        notifyItemInserted(playlists.size - 1)
    }
}
