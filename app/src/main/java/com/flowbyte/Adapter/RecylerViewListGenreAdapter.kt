package com.flowbyte.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.flowbyte.R
import com.flowbyte.data.ListGenre

class RecyclerViewListGenreAdapter(private val getSpecificFragment: () -> Fragment, private val ListGenre: List<ListGenre>) : RecyclerView.Adapter<RecyclerViewListGenreAdapter.MyViewHolder>() {
    // Adapter implementation here
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_card_components, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.GenreListTitle.text = ListGenre[position].title
        holder.GenreListImg.setImageResource(ListGenre[position].image)

        holder.CardView.setOnClickListener {
            Toast.makeText(holder.itemView.context, ListGenre[position].title, Toast.LENGTH_LONG).show()
        }
    }

    override fun getItemCount(): Int {
        return ListGenre.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val GenreListTitle: TextView = itemView.findViewById(R.id.cardText)
        val GenreListImg: ImageView = itemView.findViewById(R.id.imageCard)
        val CardView: CardView = itemView.findViewById(R.id.cardView)
    }

}
