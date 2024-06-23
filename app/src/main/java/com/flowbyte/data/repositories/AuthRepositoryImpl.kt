package com.flowbyte.data.repositories

import com.flowbyte.BuildConfig
import com.flowbyte.core.AppPreferences
import com.flowbyte.core.Constants
import com.flowbyte.data.models.SpotifyToken
import com.flowbyte.data.remote.AuthApi
import com.flowbyte.domain.repositories.AuthRepository
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi
) : AuthRepository {
    override suspend fun getSpotifyToken(): String {
        val token = AppPreferences.token
        if (token == null || token.expiresAt < System.currentTimeMillis()) {
            // Refresh the token
            val response = authApi.getSpotifyToken("client_credentials", Constants.CLIENT_ID, BuildConfig.CLIENT_SECRET)
            if (response.isSuccessful) {
                val tokenResponse = response.body()
                tokenResponse?.let {
                    AppPreferences.token = SpotifyToken(
                        it["access_token"].asString,
                        it["token_type"].asString,
                        it["expires_in"].asInt,
                    )
                    return it["access_token"].asString
                }
            }
            throw IOException("Failed to get Spotify token")
        } else {
            return token.accessToken
        }
    }
}