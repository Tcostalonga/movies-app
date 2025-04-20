package costalonga.tarsila.moviesapp.movie.data.api

import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object MovieApiInstance {
    private const val BASE_URL = "https://www.omdbapi.com/"

    private val netWorkJson = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(netWorkJson.asConverterFactory("application/json".toMediaType()))

    private val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()

    private val loggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BASIC)

    private val authenticationInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("apikey", "1f20bbd6")
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        chain.proceed(newRequest)
    }

    fun <T> createService(serviceClass: Class<T>): T {
        with(httpClient) {
            interceptors().clear()
            addInterceptor(loggingInterceptor)
            addInterceptor(authenticationInterceptor)
        }
        retrofit.client(httpClient.build())

        return retrofit.build().create(serviceClass)
    }
}