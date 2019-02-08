package com.my.demo.network

import com.my.demo.discover.Restaurant
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object RetrofitClientInstance {
    private const val BASE_URL = "https://api.doordash.com/v2/"
    val retrofitClient: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

interface DoorDashApi {
    @GET("restaurant")
    fun getRestaurantsByLocation(
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("offset") offset: String,
        @Query("limit") limit: String
    ): Single<List<Restaurant>>
}
