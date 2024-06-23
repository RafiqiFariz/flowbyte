package com.flowbyte.data.repositories

import com.flowbyte.core.Resource
import com.flowbyte.data.models.search.SearchResponse
import com.flowbyte.data.remote.SearchApi
import com.flowbyte.domain.repositories.SearchRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchApi: SearchApi
) : SearchRepository {
    override suspend fun getNewReleases(query: String): Resource<SearchResponse> {
        return try {
            Resource.Success(searchApi.getNewReleases(query))
        } catch (e: IOException) {
            Resource.Error(message = "${e.message}")
        } catch (e: HttpException) {
            Resource.Error(message = "${e.message}")
        } catch (e: Exception) {
            Resource.Error(message = "${e.message}")
        }
    }
}