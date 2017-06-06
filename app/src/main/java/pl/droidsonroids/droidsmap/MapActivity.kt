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

    private val droidsOnRoidsLocation: LatLng = LatLng(51.10944382158668, 17.0255388552323)
    private val CAMERA_TRANSITION_DURATION_MILLIS = 500
    private lateinit var map: GoogleMap

    private lateinit var officeScene: Scene
    private val groundOverlayList = ArrayList<GroundOverlay>()
    val MAP_BEARING = 201.5f
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
        googleMap.setMinZoomPreference(18f)
        map.isBuildingsEnabled = true
        map.setOnGroundOverlayClickListener(this)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 200)
            }
        }

        val bounds = LatLngBounds.builder()
                .include(LatLng(droidsOnRoidsLocation.latitude - 0.0005, droidsOnRoidsLocation.longitude - 0.0005))
                .include(LatLng(droidsOnRoidsLocation.latitude + 0.0005, droidsOnRoidsLocation.longitude + 0.0005))
                .build()

        map.setLatLngBoundsForCameraTarget(bounds)
        map.uiSettings.isZoomControlsEnabled = false
        map.uiSettings.isCompassEnabled = false
        map.uiSettings.isMyLocationButtonEnabled = false

        val cameraPosition = CameraPosition.Builder()
                .bearing(MAP_BEARING)
                .target(droidsOnRoidsLocation)
                .zoom(20f)
                .build()
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        map.moveCamera(cameraUpdate)

        val room3Overlay = GroundOverlayOptions()
                .image(createMarkerBitmapDescriptor())
                .transparency(0f)
                .bearing(MAP_BEARING)
                .position(droidsOnRoidsLocation, 6.90f, 4.30f)
                .clickable(true)

        val overlay = map.addGroundOverlay(room3Overlay)
        overlay.tag = "skybuds_room"
        groundOverlayList.add(overlay)

        map.addMarker(MarkerOptions().position(droidsOnRoidsLocation).title("Droids on Roids marker"))
        map.moveCamera(CameraUpdateFactory.newLatLng(droidsOnRoidsLocation))
    }

    private fun createMarkerBitmapDescriptor(): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(this, R.drawable.room_3)
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
                    .zoom(25f)
                    .build()

            val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
            map.animateCamera(cameraUpdate, CAMERA_TRANSITION_DURATION_MILLIS, object : GoogleMap.CancelableCallback {
                override fun onFinish() {
                    roomImage.setImageDrawable(ContextCompat.getDrawable(this@MapActivity, R.drawable.room_3))
                    performRoomTransition()
                }

                override fun onCancel() {
                    //no-op
                }
            })
        }
    }

    fun performRoomTransition() {
        shouldMoveBack = false
        roomImage.transitionName = "room_transition"
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
            200 -> {
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
        kotlin.run { endAction() }
    }
}