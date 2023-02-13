package com.example.sunnyweather.locgic.network

import com.example.sunnyweather.SunnyWeatherAppLication
import com.example.sunnyweather.locgic.model.DailyResponse
import com.example.sunnyweather.locgic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {
    @GET("v2.5/${SunnyWeatherAppLication.TOKEN}/{lng},{lat}/realtime.json")

    fun getRealtimeResponse(@Path("lng") lng : String, @Path("lat") lat : String) : Call<RealtimeResponse>

    @GET("v2.5/${SunnyWeatherAppLication.TOKEN}/{lng},{lat}/daily.json")

    fun getDailyResponse(@Path("lng") lng: String ,@Path ("lat") lat : String) : Call<DailyResponse>
}