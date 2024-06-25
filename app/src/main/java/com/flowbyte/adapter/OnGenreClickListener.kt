package com.flowbyte.adapter

import com.flowbyte.data.models.genres.ListGenre

interface OnGenreClickListener {
    fun onGenreClick(genre: ListGenre)
}