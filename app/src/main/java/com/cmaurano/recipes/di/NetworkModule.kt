package com.cmaurano.recipes.di

import android.util.Log
import com.cmaurano.recipes.BuildConfig
import com.cmaurano.recipes.data.api.Api
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val PUBLIC_KEY = "22283558cde77ae495e143e73360d168"
    private const val PRIVATE_KEY = "2d063db326a07cb9cba50129888e9698fee1c3ec"
    private const val HOST = "https://gateway.marvel.com/v1/public/"

    @Provides
    fun provideBaseUrl() = HOST

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addMarvelApiParams()
            .build()
    } else {
        OkHttpClient
            .Builder()
            .addMarvelApiParams()
            .build()
    }

    @ExperimentalSerializationApi
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(jsonSerialization.asConverterFactory("application/json".toMediaType()))
        .client(okHttpClient)
        .build()
        .create(Api::class.java)

    private fun toMD5Hash(toBeEncrypt: String): String {
        var pass = toBeEncrypt
        var encryptedString: String? = null
        val md5: MessageDigest

        try {
            md5 = MessageDigest.getInstance("MD5")
            md5.update(pass.toByteArray(), 0, pass.length)
            pass = BigInteger(1, md5.digest()).toString(16)
            while (pass.length < 32) {
                pass = "0$pass"
            }
            encryptedString = pass
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        Log.d(this::class.simpleName, "hash -> $encryptedString")
        return encryptedString ?: ""
    }

    private fun OkHttpClient.Builder.addMarvelApiParams(): OkHttpClient.Builder {
        return addInterceptor { chain ->
            val currentTimestamp = System.currentTimeMillis()
            val newUrl = chain.request().url
                .newBuilder()
                .addQueryParameter("ts", currentTimestamp.toString())
                .addQueryParameter("apikey", PUBLIC_KEY)
                .addQueryParameter("hash", toMD5Hash(currentTimestamp.toString() + PRIVATE_KEY + PUBLIC_KEY))
                .build()

            val newRequest = chain.request()
                .newBuilder()
                .url(newUrl)
                .build()
            chain.proceed(newRequest)
        }
    }

    val jsonSerialization =
        Json {
            ignoreUnknownKeys = true
        }
}