package com.hproject.banttang.di_factory

import android.content.Context
import com.hproject.banttang.BanttangApplication
import com.hproject.core.data.data_source.local.data_store.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun providePreferenceManager(
        @ApplicationContext context : Context
    ) : PreferenceManager = PreferenceManager(context)
}