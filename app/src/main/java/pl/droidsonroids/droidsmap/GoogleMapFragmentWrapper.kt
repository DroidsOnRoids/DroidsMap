package pl.droidsonroids.droidsmap

import com.google.android.gms.maps.SupportMapFragment


class GoogleMapFragmentWrapper(val googleMapFragment: SupportMapFragment) {

    fun getMapAsync(onMapReadyCallback: (GoogleMapWrapper) -> Unit) {
        googleMapFragment.getMapAsync { googleMap ->
            onMapReadyCallback.invoke(GoogleMapWrapper(googleMap))
        }
    }
}