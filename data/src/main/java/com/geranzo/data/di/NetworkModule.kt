package com.geranzo.data.di

import android.util.Log
import com.geranzo.data.remote.GoRestApi
import com.geranzo.data.remote.RemoteDataSource
import com.geranzo.data.remote.RemoteDataSourceImpl
import com.geranzo.data.repository.RepositoryImpl
import com.geranzo.domain.repository.Repository
import com.geranzo.remote.BuildConfig
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.Credentials.basic
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
object NetworkModule {
    @JvmStatic
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val credentials = basic("Bearer", "379084cc5d1f046e4de6ac2a88d8cf6b84848e60bf065bcf122d262be49b9a9d")
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("Authorization", credentials)

                val request = requestBuilder.build()
                chain.proceed(request)
            }

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor { message ->
                Log.d("OkHttp", message)
            }.apply { level = HttpLoggingInterceptor.Level.BODY }

            builder.addInterceptor(interceptor)
        }

        return builder.build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideGoRestApi(okHttpClient: OkHttpClient): GoRestApi {
        return Retrofit.Builder()
            .baseUrl(GoRestApi.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().build()
                )
            )
            .build()
            .create(GoRestApi::class.java)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideRepository(repo: RepositoryImpl): Repository = repo

    @JvmStatic
    @Singleton
    @Provides
    fun provideRemoteDataSource(remoteDataSource: RemoteDataSourceImpl): RemoteDataSource = remoteDataSource
}
