package com.example.covidapp.network

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://corona.lmao.ninja/v2/"

val countriesDataService: CountriesService =
    ApiClient.getClient().create(CountriesService::class.java)


private const val REQUEST_TIMEOUT = 60
private var okHttpClient: OkHttpClient? = null

private fun initOkHttp() {
    val httpClient = OkHttpClient().newBuilder()
        .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    httpClient.addInterceptor(interceptor)
    httpClient.addInterceptor { chain ->
        val original: Request = chain.request()
        val requestBuilder: Request.Builder = original.newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Request-Type", "Android")
            .addHeader("Content-Type", "application/json")
        val request: Request = requestBuilder.build()
        chain.proceed(request)
    }
    okHttpClient = httpClient.build()
}


object ApiClient {
    fun getClient(): Retrofit {
        if (okHttpClient == null) {
            initOkHttp()
        }

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }
}

interface CountriesService {
//    @GET("countries/{query}")
//    fun getCountry(@Path("query") query: String): Single<NetworkCountriesData>

    @GET("countries")
    fun getCountries(): Single<List<NetworkCountriesData>>
}