package com.hproject.banttang.di_factory

import com.google.gson.GsonBuilder
import com.hproject.banttang.BuildConfig
import com.hproject.core.data.authorization.TokenManager
import com.hproject.core.data.data_source.remote.retrofit.interceptor.AuthorizationTokenInterceptor
import com.hproject.core.data.data_source.remote.retrofit.interceptor.NetworkPrettyLogger
import com.hproject.core.domain.authorization.ITokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


/**
 * Network Module 의존성 주입
 *
 * @author hjkim
 * @since 2023/02/14
 **/
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    /**
     * NetworkClient 의존성 주입
     *
     * @author hjkim
     * @since 2023/02/14
     **/
    @Provides
    @Singleton
    fun provideNetworkClient(
        httpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BANTTANG_API_SERVER_BASE_URL)
        .client(httpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()

    /**
     * HttpClient 의존성 주입
     *
     * @param tokenInterceptor  authorization token interceptor
     * @author hjkim
     * @since 2023/02/14
     **/
    @Provides
    @Singleton
    fun provideHttpClient(
        tokenInterceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(tokenInterceptor)
        .addNetworkInterceptor(loggingInterceptor)
        .build()

    /**
     * Auth Token Interceptor 의존성 주입
     *
     * @param tokenManager token 관리 객체
     * @author hjkim
     * @since 2023/02/14
     **/
    @Provides
    @Singleton
    fun provideTokenInterceptor(
        tokenManager: ITokenManager
    ): Interceptor = AuthorizationTokenInterceptor(
        tokenManager = tokenManager
    )

    /**
     * 토큰 관리 매니저 의존성 주입
     *
     * @author hjkim
     * @since 2023/02/14
     **/
    @Provides
    @Singleton
    fun provideTokenManager(): ITokenManager {
        return TokenManager.instance
    }

    /**
     * Network Logging Interceptor 의존성 주입
     *
     * @author hjkim
     * @since 2023/02/14
     **/
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(
            logger = NetworkPrettyLogger()
        ).setLevel(
            if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        )
    }

    /**
     * Http Parse 의존성 주입
     *
     * @author hjkim
     * @since 2023/02/14
     **/
    @Provides
    @Singleton
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create(GsonBuilder().create())
    }
}