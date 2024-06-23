package com.flowbyte.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowbyte.core.AppPreferences
import com.flowbyte.core.Resource
import com.flowbyte.data.models.playlist.FeaturedPlaylistsResponse
import com.flowbyte.data.models.playlist.Item
import com.flowbyte.domain.repositories.AuthRepository
import com.flowbyte.domain.repositories.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val playlistRepository: PlaylistRepository
) : ViewModel() {
    private val _spotifyToken = MutableLiveData<String>()
    val spotifyToken: LiveData<String> = _spotifyToken

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val _playlists = MutableLiveData<Resource<FeaturedPlaylistsResponse>>()
    val playlists: LiveData<Resource<FeaturedPlaylistsResponse>> = _playlists

    private val _selectedPlaylist = MutableLiveData<Item>()

    fun authorize() {
        viewModelScope.launch {
            try {
                val token = authRepository.getSpotifyToken()
                _spotifyToken.value = token
            } catch (e: Exception) {
                Log.e("SpotifyViewModel", "Failed to fetch Spotify token: ${e.message}")
            }
        }
    }

    fun fetchFeaturedPlaylists(limit: Int? = null, offset: Int? = null) {
        viewModelScope.launch {
            _playlists.value = playlistRepository.getFeaturedPlaylists(limit, offset)
        }
    }

    val selectedPlaylist: LiveData<Item>
        get() = _selectedPlaylist

    fun setSelectedPlaylist(item: Item) {
        _selectedPlaylist.value = item
    }
}