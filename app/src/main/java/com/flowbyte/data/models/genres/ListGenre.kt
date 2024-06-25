package com.flowbyte.data.models.genres

data class GenreResponse(
    val genres: List<String>
)

data class ListGenre(
    val id: Int,
    val name: String,
    val picture: String
)