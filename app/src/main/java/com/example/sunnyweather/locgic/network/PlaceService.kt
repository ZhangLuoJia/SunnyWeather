package com.example.sunnyweather.locgic.network

import com.example.sunnyweather.SunnyWeatherAppLication
import com.example.sunnyweather.locgic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface PlaceService{

    @GET("v2/place?token=${SunnyWeatherAppLication.TOKEN}&lang=zh_CN")

    fun searchPlaces(@Query("query") query: String):Call<PlaceResponse>

}