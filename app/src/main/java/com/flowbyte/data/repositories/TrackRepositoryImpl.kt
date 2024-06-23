package com.flowbyte.data.repositories

import com.flowbyte.core.Resource
import com.flowbyte.data.models.tracks.TrackResponse
import com.flowbyte.data.remote.TrackApi
import com.flowbyte.domain.repositories.TrackRepository
import javax.inject.Inject

class TrackRepositoryImpl @Inject constructor(
    private val trackApi: TrackApi
) : TrackRepository {
    override suspend fun getTrack(id: String): Resource<TrackResponse> {
        return try {
            Resource.Success(trackApi.getTrack(id))
        } catch (e: Exception) {
            Resource.Error(message = "${e.message}")
        }
    }
}