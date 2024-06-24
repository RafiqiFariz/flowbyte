package com.flowbyte.ui.home.playlist_detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowbyte.core.Resource
import com.flowbyte.data.models.playlist.Item
import com.flowbyte.domain.repositories.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomePlaylistDetailViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository
) : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is home playlist detail"
    }
    val text: LiveData<String> = _text

    private val _details = MutableLiveData<Resource<Item>>()
    val details: LiveData<Resource<Item>> = _details

    fun setId(id: String) {
        viewModelScope.launch {
            val result = playlistRepository.getPlaylist(id)
            if (result is Resource.Success) {
                _details.value = result
                Log.d("Playlist Detail Data: ", details.toString())
            } else {
                Log.d("Error", "Unable to fetch playlist detail")
            }
        }
    }
}