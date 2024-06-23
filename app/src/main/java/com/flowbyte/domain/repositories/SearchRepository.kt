package com.flowbyte.domain.repositories

import com.flowbyte.core.Resource
import com.flowbyte.data.models.search.SearchResponse

interface SearchRepository {
    suspend fun getNewReleases(query: String): Resource<SearchResponse>
}