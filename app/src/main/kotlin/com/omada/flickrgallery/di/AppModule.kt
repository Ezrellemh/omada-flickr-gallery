package com.omada.flickrgallery.di

import com.omada.flickrgallery.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @Named("apiKey")
    fun provideFlickrApiKey(): String = BuildConfig.FLICKR_API_KEY
}
