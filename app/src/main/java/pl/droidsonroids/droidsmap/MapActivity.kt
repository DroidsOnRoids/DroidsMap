package pl.droidsonroids.droidsmap

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.transition.Scene
import android.transition.Transition
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.scene_office_map.*
import java.util.*

class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnGroundOverlayClickListener {

    companion object {
        private val LOCATION_REQUEST_CODE = 1
        private val CAMERA_TRANSITION_DURATION_MILLIS = 300
        private val MAP_BEARING = 201.5f
        private val MIN_MAP_ZOOM = 18f
        private val MAX_MAP_ZOOM = 25f
        private val ROOM_TRANSITION_NAME = "room_transition"
    }

    private val room3Location: LatLng = LatLng(51.10944082158668, 17.0255388552323)
    private val room2Location: LatLng = LatLng(51.10948273424337, 17.02558437671076)

    private lateinit var map: GoogleMap
    private lateinit var officeScene: Scene
    private val groundOverlayList = ArrayList<GroundOverlay>()
    private var shouldMoveBack = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        officeScene = Scene(rootLayout, officeSceneLayout)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        googleMap.setMinZoomPreference(MIN_MAP_ZOOM)
        googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        map.isBuildingsEnabled = true
        map.setOnGroundOverlayClickListener(this)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
            }
        }

        val bounds = LatLngBounds.builder()
                .include(LatLng(room3Location.latitude - 0.0005, room3Location.longitude - 0.0005))
                .include(LatLng(room3Location.latitude + 0.0005, room3Location.longitude + 0.0005))
                .build()

        map.setLatLngBoundsForCameraTarget(bounds)
        map.uiSettings.isZoomControlsEnabled = false
        map.uiSettings.isCompassEnabled = false
        map.uiSettings.isMyLocationButtonEnabled = false

        val cameraPosition = CameraPosition.Builder()
                .bearing(MAP_BEARING)
                .target(room3Location)
                .zoom(20f)
                .build()
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        map.moveCamera(cameraUpdate)

        val room3OverlayOptions = GroundOverlayOptions()
                .image(createMarkerBitmapDescriptor(R.drawable.room_3))
                .transparency(0f)
                .bearing(MAP_BEARING)
                .position(room3Location, 7.6f, 4.8f)
                .clickable(true)

        val room3Overlay = map.addGroundOverlay(room3OverlayOptions)
        room3Overlay.tag = "skybuds_room"
        groundOverlayList.add(room3Overlay)

        val room2OverlayOptions = GroundOverlayOptions()
                .image(createMarkerBitmapDescriptor(R.drawable.room_2))
                .transparency(0f)
                .bearing(MAP_BEARING)
                .position(room2Location, 4.9333333333f, 6.8f)
                .clickable(true)

        val room2Overlay = map.addGroundOverlay(room2OverlayOptions)
        room2Overlay.tag = "server_room"
        groundOverlayList.add(room2Overlay)

        map.addMarker(MarkerOptions().position(room3Location).title("Droids on Roids marker"))
        map.moveCamera(CameraUpdateFactory.newLatLng(room3Location))
    }

    private fun createMarkerBitmapDescriptor(resourceId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(this, resourceId)
        val width = vectorDrawable.intrinsicWidth
        val height = vectorDrawable.intrinsicHeight
        vectorDrawable.setBounds(0, 0, width, height)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onGroundOverlayClick(groundOverlay: GroundOverlay) {
        if (groundOverlay.tag == "skybuds_room") {
            Toast.makeText(this, "Skybuds room overlay clicked!", Toast.LENGTH_SHORT).show()

            val cameraPosition = CameraPosition.Builder()
                    .bearing(MAP_BEARING)
                    .target(groundOverlay.position)
                    .zoom(MAX_MAP_ZOOM)
                    .build()

            val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
            map.animateCamera(cameraUpdate, CAMERA_TRANSITION_DURATION_MILLIS, CameraListenerAdapter({
                roomImage.setImageDrawable(ContextCompat.getDrawable(this@MapActivity, R.drawable.room_3))
                performRoomTransition()
            }))
        } else if (groundOverlay.tag == "server_room") {
            Toast.makeText(this, "Server room overlay clicked!", Toast.LENGTH_SHORT).show()

            val cameraPosition = CameraPosition.Builder()
                    .bearing(MAP_BEARING)
                    .target(groundOverlay.position)
                    .zoom(MAX_MAP_ZOOM)
                    .build()

            val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
            map.animateCamera(cameraUpdate, CAMERA_TRANSITION_DURATION_MILLIS, CameraListenerAdapter({
                roomImage.setImageDrawable(ContextCompat.getDrawable(this@MapActivity, R.drawable.room_2))
                performRoomTransition()
            }))
        }
    }

    fun performRoomTransition() {
        shouldMoveBack = false
        roomImage.transitionName = ROOM_TRANSITION_NAME
        val roomScene = Scene.getSceneForLayout(rootLayout, R.layout.scene_room, this)
        roomScene.setEnterAction {
            val roomSceneImage = (roomScene.sceneRoot.findViewById(R.id.zoomedRoomImage) as ImageView)
            roomSceneImage.setImageDrawable(roomImage.drawable)
        }
        val sceneTransition = TransitionInflater.from(this).inflateTransition(R.transition.room_scene_enter_transition)
        TransitionManager.go(roomScene, sceneTransition)
    }

    private fun performOfficeTransition() {
        val sceneTransition = TransitionInflater.from(this).inflateTransition(R.transition.room_scene_exit_transition)
        sceneTransition.addListener(TransitionListenerAdapter({
            roomImage.transitionName = ""
            roomImage.setImageDrawable(null)
        }))
        TransitionManager.go(officeScene, sceneTransition)
    }

    override fun onBackPressed() {
        if (shouldMoveBack) {
            super.onBackPressed()
        } else {
            performOfficeTransition()
            shouldMoveBack = true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    map.isMyLocationEnabled = true
                } else {
                    Toast.makeText(this, "Grant the permission!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

private class TransitionListenerAdapter(private val endAction: () -> Unit) : Transition.TransitionListener {

    override fun onTransitionResume(transition: Transition?) {
        //no-op
    }

    override fun onTransitionPause(transition: Transition?) {
        //no-op
    }

    override fun onTransitionCancel(transition: Transition?) {
        //no-op
    }

    override fun onTransitionStart(transition: Transition?) {
        //no-op
    }

    override fun onTransitionEnd(transition: Transition?) {
        endAction.invoke()
    }
}

private class CameraListenerAdapter(private val endAction: () -> Unit) : GoogleMap.CancelableCallback {
    override fun onFinish() {
        endAction.invoke()
    }

    override fun onCancel() {
        //no-op
    }
}