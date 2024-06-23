package com.flowbyte.data.models

data class SpotifyToken(
    val accessToken: String,
    val tokenType: String,
    val expiresAt: Int
)

