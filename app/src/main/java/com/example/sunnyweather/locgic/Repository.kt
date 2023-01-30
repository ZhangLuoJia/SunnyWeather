package com.example.sunnyweather.locgic


import androidx.lifecycle.liveData
import com.example.sunnyweather.locgic.model.Place
import com.example.sunnyweather.locgic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.lang.RuntimeException

object Repository {
    //Dispatchers 调度器
    //上述代码中我们还将liveData()函数的线程参数类型指定成了
    //Dispatchers.IO，这样代码块中的所有代码就都运行在子线程中了。
    fun searchPlaces(query: String) = liveData(Dispatchers.IO){
        val result = try {
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
            //判断如果服务器响应的状态是ok，那么就使用Kotlin内置的Result.success()方法来包装获
            //取的城市数据列表，否则使用Result.failure()方法来包装一个异常信息。最后使用一个
            //emit()方法将包装的结果发射出去，这个emit()方法其实类似于调用LiveData的
            //setValue()方法来通知数据变化
            if (placeResponse.status == "ok"){
                val places = placeResponse.places
                Result.success(places)
            }else{
                Result.failure(RuntimeException("reponse status is ${placeResponse.status}"))
            }
        }catch (e:Exception){
            Result.failure<List<Place>>(e)
        }
        emit(result)
    }
}

