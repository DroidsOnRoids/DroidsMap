package pl.droidsonroids.droidsmap.feature.office.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.PictureDrawable
import android.transition.Transition
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import pl.droidsonroids.droidsmap.ActivityWrapper
import pl.droidsonroids.droidsmap.GoogleMapWrapper
import pl.droidsonroids.droidsmap.base.BaseFeatureView
import pl.droidsonroids.droidsmap.feature.office.api.OfficeDataEndpoint
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeFeatureBoundary
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficeMvpView
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficePresenter
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficeUiModel
import pl.droidsonroids.droidsmap.feature.office.repository.OfficeRepository
import pl.droidsonroids.droidsmap.feature.room.api.RoomDataEndpoint
import pl.droidsonroids.droidsmap.feature.room.api.RoomImagesEndpoint
import pl.droidsonroids.droidsmap.feature.room.mvp.RoomUiModel
import pl.droidsonroids.droidsmap.model.Coordinates
import java.util.*


private const val LOCATION_REQUEST_CODE = 1
private const val CAMERA_TRANSITION_DURATION_MILLIS = 300
private const val MAP_BEARING = 201.5f
private const val MIN_MAP_ZOOM = 18f
private const val MAX_MAP_ZOOM = 25f

open class OfficeUiFeatureView(private val activityWrapper: ActivityWrapper) : BaseFeatureView<OfficePresenter>(), OfficeMvpView {


    private val officeBoundary = OfficeFeatureBoundary.create(
            officeRepository = OfficeRepository(OfficeDataEndpoint.create(), RoomDataEndpoint.create(), RoomImagesEndpoint.create()))
    private var googleMap: GoogleMapWrapper? = null
    private val groundOverlayList = ArrayList<GroundOverlay>()
//    private var officeScene: Scene = Scene(activityWrapper.officeRoot, activityWrapper.officeSceneLayout)

    init {
        presenter = OfficePresenter.create(this, officeBoundary)
        val mapFragment = activityWrapper.getMapFragmentWrapper()
        mapFragment.getMapAsync { mapWrapper ->
            googleMap = mapWrapper
            mapWrapper.setup(MIN_MAP_ZOOM)
            mapWrapper.setOnGroundOverlayClickListener {
                onGroundOverlayClicked(it)
            }
            checkLocationPermission()
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

        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)

        googleMap?.animateCamera(cameraUpdate, CAMERA_TRANSITION_DURATION_MILLIS, CameraListenerAdapter({
            presenter.onMapCameraAnimationCompleted()
        }))
    }

    override fun prepareForRoomTransition() {
        with(activityWrapper.roomImage) {
            val resources = activityWrapper.resources
            val resourceId = resources.getIdentifier("room_3", "drawable", activityWrapper.packageName)
            val roomImageDrawable = resources.getDrawable(resourceId)

            setImageDrawable(roomImageDrawable)
            layoutParams.width = ((roomImageDrawable).intrinsicWidth * 2.2f).toInt()
            layoutParams.height = ((roomImageDrawable).intrinsicHeight * 2.2f).toInt()

            googleMap?.run {
                with(projection.visibleRegion) {
                    addMarker(MarkerOptions().position(nearLeft).title("Near left"))
                    addMarker(MarkerOptions().position(nearRight).title("Near right"))
                    addMarker(MarkerOptions().position(farLeft).title("Far left"))
                    addMarker(MarkerOptions().position(farRight).title("Far right"))
                }
            }
        }
    }

//    private fun createRoomList() {
//        roomsList.add(Room(228f, 144f, 114f, 138f, "skybuds room", R.drawable.room_3))
//        roomsList.add(Room(148f, 204f, 74f, 304f, "server room", R.drawable.room_2))
//        roomsList.add(Room(100f, 168f, 50f, 454f, "server room2", R.drawable.room_2a))
//        roomsList.add(Room(164f, 116f, 174f, 456f, "room 1", R.drawable.room_1))
//        roomsList.add(Room(126f, 142f, 257f, 273f, "room 4", R.drawable.room_4))
//        roomsList.add(Room(166f, 202f, 301f, 607f, "room m", R.drawable.room_m))
//        roomsList.add(Room(144f, 202f, 448f, 607f, "room 5", R.drawable.room_5))
//        roomsList.add(Room(256f, 204f, 562f, 304f, "room 6", R.drawable.room_6))
//        roomsList.add(Room(130f, 70f, 377f, 371f, "wall 1", R.drawable.wall_1))
//        roomsList.add(Room(146f, 202f, 585f, 607f, "room_7", R.drawable.room_7))
//        roomsList.add(Room(194f, 406f, 759f, 203f, "room_fun", R.drawable.room_fun))
//        roomsList.add(Room(206f, 202f, 753f, 607f, "room_9", R.drawable.room_9))
//    }

    private fun checkLocationPermission() {
        if (activityWrapper.isLocationPermissionGranted()) {
            googleMap?.isMyLocationEnabled = true
        } else {
            activityWrapper.requestPermission(LOCATION_REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    fun requestOffice() = presenter.onRequestOffice()

    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    googleMap?.isMyLocationEnabled = true
                } else {
                    activityWrapper.showToast("Grant the permission!", Toast.LENGTH_SHORT)
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
    }

    override fun displayOfficeRooms(officeUiModel: OfficeUiModel) {
        if (googleMap == null) {
            UiCommandInvoker.queueInvokement { performDisplayOfficeRooms(officeUiModel) }
        } else {
            performDisplayOfficeRooms(officeUiModel)
        }
    }

    private fun performDisplayOfficeRooms(officeUiModel: OfficeUiModel) {
        officeUiModel.roomUiModels.forEach { roomUiModel ->
            Glide.with(activityWrapper.getActivity())
                    .`as`(PictureDrawable::class.java)
                    .load(roomUiModel.imageUrl)
                    .listener(object : RequestListener<PictureDrawable> {
                        override fun onLoadFailed(p0: GlideException?, p1: Any?, target: Target<PictureDrawable>?, p3: Boolean): Boolean {
                            p0?.printStackTrace()
                            return false
                        }

                        override fun onResourceReady(bitmap: PictureDrawable?, p1: Any?, target: Target<PictureDrawable>?, p3: DataSource?, p4: Boolean): Boolean {
                            var bitmap = pictureDrawableToBitmap(bitmap!!)
                            createAndDisplayMapOverlay(roomUiModel, officeUiModel, bitmap)
                            return false
                        }
                    })
                    .submit()
        }
    }

    private fun pictureDrawableToBitmap(pictureDrawable: PictureDrawable): Bitmap {
        val bmp = Bitmap.createBitmap(pictureDrawable.intrinsicWidth, pictureDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        canvas.drawPicture(pictureDrawable.picture)
        return bmp
    }

    private fun createAndDisplayMapOverlay(room: RoomUiModel, uiModel: OfficeUiModel, bitmap: Bitmap) {
        val overlayOptions = GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromBitmap(bitmap))
                .transparency(0f)
                .bearing(uiModel.mapViewportBearing.toFloat())
                .position(LatLng(
                        (uiModel.leftTopCornerLatitude + room.getRelativeCenterLatitude(uiModel.translatedMapViewportBearing)),
                        (uiModel.leftTopCornerLongitude + room.getRelativeCenterLongitude(uiModel.translatedMapViewportBearing))),
                        room.getRoomHeightMeters(), room.getRoomWidthMeters())
                .clickable(true)

        googleMap?.let {
            val overlay = (googleMap as GoogleMapWrapper).addGroundOverlay(overlayOptions)
//            overlay.tag = room.tag
            groundOverlayList.add(overlay)
        }
    }

    override fun performOfficeTransition() {
//        activityWrapper.performTransition(
//                R.transition.room_scene_exit_transition,
//                officeScene,
//                TransitionListenerAdapter({
//                    activityWrapper.roomImage.setImageDrawable(null)
//                }))
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

    fun onBackButtonPressed() {
        presenter.onBackButtonPressed()
    }
}

