package com.example.sunnyweather.locgic


import androidx.lifecycle.liveData
import com.example.sunnyweather.locgic.dao.PlaceDao
import com.example.sunnyweather.locgic.model.Place
import com.example.sunnyweather.locgic.model.Weather
import com.example.sunnyweather.locgic.network.SunnyWeatherNetwork
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import retrofit2.http.Query
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

object Repository {
    //Dispatchers 调度器
    //上述代码中我们还将liveData()函数的线程参数类型指定成了
    //Dispatchers.IO，这样代码块中的所有代码就都运行在子线程中了。
//    fun searchPlaces(query: String) = liveData(Dispatchers.IO){
//        val result = try {
//            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
//            //判断如果服务器响应的状态是ok，那么就使用Kotlin内置的Result.success()方法来包装获
//            //取的城市数据列表，否则使用Result.failure()方法来包装一个异常信息。最后使用一个
//            //emit()方法将包装的结果发射出去，这个emit()方法其实类似于调用LiveData的
//            //setValue()方法来通知数据变化
//            if (placeResponse.status == "ok"){
//                val places = placeResponse.places
//                Result.success(places)
//            }else{
//                Result.failure(RuntimeException("reponse status is ${placeResponse.status}"))
//            }
//        }catch (e:Exception){
//            Result.failure<List<Place>>(e)
//        }
//        emit(result)
//    }
//    fun refreahWeather(lng : String,lat : String)= liveData(Dispatchers.IO){
//        val result = try {
//            coroutineScope {
//                val deferredRealtime = async {
//                    SunnyWeatherNetwork.getRealtimeResponse(lng , lat)
//                }
//                val deferrDaily = async {
//                    SunnyWeatherNetwork.getDailyResponse(lng ,lat)
//                }
//                val realtimeResponse = deferredRealtime.await()
//                val dailyResponse = deferrDaily.await()
//                if (realtimeResponse.status=="ok" && dailyResponse.status == "ok"){
//                    val weather = Weather(realtimeResponse.result.realtime,dailyResponse.result.daily)
//                    Result.success(weather)
//                }
//                else{
//                    Result.failure(RuntimeException("realtime status is ${realtimeResponse.status}"+
//                            "daily response status is ${dailyResponse.status}"))
//                }
//            }
//        }catch (e:Exception){
//            Result.failure<Weather>(e)
//        }
//        emit(result)
//    }
    fun searchPlaces(query: String) = fire(Dispatchers.IO){
        val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
        if (placeResponse.status == "ok"){
            val place = placeResponse.places
            Result.success(place)
        }else{
            Result.failure(RuntimeException("place status is ${placeResponse.status}"))
        }
    }
    //refresh 刷新
    fun refreshWeather(lng : String ,lat :String)= fire(Dispatchers.IO){
        //coroutineScope :协程示波器
        coroutineScope {
            val realtimeResponse = async {
                SunnyWeatherNetwork.getRealtimeResponse(lng, lat)
            }
            val dailyResponse =async {
                SunnyWeatherNetwork.getDailyResponse(lng, lat)
            }
            val realtime =realtimeResponse.await()
            val daily = dailyResponse.await()
            if (realtime.status=="ok" && daily.status=="ok"){
                val weather = Weather(realtime.result.realtime,daily.result.daily)
                Result.success(weather)
            }else{
                Result.failure(RuntimeException("realtime status is ${realtime.status}"+
                        "daily response status is ${daily.status}"))
            }
        }
    }
    private fun <T> fire(context: CoroutineContext,block :suspend () ->Result<T>)= liveData<Result<T>>(context){
        val result = try {
            block()
        }catch (e:Exception){
            Result.failure<T>(e)
        }
        emit(result)
    }

    fun savePlace(place: Place)=PlaceDao.savePlace(place)

    fun getSavePlace()=PlaceDao.getSavedPlace()

    fun isPlaceSaved()=PlaceDao.isPlaceSaved()
}

