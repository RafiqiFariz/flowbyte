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
import com.flowbyte.data.SongAlbumItem

class RecyclerViewAlbumAdapter(private val getSpecificFragment: () -> Fragment, private val listAlbum: List<SongAlbumItem>) : RecyclerView.Adapter<RecyclerViewAlbumAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_card_components, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.albumTitle.text = listAlbum[position].text
        Glide.with(holder.albumImg.context)
            .load(listAlbum[position].imageResId)
            .transform(CenterCrop(), RoundedCorners(16)) // Optional: apply transformations
            .override(300, 300) // Resize the image to a specific size (width, height)
            .into(holder.albumImg)

        holder.cardView.setOnClickListener {
            Toast.makeText(holder.itemView.context, listAlbum[position].text, Toast.LENGTH_LONG).show()
        }
    }

    override fun getItemCount(): Int {
        return listAlbum.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val albumTitle: TextView = itemView.findViewById(R.id.cardText)
        val albumImg: ImageView = itemView.findViewById(R.id.imageCard)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }
}
