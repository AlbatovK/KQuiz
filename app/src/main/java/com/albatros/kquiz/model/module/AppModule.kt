package com.albatros.kquiz.model.module

import com.albatros.kquiz.model.api.ApiService
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val host_name = "https://floating-eyrie-81969.herokuapp.com"

private fun provideApiService(retrofit: Retrofit) =
    retrofit.create(ApiService::class.java)

private fun provideRetrofit(factory: GsonConverterFactory) = Retrofit.Builder()
    .baseUrl(host_name)
    .addConverterFactory(factory)
    .build()

private fun provideGsonFactory(gson: Gson) =
    GsonConverterFactory.create(gson)

private fun provideGson() = GsonBuilder()
    .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
    .setPrettyPrinting()
    .serializeNulls()
    .create()

val appModule = module {
    single { provideApiService(get()) }
    single { provideRetrofit(get()) }
    single { provideGsonFactory(get()) }
    single { provideGson() }
}