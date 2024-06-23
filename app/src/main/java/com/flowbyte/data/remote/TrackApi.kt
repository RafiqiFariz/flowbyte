package com.flowbyte.data.remote

import com.flowbyte.data.models.tracks.TrackResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface TrackApi {
    @GET("v1/tracks/{id}")
    suspend fun getTrack(
        @Path("id") id: String
    ): TrackResponse
}