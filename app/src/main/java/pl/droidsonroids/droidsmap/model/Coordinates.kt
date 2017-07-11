package pl.droidsonroids.droidsmap.model

import com.google.android.gms.maps.model.LatLng

data class Coordinates(val latitude: Double, val longitude: Double) {
    companion object {
        fun from(latLng: LatLng) = Coordinates(latLng.latitude, latLng.longitude)
    }
}