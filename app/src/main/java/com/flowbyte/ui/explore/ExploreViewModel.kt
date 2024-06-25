package com.flowbyte.ui.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowbyte.core.Resource
import com.flowbyte.data.ListArtistBasedByGenreReponse
import com.flowbyte.data.models.genres.GenreResponse
import com.flowbyte.data.models.playlist.FeaturedPlaylistsResponse
import com.flowbyte.domain.repositories.GenreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val genreRepository: GenreRepository
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    private val _genres = MutableLiveData<Resource<GenreResponse>>()
    val genres: LiveData<Resource<GenreResponse>> = _genres

    fun fetchGenres() {
        viewModelScope.launch {
            _genres.value = genreRepository.getGenres()
        }
    }
}