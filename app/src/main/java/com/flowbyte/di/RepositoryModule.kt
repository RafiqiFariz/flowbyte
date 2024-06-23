package com.flowbyte.di

import com.flowbyte.data.repositories.AlbumRepositoryImpl
import com.flowbyte.data.repositories.AuthRepositoryImpl
import com.flowbyte.data.repositories.PlaylistRepositoryImpl
import com.flowbyte.data.repositories.SearchRepositoryImpl
import com.flowbyte.data.repositories.TrackRepositoryImpl
import com.flowbyte.data.repositories.UserRepositoryImpl
import com.flowbyte.domain.repositories.AlbumRepository
import com.flowbyte.domain.repositories.AuthRepository
import com.flowbyte.domain.repositories.PlaylistRepository
import com.flowbyte.domain.repositories.TrackRepository
import com.flowbyte.domain.repositories.UserRepository
import com.flowbyte.domain.repositories.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAlbumRepository(
        albumRepositoryImpl: AlbumRepositoryImpl
    ): AlbumRepository

    @Binds
    abstract fun bindSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    abstract fun bindTrackRepository(
        trackRepositoryImpl: TrackRepositoryImpl
    ): TrackRepository

    @Binds
    abstract fun bindPlaylistRepository(
        playlistRepositoryImpl: PlaylistRepositoryImpl
    ): PlaylistRepository

    @Binds
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}