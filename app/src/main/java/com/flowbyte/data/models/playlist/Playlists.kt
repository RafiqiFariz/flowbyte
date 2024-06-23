package com.flowbyte.data.models.playlist

data class Playlists(
    val href: String,
    val limit: Int,
    val next: Any,
    val offset: Int,
    val previous: Any,
    val total: Int,
    val items: List<Item>,
)
