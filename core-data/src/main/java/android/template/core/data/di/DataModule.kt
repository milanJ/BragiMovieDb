/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.template.core.data.di

import android.content.Context
import android.template.core.data.BuildConfig
import android.template.core.data.DefaultGenresRepository
import android.template.core.data.DefaultMoviesRepository
import android.template.core.data.GenresRepository
import android.template.core.data.MoviesRepository
import android.template.core.data.remote.ApiService
import android.template.core.data.remote.AuthorizationInterceptor
import android.template.core.data.remote.DefaultGenresRemoteDataSource
import android.template.core.data.remote.DefaultMoviesRemoteDataSource
import android.template.core.data.remote.GenresRemoteDataSource
import android.template.core.data.remote.MoviesRemoteDataSource
import android.template.core.data.remote.RetryInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.migration.DisableInstallInCheck
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [DataModuleBinds::class])
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideGenresRemoteDataSource(
        apiService: ApiService,
    ): GenresRemoteDataSource = DefaultGenresRemoteDataSource(apiService)

    @Singleton
    @Provides
    fun provideMoviesRemoteDataSource(
        apiService: ApiService,
    ): MoviesRemoteDataSource = DefaultMoviesRemoteDataSource(apiService)

    @Singleton
    @Provides
    fun provideApiService(
        gson: Gson,
        okHttpClient: OkHttpClient
    ): ApiService {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BuildConfig.TMDB_BASE_URL)
            .build()
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        okHttpClientCache: Cache
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
            .apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            }
        return OkHttpClient.Builder()
            .cache(okHttpClientCache)
            .addInterceptor(AuthorizationInterceptor(BuildConfig.TMDB_API_KEY))
            .addInterceptor(RetryInterceptor())
            .addInterceptor(loggingInterceptor)
            .connectTimeout(CONNECT_READ_WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(CONNECT_READ_WRITE_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(CONNECT_READ_WRITE_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClientCache(
        @ApplicationContext context: Context
    ): Cache = Cache(context.cacheDir, OK_HTTP_CLIENT_CACHE_SIZE)

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()
}

@Module
@DisableInstallInCheck
abstract class DataModuleBinds {

    @Singleton
    @Binds
    abstract fun bindsGenresRepository(
        genresRepository: DefaultGenresRepository
    ): GenresRepository

    @Singleton
    @Binds
    abstract fun bindsMoviesRepository(
        moviesRepository: DefaultMoviesRepository
    ): MoviesRepository
}

private const val OK_HTTP_CLIENT_CACHE_SIZE = 20L * 1024L * 1024L // 20 MB
private const val CONNECT_READ_WRITE_TIMEOUT = 30L // 30 seconds
