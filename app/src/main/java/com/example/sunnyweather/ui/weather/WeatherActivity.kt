package com.example.sunnyweather.ui.weather

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sunnyweather.R
import com.example.sunnyweather.locgic.model.Weather
import com.example.sunnyweather.locgic.model.getSky
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.forecast.*
import kotlinx.android.synthetic.main.life_item.*
import kotlinx.android.synthetic.main.now.*
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider ( this) .get(WeatherViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Decor 装饰
        //我们调用了getWindow().getDecorView()方法拿到当前Activity的DecorView，再调用它
        //的setSystemUiVisibility()方法来改变系统UI的显示，这里传入
        //View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN和
        //View.SYSTEM_UI_FLAG_LAYOUT_STABLE就表示Activity的布局会显示在状态栏上面，最后
        //调用一下setStatusBarColor()方法将状态栏设置成透明色。



        setContentView(R.layout.activity_weather)
        if (viewModel.locationLng.isEmpty()){
            viewModel.locationLng=intent.getStringExtra("location_lng")?:""
        }
        if (viewModel.locationLat.isEmpty()){
            viewModel.locationLat=intent.getStringExtra("location_lat")?:""
        }
        if (viewModel.placeName.isEmpty()){
            viewModel.placeName=intent.getStringExtra("place_name")?:""
        }
        viewModel.weatherLiveData.observe(this , Observer { result->
            val weather = result.getOrNull()
            if (weather !=null) {
                showWeather(weather)
            } else{
                Toast.makeText(this,"无法获取天气信息",Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
        viewModel.refreshWeather(viewModel.locationLng,viewModel.locationLat)
    }

    private fun showWeather(weather: Weather) {
        placeName.text=viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily

        val currentTempText = "${realtime.temperature.toInt()}℃"
        currentTemp.text=currentTempText
        currentSky.text= getSky(realtime.skycon).info
        val currentPM25Text = "空气指数${realtime.airQuality.api.chn.toInt()}"
        currentAQI.text=currentPM25Text
        nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
        forecastLayout.removeAllViews()
        val days =daily.skycon.size
        for (i in 0 until days){
            val skycon=daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false)
            val dataInfo = view.findViewById<TextView>(R.id.dataInfo)
            val skyIcon=view.findViewById<ImageView>(R.id.skyIcon)
            val skyInfo = view.findViewById<TextView>(R.id.skyInfo)
            val temperatureInfo = view.findViewById<TextView>(R.id.temperatureInfo)
            val simpleDateFormat=SimpleDateFormat("yyy-MM-dd", Locale.getDefault())
            dataInfo.text=simpleDateFormat.format(skycon.date)
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text=sky.info
            val tempText ="${temperature.min.toInt()}~${temperature.max.toInt()}"
            temperatureInfo.text=tempText
            forecastLayout.addView(view)
        }
        val lifeIndex = daily.lifeIndex
        coldRiskText.text=lifeIndex.coldRisk[0].desc
        dressingText.text=lifeIndex.dressing[0].desc
        ultravioletText.text=lifeIndex.ultraviolet[0].desc
        carwashingText.text=lifeIndex.carWashing[0].desc
        weatherLayout.visibility=View.VISIBLE
    }
}