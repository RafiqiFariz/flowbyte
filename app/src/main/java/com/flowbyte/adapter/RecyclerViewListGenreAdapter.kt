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
import com.flowbyte.data.ListGenre

class RecyclerViewListGenreAdapter(
    private val listGenre: List<ListGenre>,
    private val onGenreClickListener: OnGenreClickListener
) : RecyclerView.Adapter<RecyclerViewListGenreAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_card_components, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val genre = listGenre[position]
        holder.genreListTitle.text = genre.name
        Glide.with(holder.itemView.context)
            .load(genre.picture)
            .placeholder(R.drawable.placeholder)
            .into(holder.genreListImg)

        holder.cardView.setOnClickListener {
            onGenreClickListener.onGenreClick(genre)
        }
    }

    override fun getItemCount(): Int {
        return listGenre.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val genreListTitle: TextView = itemView.findViewById(R.id.cardText)
        val genreListImg: ImageView = itemView.findViewById(R.id.imageCard)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }
}
