package com.example.sunnyweather.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.sunnyweather.locgic.Repository
import com.example.sunnyweather.locgic.model.Place
import retrofit2.http.Query

class PlaceViewModel : ViewModel() {
    private val searchLiveData = MutableLiveData<String>()

    val placeList = ArrayList<Place>()


    val placeLiveData = Transformations.switchMap(searchLiveData){query->
        Repository.searchPlaces(query)
    }
    fun searcahPlace(query: String){
        searchLiveData.value=query
    }
}