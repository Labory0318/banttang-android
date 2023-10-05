package com.hproject.banttang.di_factory

import com.hproject.banttang.BuildConfig
import com.hproject.banttang.data.version.data_source.remote.IVersionCheckRemoteDataSource
import com.hproject.banttang.data.version.data_source.remote.dummy.VersionCheckDummyRemoteDataSource
import com.hproject.banttang.data.version.data_source.remote.retrofit.IVersionCheckRetrofitApi
import com.hproject.banttang.data.version.data_source.remote.retrofit.VersionCheckRemoteDataSource
import com.hproject.banttang.data.version.repository.VersionCheckRemoteRepository
import com.hproject.banttang.domain.version.repository.IVersionCheckRepository
import com.hproject.banttang.domain.version.usecase.VersionCheckUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create

/**
 * 버전 체크 관련 의존성 주입
 *
 * @author hjkim
 * @since 2023/02/15
 **/
@Module
@InstallIn(SingletonComponent::class)
object VersionCheckModule {
    @Provides
    fun provideVersionCheckUseCase(
        repository: IVersionCheckRepository
    ): VersionCheckUseCase {
        return VersionCheckUseCase(
            repository = repository
        )
    }

    @Provides
    fun provideVersionCheckRepository(
        remoteDataSource: IVersionCheckRemoteDataSource
    ): IVersionCheckRepository {
        return VersionCheckRemoteRepository(
            remoteDataSource = remoteDataSource
        )
    }

    @Provides
    fun provideVersionCheckRemoteDataSource(
        versionCheckApi: IVersionCheckRetrofitApi
    ): IVersionCheckRemoteDataSource {
        return when {
            BuildConfig.IS_DUMMY -> {
                VersionCheckDummyRemoteDataSource()
            }
            else -> {
                VersionCheckRemoteDataSource(versionCheckApi)
            }
        }
    }

    @Provides
    fun provideVersionCheckApi(retrofit: Retrofit): IVersionCheckRetrofitApi {
        return retrofit.create()
    }
}