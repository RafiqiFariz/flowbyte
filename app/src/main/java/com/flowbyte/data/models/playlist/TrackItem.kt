package com.flowbyte.data.models.playlist

import com.flowbyte.data.models.tracks.TrackResponse
import com.google.gson.annotations.SerializedName

data class TrackItem(
    @SerializedName("added_at")
    val addedAt: String,
    @SerializedName("added_by")
    val addedBy: AddedByItem,
    @SerializedName("is_local")
    val isLocal: Boolean,
    val track: TrackResponse
)