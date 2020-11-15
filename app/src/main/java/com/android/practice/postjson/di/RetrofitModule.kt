package com.android.practice.postjson.di

import com.android.practice.postjson.util.BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object RetrofitModule {

     private fun createRetrofitBuilder(): Retrofit.Builder {
            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(100, TimeUnit.SECONDS)
                .connectTimeout(100, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        }

        @Provides
        @Singleton
        fun getApiClient(): Retrofit {
            val gson = GsonBuilder()
                .setLenient().create()

            return createRetrofitBuilder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
}