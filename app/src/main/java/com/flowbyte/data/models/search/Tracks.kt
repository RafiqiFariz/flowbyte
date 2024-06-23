package com.flowbyte.data.models.search

data class Tracks(
    val href: String,
    val items: List<ItemTrack>,
    val limit: Int,
    val next: String,
    val offset: Int,
    val previous: Any,
    val total: Int
)