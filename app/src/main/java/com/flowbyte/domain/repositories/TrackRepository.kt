package com.flowbyte.domain.repositories

import com.flowbyte.core.Resource
import com.flowbyte.data.models.tracks.TrackResponse

interface TrackRepository {
    suspend fun getTrack(id: String): Resource<TrackResponse>
}