package com.flowbyte.data

data class ListArtistBasedByGenreReponse(
    val data: List<ListArtist>
)
data class ListArtist(
    val id: Int,
    val name: String,
    val picture: String,
    val radio: String
)