package com.hproject.banttang.di_factory

import com.hproject.banttang.BuildConfig
import com.hproject.banttang.data.user.data_source.local.IUserLocalDataSource
import com.hproject.banttang.data.user.data_source.local.UserLocalDataSource
import com.hproject.banttang.data.user.data_source.remote.IUserRemoteDataSource
import com.hproject.banttang.data.user.data_source.remote.dummy.UserDummyDataSource
import com.hproject.banttang.data.user.data_source.remote.retrofit.IUserRetrofitApi
import com.hproject.banttang.data.user.data_source.remote.retrofit.UserRemoteDataSource
import com.hproject.banttang.data.user.manager.UserManager
import com.hproject.banttang.data.user.repository.UserRemoteRepository
import com.hproject.banttang.domain.user.repository.IUserRepository
import com.hproject.banttang.domain.user.usecase.LoginUseCase
import com.hproject.banttang.domain.user.usecase.GetUserInfoUseCase
import com.hproject.banttang.domain.user.usecase.JoinUseCase
import com.hproject.banttang.domain.user.usecase.UpdateProfileUseCase
import com.hproject.banttang.domain.user.usecase.ReIssueUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

/**
 * 유저 관련 의존성 주입
 *
 * @author hjkim
 * @since 2023/02/16
 **/
@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    fun provideUpdateProfileUseCase(
        repository: IUserRepository
    ): UpdateProfileUseCase {
        return UpdateProfileUseCase(repository = repository)
    }
    @Provides
    fun provideJoinUseCase(
        repository: IUserRepository
    ): JoinUseCase {
        return JoinUseCase(repository = repository)
    }
    @Provides
    fun provideUseInfoUseCase(
        repository: IUserRepository
    ): GetUserInfoUseCase {
       return GetUserInfoUseCase(repository = repository)
    }

    @Provides
    fun provideLoginUseCase(
        repository: IUserRepository
    ): LoginUseCase {
        return LoginUseCase(
            repository = repository
        )
    }

    @Provides
    fun provideReIssueUseCase(
        repository: IUserRepository
    ): ReIssueUseCase {
        return ReIssueUseCase(
            repository = repository
        )
    }

    @Provides
    fun provideUserRepository(
        remoteDataSource: IUserRemoteDataSource,
        localDataSource: IUserLocalDataSource
    ): IUserRepository {
        return  UserRemoteRepository(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource
        )
    }

    @Provides
    fun provideUserLocalDataSource(userManager: UserManager): IUserLocalDataSource =
        UserLocalDataSource(userInfoManager = userManager)

    @Provides
    @Singleton
    fun provideUserManager(): UserManager  = UserManager()

    @Provides
    fun provideUserRemoteDataSource(
        userApi: IUserRetrofitApi
    ): IUserRemoteDataSource {
        return when {
            BuildConfig.IS_DUMMY -> {
                UserDummyDataSource()
            }
            else -> {
                UserRemoteDataSource(userApi)
            }
        }
    }

    @Provides
    fun provideUserApi(retrofit: Retrofit): IUserRetrofitApi {
        return retrofit.create()
    }
}