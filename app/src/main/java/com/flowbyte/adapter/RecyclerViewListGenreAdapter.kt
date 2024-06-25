package com.flowbyte.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flowbyte.R
import com.flowbyte.data.models.genres.ListGenre
import com.flowbyte.data.models.playlist.Item

class RecyclerViewListGenreAdapter(
    private val getSpecificFragment: () -> Fragment,
    private var listGenre: List<String>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerViewListGenreAdapter.ListGenreViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int, item: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListGenreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_card_components, parent, false)
        return ListGenreViewHolder(view)
    }

    fun updateData(newGenres: List<String>) {
        listGenre = newGenres
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ListGenreViewHolder, position: Int) {
        val genre = listGenre[position]
        Log.d("Genre Bro: ", genre)
        holder.genreListTitle.text = genre
//        Glide.with(holder.itemView.context)
//            .load(genre.picture)
//            .placeholder(R.drawable.placeholder)
//            .into(holder.genreListImg)

        holder.cardView.setOnClickListener {
            itemClickListener.onItemClick(position, genre)
        }

        holder.bindingAdapterPosition
    }

    override fun getItemCount(): Int {
        return listGenre.size
    }

    class ListGenreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val genreListTitle: TextView = itemView.findViewById(R.id.cardText)
//        val genreListImg: ImageView = itemView.findViewById(R.id.imageCard)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }
}
