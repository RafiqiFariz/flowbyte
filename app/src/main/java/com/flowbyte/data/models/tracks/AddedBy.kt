package com.flowbyte.data.models.tracks

import com.flowbyte.data.models.playlist.ExternalUrls
import com.flowbyte.data.models.playlist.Followers
import com.google.gson.annotations.SerializedName

data class AddedBy(
    @SerializedName("external_urls")
    val externalUrls: ExternalUrls,
    val followers: Followers,
    val href: String,
    val id: String,
    val type: String,
    val uri: String
)