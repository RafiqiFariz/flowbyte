package com.flowbyte.data.models.playlist

data class Tracks(
    val href: String,
    val limit: Int,
    val next: String,
    val offset: Int,
    val previous: String,
    val total: Int,
    val items: List<TrackItem>
)