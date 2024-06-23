package com.flowbyte.data.models.search

import android.provider.MediaStore.Audio.Artists
import com.flowbyte.data.models.albums.Tracks

data class SearchResponse(
    val artists: Artists,
    val tracks: Tracks
)
