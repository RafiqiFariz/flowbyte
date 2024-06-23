package com.flowbyte.domain.repositories

interface AuthRepository {
    suspend fun getSpotifyToken(): String
}