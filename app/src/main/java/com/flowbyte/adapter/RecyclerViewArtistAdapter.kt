package com.flowbyte.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flowbyte.R
import com.flowbyte.data.ListArtist
import com.flowbyte.ui.artist_based_by_genre.ArtistBasedByGenreFragment

class RecyclerViewArtistAdapter(
    private val listArtist: List<ListArtist>,
    private val onGenreClickListener: ArtistBasedByGenreFragment
) : RecyclerView.Adapter<RecyclerViewArtistAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_card_components, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val artist = listArtist[position]
        holder.genreListTitle.text = artist.name
        Glide.with(holder.itemView.context)
            .load(artist.picture)
            .placeholder(R.drawable.placeholder)
            .into(holder.genreListImg)

        holder.cardView.setOnClickListener {
//            onGenreClickListener.onGenreClick(artist)
        }
    }

    override fun getItemCount(): Int {
        return listArtist.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val genreListTitle: TextView = itemView.findViewById(R.id.cardText)
        val genreListImg: ImageView = itemView.findViewById(R.id.imageCard)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }
}
