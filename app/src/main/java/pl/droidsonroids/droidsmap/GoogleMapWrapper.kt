package pl.droidsonroids.droidsmap

import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.Projection
import com.google.android.gms.maps.model.GroundOverlay
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class GoogleMapWrapper(val googleMap: GoogleMap) {

    val projection: Projection
        get() = googleMap.projection

    var isMyLocationEnabled: Boolean
    get() = googleMap.isMyLocationEnabled
    set(enabled) {
        googleMap.isMyLocationEnabled = enabled
    }

    fun animateCamera(cameraUpdate: CameraUpdate, var2: Int, callback: GoogleMap.CancelableCallback) {
        googleMap.animateCamera(cameraUpdate, var2, callback)
    }

    fun setup(minZoom: Float) {
        googleMap.setup(minZoom)
    }

    fun setOnGroundOverlayClickListener(groundOverlayCallback: (GroundOverlay) -> Unit) {
        googleMap.setOnGroundOverlayClickListener(groundOverlayCallback)
    }

    fun addMarker(markerOptions: MarkerOptions) {
        googleMap.addMarker(markerOptions)
    }

    fun setLatLngBoundsForCameraTarget(latLngBounds: LatLngBounds) {
        googleMap.setLatLngBoundsForCameraTarget(latLngBounds)
    }

    fun moveCamera(cameraUpdate: CameraUpdate) {
        googleMap.moveCamera(cameraUpdate)
    }

    private fun GoogleMap.setup(minZoom: Float) {
        with(uiSettings) {
            isZoomControlsEnabled = false
            isCompassEnabled = false
            isMyLocationButtonEnabled = false
        }
        this.setMinZoomPreference(minZoom)
        this.mapType = GoogleMap.MAP_TYPE_TERRAIN
        this.isBuildingsEnabled = true
    }
}
