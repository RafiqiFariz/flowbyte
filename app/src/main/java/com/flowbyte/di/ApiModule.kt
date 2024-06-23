package com.flowbyte.di

import com.flowbyte.data.remote.AlbumApi
import com.flowbyte.data.remote.AuthApi
import com.flowbyte.data.remote.PlaylistApi
import com.flowbyte.data.remote.SearchApi
import com.flowbyte.data.remote.TrackApi
import com.flowbyte.data.remote.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    fun provideAlbumApi(@Named("retrofit") retrofit: Retrofit): AlbumApi =
        retrofit.create(AlbumApi::class.java)

    @Provides
    fun provideSearchApi(@Named("retrofit") retrofit: Retrofit): SearchApi =
        retrofit.create(SearchApi::class.java)

    @Provides
    fun provideUserApi(@Named("retrofit") retrofit: Retrofit): UserApi =
        retrofit.create(UserApi::class.java)

    @Provides
    fun provideTrackApi(@Named("retrofit") retrofit: Retrofit): TrackApi =
        retrofit.create(TrackApi::class.java)

    @Provides
    fun providePlaylistApi(@Named("retrofit") retrofit: Retrofit): PlaylistApi =
        retrofit.create(PlaylistApi::class.java)

    @Provides
    fun provideAuthApi(@Named("auth_retrofit") retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)
}