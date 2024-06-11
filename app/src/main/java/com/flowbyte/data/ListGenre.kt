package com.flowbyte.data

data class GenreResponse(
    val data: List<ListGenre>
)
data class ListGenre(
    val id: Int,
    val name: String,
    val picture: String
)