package com.example.knackplayer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.knackplayer.R
import com.example.knackplayer.model.Audio

class AudioRecyclerViewAdapter(private val songs: List<Audio> ,private val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<AudioRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.audio_list , parent , false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songs[position]
        holder.titleView.text = song.title

        holder.itemView.setOnClickListener {
            onItemClick.invoke(position)
        }
    }



    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val titleView:TextView = itemView.findViewById(R.id.song)

            }



}