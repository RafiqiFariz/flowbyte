package com.flowbyte.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.flowbyte.ui.library.local_music.AudioFile

class MusicAdapter(
    private val audioFiles: List<AudioFile>,
    private val onItemClick: (AudioFile) -> Unit,
    private val onItemLongClick: (AudioFile) -> Unit
) : RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val audioFile = audioFiles[position]
        holder.bind(audioFile)
        holder.itemView.setOnClickListener {
            onItemClick(audioFile)
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClick(audioFile)
            true
        }
    }

    override fun getItemCount(): Int = audioFiles.size

    class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(audioFile: AudioFile) {
            itemView.findViewById<TextView>(android.R.id.text1).text = audioFile.title
            itemView.findViewById<TextView>(android.R.id.text2).text = audioFile.artist
        }
    }
}
