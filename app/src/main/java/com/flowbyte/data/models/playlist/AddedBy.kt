package com.flowbyte.data.models.playlist

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