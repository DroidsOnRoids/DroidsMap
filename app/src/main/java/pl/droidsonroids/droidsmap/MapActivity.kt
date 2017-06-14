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
import android.util.Log
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

    private var officeLeftTopCornerCoordinates = LatLng(51.10938699, 17.025564399)
    private lateinit var map: GoogleMap
    private lateinit var officeScene: Scene
    private val groundOverlayList = ArrayList<GroundOverlay>()
    private val roomsList = ArrayList<Room>()
    private var shouldMoveBack = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        createRoomList()
        officeScene = Scene(rootLayout, officeSceneLayout)

        latplus.setOnClickListener {
            officeLeftTopCornerCoordinates = LatLng(officeLeftTopCornerCoordinates.latitude + 0.000002, officeLeftTopCornerCoordinates.longitude)
            recalculateOfficeOverlays()
        }
        latminus.setOnClickListener {
            officeLeftTopCornerCoordinates = LatLng(officeLeftTopCornerCoordinates.latitude - 0.000002, officeLeftTopCornerCoordinates.longitude)
            recalculateOfficeOverlays()
        }
        longplus.setOnClickListener {
            officeLeftTopCornerCoordinates = LatLng(officeLeftTopCornerCoordinates.latitude, officeLeftTopCornerCoordinates.longitude + 0.000002)
            recalculateOfficeOverlays()
        }
        longminus.setOnClickListener {
            officeLeftTopCornerCoordinates = LatLng(officeLeftTopCornerCoordinates.latitude, officeLeftTopCornerCoordinates.longitude - 0.000002)
            recalculateOfficeOverlays()
        }
    }

    private fun recalculateOfficeOverlays() {
        Log.d("New coordinates", "$officeLeftTopCornerCoordinates")
        map.clear()
        roomsList.forEach { createMapOverlay(it) }
    }

    private fun createRoomList() {
        roomsList.add(Room(228f, 144f, 114f, 138f, "skybuds room", R.drawable.room_3))
        roomsList.add(Room(148f, 204f, 74f, 304f, "server room", R.drawable.room_2))
        roomsList.add(Room(100f, 168f, 50f, 454f, "server room2", R.drawable.room_2a))
        roomsList.add(Room(164f, 116f, 174f, 456f, "room 1", R.drawable.room_1))
        roomsList.add(Room(126f, 142f, 257f, 273f, "room 4", R.drawable.room_4))
        roomsList.add(Room(166f, 202f, 301f, 607f, "room m", R.drawable.room_m))
        roomsList.add(Room(144f, 202f, 448f, 607f, "room 5", R.drawable.room_5))
        roomsList.add(Room(256f, 204f, 562f, 304f, "room 6", R.drawable.room_6))
        roomsList.add(Room(130f, 70f, 377f, 371f, "wall 1", R.drawable.wall_1))
        roomsList.add(Room(146f, 202f, 585f, 607f, "room_7", R.drawable.room_7))
        roomsList.add(Room(194f, 406f, 759f, 203f, "room_fun", R.drawable.room_fun))
        roomsList.add(Room(206f, 202f, 753f, 607f, "room_9", R.drawable.room_9))
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
                .include(LatLng(officeLeftTopCornerCoordinates.latitude - 0.0005, officeLeftTopCornerCoordinates.longitude - 0.0005))
                .include(LatLng(officeLeftTopCornerCoordinates.latitude + 0.0005, officeLeftTopCornerCoordinates.longitude + 0.0005))
                .build()

        map.setLatLngBoundsForCameraTarget(bounds)
        map.uiSettings.isZoomControlsEnabled = false
        map.uiSettings.isCompassEnabled = false
        map.uiSettings.isMyLocationButtonEnabled = false

        val cameraPosition = CameraPosition.Builder()
                .bearing(MAP_BEARING)
                .target(officeLeftTopCornerCoordinates)
                .zoom(20f)
                .build()
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        map.moveCamera(cameraUpdate)

        map.moveCamera(CameraUpdateFactory.newLatLng(officeLeftTopCornerCoordinates))

        roomsList.forEach { createMapOverlay(it) }
    }

    private fun createMapOverlay(room: Room) {
        val overlayOptions = GroundOverlayOptions()
                .image(createMarkerBitmapDescriptor(room.getImageResource()))
                .transparency(0f)
                .bearing(MAP_BEARING)
                .position(LatLng(
                        officeLeftTopCornerCoordinates.latitude + room.getRelativeCenterLatitude(291.5f),
                        officeLeftTopCornerCoordinates.longitude + room.getRelativeCenterLongitude(291.5f)),
                        room.getRoomHeightMeters(), room.getRoomWidthMeters())
                .clickable(true)

        val overlay = map.addGroundOverlay(overlayOptions)
        overlay.tag = room.getTag()
        groundOverlayList.add(overlay)
    }

    private fun createMarkerBitmapDescriptor(imageResource: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(this, imageResource)
        val width = vectorDrawable.intrinsicWidth
        val height = vectorDrawable.intrinsicHeight
        vectorDrawable.setBounds(0, 0, width, height)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onGroundOverlayClick(groundOverlay: GroundOverlay) {
        val cameraPosition = CameraPosition.Builder()
                .bearing(MAP_BEARING)
                .target(groundOverlay.position)
                .zoom(MAX_MAP_ZOOM)
                .build()

        val roomImageResource = roomsList
                .filter { it.getTag() == groundOverlay.tag }
                .first()
                .getImageResource()

        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        map.animateCamera(cameraUpdate, CAMERA_TRANSITION_DURATION_MILLIS, CameraListenerAdapter({
            roomImage.setImageDrawable(ContextCompat.getDrawable(this@MapActivity,
                    roomImageResource
            ))
            roomImage.layoutParams.width = (ContextCompat.getDrawable(this, roomImageResource).intrinsicWidth * 2.2f).toInt()
            roomImage.layoutParams.height = (ContextCompat.getDrawable(this, roomImageResource).intrinsicHeight * 2.2f).toInt()



            map.addMarker(MarkerOptions().position(map.projection.visibleRegion.nearLeft).title("Near left"))
            map.addMarker(MarkerOptions().position(map.projection.visibleRegion.nearRight).title("Near right"))
            map.addMarker(MarkerOptions().position(map.projection.visibleRegion.farLeft).title("Near left"))
            map.addMarker(MarkerOptions().position(map.projection.visibleRegion.farRight).title("Near right"))
            performRoomTransition()
        }))
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