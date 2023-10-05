package com.hproject.banttang.di_factory

import com.hproject.banttang.BuildConfig
import com.hproject.banttang.data.file.data_source.remote.IFileRemoteDataSource
import com.hproject.banttang.data.file.data_source.remote.dummy.FileDummyDataSource
import com.hproject.banttang.data.file.data_source.remote.retrofit.FileRemoteDataSource
import com.hproject.banttang.data.file.data_source.remote.retrofit.IFileRetrofitApi
import com.hproject.banttang.data.file.repository.FileRemoteRepository
import com.hproject.banttang.domain.file.repository.IFileRepository
import com.hproject.banttang.domain.file.usecase.UpdateImageUseCase
import com.hproject.banttang.domain.user.repository.IUserRepository
import com.hproject.banttang.domain.user.usecase.GetUserInfoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
object FileModule {

    @Provides
    fun provideUseInfoUseCase(
        repository: IFileRepository
    ): UpdateImageUseCase {
        return UpdateImageUseCase(repository = repository)
    }

    @Provides
    fun provideUserRepository(
        remoteDataSource: IFileRemoteDataSource,
    ): IFileRepository = FileRemoteRepository(remoteDatasource = remoteDataSource)

    @Provides
    fun provideUserRemoteDataSource(
        fileApi: IFileRetrofitApi
    ): IFileRemoteDataSource = when {
            BuildConfig.IS_DUMMY -> FileDummyDataSource()
            else -> FileRemoteDataSource(fileApi)
        }

    @Provides
    fun provideUserApi(retrofit: Retrofit): IFileRetrofitApi {
        return retrofit.create()
    }
}