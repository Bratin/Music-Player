package com.bratin.musicplayer.di

import com.bratin.musicplayer.data.repoimpl.SongsRepositoryImpl
import com.bratin.musicplayer.domain.repository.SongsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepoModule {

    @Binds
    abstract fun bindWeatherRepository(songsRepositoryImpl: SongsRepositoryImpl): SongsRepository
}