package edu.fullerton.ecs.mdap.intersectioninfo.geocoding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.fullerton.ecs.mdap.intersectioninfo.services.MapboxService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

/**
 * ViewModel for data displayed on the map fragment.
 */
class GeocodingViewModel: ViewModel() {

    // Backing property for address
    private val _address = MutableLiveData("")

    // Valid address returned by MapBox.
    val address: LiveData<String>
        get() {
            return _address
        }

    // Backing property for imageURL
    private val _imageURL = MutableLiveData("")

    // URL of the address' image
    val imageURL: LiveData<String>
        get() {
            return _imageURL
        }

    /**
     * Calls Mapbox's Geocoding API to retrieve the closest matching
     * address for the given search string
     *
     * @param searchString string to match to an address
     */
    fun geoCode(searchString: String) {
        viewModelScope.launch {
            MapboxService.GeoCoding.Api.retrofitService.getPlaces(searchString).enqueue(
                object : Callback, retrofit2.Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        _address.value = response.body()
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        _address.value = "Failure ${t.message}"
                    }
                })
        }
    }
}