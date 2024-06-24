package com.flowbyte.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.flowbyte.R
import com.flowbyte.data.models.playlist.TrackItem
import com.flowbyte.ui.home.playlist_detail.HomePlaylistDetailActivity

class RecyclerViewPlaylistTrackAdapter(
    private val getSpecificActivity: () -> HomePlaylistDetailActivity,
    private var tracks: List<TrackItem>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerViewPlaylistTrackAdapter.PlaylistTrackViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int, item: TrackItem)
    }

    fun updateData(newPlaylists: List<TrackItem>) {
        tracks = newPlaylists
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistTrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_music, parent, false)
        return PlaylistTrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistTrackViewHolder, position: Int) {
        val item = tracks[position]
        val albumName = tracks[position].track.album.name
        val artistName = tracks[position].track.artists[0].name

        holder.trackTitle.text = albumName
        holder.trackArtist.text = artistName

        Glide.with(holder.trackImage.context)
            .load(tracks[position].track.album.images[0].url)
            .transform(CenterCrop(), RoundedCorners(16)) // Optional: apply transformations
            .override(300, 300) // Resize the image to a specific size (width, height)
            .into(holder.trackImage)

        holder.track.setOnClickListener {
            Toast.makeText(holder.itemView.context, albumName, Toast.LENGTH_LONG).show()
            itemClickListener.onItemClick(position, item)
        }

        holder.bindingAdapterPosition
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    class PlaylistTrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val track: RelativeLayout = itemView.findViewById(R.id.track)
        val trackImage: ImageView = itemView.findViewById(R.id.track_image)
        val trackTitle: TextView = itemView.findViewById(R.id.track_title)
        val trackArtist: TextView = itemView.findViewById(R.id.track_artist)
    }
}
