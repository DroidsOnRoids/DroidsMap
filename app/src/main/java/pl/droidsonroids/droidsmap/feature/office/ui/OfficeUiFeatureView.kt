package pl.droidsonroids.droidsmap.feature.office.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.transition.Scene
import android.transition.Transition
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.scene_office_map.*
import pl.droidsonroids.droidsmap.MapActivity
import pl.droidsonroids.droidsmap.R
import pl.droidsonroids.droidsmap.feature.office.api.OfficeDataEndpoint
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeFeatureBoundary
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficeMvpView
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficePresenter
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficeUiModel
import pl.droidsonroids.droidsmap.feature.office.repository.OfficeRepository
import pl.droidsonroids.droidsmap.model.Coordinates
import pl.droidsonroids.droidsmap.model.Room
import java.util.*

private const val LOCATION_REQUEST_CODE = 1
private const val CAMERA_TRANSITION_DURATION_MILLIS = 300
private const val MAP_BEARING = 201.5f
private const val MIN_MAP_ZOOM = 18f
private const val MAX_MAP_ZOOM = 25f
private const val ROOM_TRANSITION_NAME = "room_transition"

class OfficeUiFeatureView(private val activity: MapActivity) : OfficeMvpView<OfficeUiModel> {

    private val presenter = OfficePresenter.create(this, OfficeFeatureBoundary.create(repository = OfficeRepository(OfficeDataEndpoint.create())))
    private var googleMap: GoogleMap? = null
    private val roomsList = ArrayList<Room>()
    private val groundOverlayList = ArrayList<GroundOverlay>()
    private var officeScene: Scene = Scene(activity.rootLayout, activity.officeSceneLayout)

    init {
        val mapFragment = activity.supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it
            it.setup()
            it.setOnGroundOverlayClickListener {
                onGroundOverlayClicked(it)
            }
            checkLocationPermission()
            createRoomList()
            UiCommandInvoker.invokeQueuedChain()
        }
    }

    private fun onGroundOverlayClicked(groundOverlay: GroundOverlay) {
        presenter.onRoomClicked(Coordinates.from(groundOverlay.position))
    }

    override fun animateCameraToClickedRoom(coordinates: Coordinates) {
        val cameraPosition = CameraPosition.Builder()
                .bearing(MAP_BEARING)
                .target(LatLng(coordinates.latitude, coordinates.longitude))
                .zoom(MAX_MAP_ZOOM)
                .build()

        val roomImageResource = R.drawable.room_3

        /*roomsList
                .filter { it.tag == groundOverlay.tag }
                .first()
                .imageResource TODO("remove hardcode & recognize image tag") */

        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)

        with(activity.roomImage) {
            googleMap?.animateCamera(cameraUpdate, CAMERA_TRANSITION_DURATION_MILLIS, CameraListenerAdapter({
                val imageDrawable = ContextCompat.getDrawable(activity, roomImageResource)
                setImageDrawable(imageDrawable)
                layoutParams.width = ((imageDrawable).intrinsicWidth * 2.2f).toInt()
                layoutParams.height = ((imageDrawable).intrinsicHeight * 2.2f).toInt()

                googleMap?.run {
                    with(projection.visibleRegion) {
                        addMarker(MarkerOptions().position(nearLeft).title("Near left"))
                        addMarker(MarkerOptions().position(nearRight).title("Near right"))
                        addMarker(MarkerOptions().position(farLeft).title("Far left"))
                        addMarker(MarkerOptions().position(farRight).title("Far right"))
                    }
                }

                performRoomTransition()
            }))
        }
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

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap?.isMyLocationEnabled = true
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
            }
        }
    }

    fun requestOffice() = presenter.onRequestOffice()

    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    googleMap?.isMyLocationEnabled = true
                } else {
                    Toast.makeText(activity, "Grant the permission!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun setMapPanningConstraints(uiModel: OfficeUiModel) {
        if (googleMap == null) UiCommandInvoker.queueInvokement { performSetMapPanningConstraints(uiModel) } else performSetMapPanningConstraints(uiModel)
    }

    private fun performSetMapPanningConstraints(uiModel: OfficeUiModel) {
        val bounds = LatLngBounds.builder()
                .include(LatLng(uiModel.centerLatitude - uiModel.mapViewportConstraint, uiModel.centerLongitude - uiModel.mapViewportConstraint))
                .include(LatLng(uiModel.centerLatitude + uiModel.mapViewportConstraint, uiModel.centerLongitude + uiModel.mapViewportConstraint))
                .build()

        googleMap?.setLatLngBoundsForCameraTarget(bounds)
    }

    override fun focusMapOnOfficeLocation(uiModel: OfficeUiModel) {
        if (googleMap == null) UiCommandInvoker.queueInvokement { performFocusMapOnOfficeLocation(uiModel) } else performFocusMapOnOfficeLocation(uiModel)
    }

    private fun performFocusMapOnOfficeLocation(uiModel: OfficeUiModel) {
        val officeCenterCoordinates = LatLng(uiModel.centerLatitude, uiModel.centerLongitude)

        val cameraPosition = CameraPosition.Builder()
                .bearing(uiModel.mapViewportBearing.toFloat())
                .target(officeCenterCoordinates)
                .zoom(20f)
                .build()

        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        googleMap?.moveCamera(cameraUpdate)
        googleMap?.moveCamera(CameraUpdateFactory.newLatLng(officeCenterCoordinates))
    }

    override fun displayOfficeRooms(uiModel: OfficeUiModel) {
        if (googleMap == null) UiCommandInvoker.queueInvokement { performDisplayOfficeRooms(uiModel) } else performDisplayOfficeRooms(uiModel)
    }

    private fun performDisplayOfficeRooms(uiModel: OfficeUiModel) {
        roomsList.forEach { createAndDisplayMapOverlay(it, uiModel) }
    }

    private fun createAndDisplayMapOverlay(room: Room, uiModel: OfficeUiModel) {
        val overlayOptions = GroundOverlayOptions()
                .image(createMarkerBitmapDescriptor(room.imageResource))
                .transparency(0f)
                .bearing(uiModel.mapViewportBearing.toFloat())
                .position(LatLng(
                        (uiModel.leftTopCornerLatitude + room.getRelativeCenterLatitude(uiModel.translatedMapViewportBearing)),
                        (uiModel.leftTopCornerLongitude + room.getRelativeCenterLongitude(uiModel.translatedMapViewportBearing))),
                        room.getRoomHeightMeters(), room.getRoomWidthMeters())
                .clickable(true)

        googleMap?.let {
            val overlay = (googleMap as GoogleMap).addGroundOverlay(overlayOptions)
            overlay.tag = room.tag
            groundOverlayList.add(overlay)
        }
    }

    private fun createMarkerBitmapDescriptor(imageResource: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(activity, imageResource)
        val width = vectorDrawable.intrinsicWidth
        val height = vectorDrawable.intrinsicHeight
        vectorDrawable.setBounds(0, 0, width, height)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun performRoomTransition() {
        with(activity) {
            //            shouldMoveBack = false
            roomImage.transitionName = ROOM_TRANSITION_NAME
            val roomScene = Scene.getSceneForLayout(rootLayout, R.layout.scene_room, this)
            roomScene.setEnterAction {
                val roomSceneImage = (roomScene.sceneRoot.findViewById(R.id.zoomedRoomImage) as ImageView)
                roomSceneImage.setImageDrawable(roomImage.drawable)
            }
            val sceneTransition = TransitionInflater.from(this).inflateTransition(R.transition.room_scene_enter_transition)
            TransitionManager.go(roomScene, sceneTransition)
        }
    }

    internal fun performOfficeTransition() {
        with(activity) {
            val sceneTransition = TransitionInflater.from(this).inflateTransition(R.transition.room_scene_exit_transition)
            sceneTransition.addListener(TransitionListenerAdapter({
                roomImage.transitionName = ""
                roomImage.setImageDrawable(null)

            }))
            TransitionManager.go(officeScene, sceneTransition)
        }
    }

    object UiCommandInvoker {
        private val uiCommands = mutableListOf<() -> Any>()

        fun queueInvokement(function: () -> Any) {
            uiCommands += function
        }

        fun invokeQueuedChain() = uiCommands.forEach {
            it()
            uiCommands -= it
        }
    }

    inner class TransitionListenerAdapter(private val endAction: () -> Unit) : Transition.TransitionListener {

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
            endAction()
        }
    }

    inner class CameraListenerAdapter(private val endAction: () -> Unit) : GoogleMap.CancelableCallback {
        override fun onFinish() {
            endAction()
        }

        override fun onCancel() {
            //no-op
        }
    }
}

private fun GoogleMap.setup() {
    with(uiSettings) {
        isZoomControlsEnabled = false
        isCompassEnabled = false
        isMyLocationButtonEnabled = false
    }
    this.setMinZoomPreference(MIN_MAP_ZOOM)
    this.mapType = GoogleMap.MAP_TYPE_TERRAIN
    this.isBuildingsEnabled = true
}

