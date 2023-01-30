package com.example.sunnyweather.locgic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import retrofit2.http.Query
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object SunnyWeatherNetwork {
    //await() 为了不占时间 suspend 主要在携程
    private val placeService = ServiceCreator.create<PlaceService>()

    suspend fun searchPlaces(query: String)= placeService.searchPlaces(query).await()

    private suspend fun <T> Call<T>.await():T {
        //response:回答 Coroutine ：携程 suspend：悬
        return suspendCoroutine { continuation ->
            enqueue(object :Callback<T>{
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body!=null)
                        continuation.resume(body)
                    else
                        continuation.resumeWithException(RuntimeException("response is null"))
                }
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}