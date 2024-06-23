package com.flowbyte.data.remote

import com.flowbyte.data.models.search.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("/v1/search?type=track,artist")
    suspend fun getNewReleases(
        @Query("q") query: String
    ): SearchResponse
}