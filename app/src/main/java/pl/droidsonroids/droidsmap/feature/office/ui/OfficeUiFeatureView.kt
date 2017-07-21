package pl.droidsonroids.droidsmap.feature.office.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.PictureDrawable
import android.transition.Scene
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.scene_office_map.*
import pl.droidsonroids.droidsmap.FlowNavigator
import pl.droidsonroids.droidsmap.R
import pl.droidsonroids.droidsmap.base.BaseFeatureView
import pl.droidsonroids.droidsmap.base.MapActivityWrapper
import pl.droidsonroids.droidsmap.base.MvpView
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficeMvpView
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficePresenter
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficeUiModel
import pl.droidsonroids.droidsmap.feature.room.mvp.RoomUiModel
import pl.droidsonroids.droidsmap.model.Coordinates
import java.util.*

private const val CAMERA_TRANSITION_DURATION_MILLIS = 300
private const val MAP_BEARING = 201.5f
private const val MIN_MAP_ZOOM = 18f
private const val MAX_MAP_ZOOM = 25f

class OfficeUiFeatureView(private val activityWrapper: MapActivityWrapper, presenter: OfficePresenter) : BaseFeatureView<OfficeMvpView, OfficePresenter>(), OfficeMvpView, OfficeUiGateway {

    private var googleMap: GoogleMap? = null
    private val groundOverlayList = ArrayList<GroundOverlay>()

    init {
        this.presenter = presenter
    }

    override fun initMap() {
        val mapFragment = activityWrapper.activity.supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it
            it.setup()
            it.setOnGroundOverlayClickListener {
                onGroundOverlayClicked(it)
            }
            UiCommandInvoker.invokeQueuedChain()
        }
    }

    override fun registerFlowNavigator(flowNavigator: FlowNavigator) {
        presenter.registerFlowNavigator(flowNavigator)
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

    override fun onLocationPermissionGranted() {
        googleMap?.isMyLocationEnabled = true
    }

    override fun onPerspectiveChanged(active: Boolean) = Unit

    private fun requestOffice() = presenter.onRequestOffice()

    override fun setMapPanningConstraints(uiModel: OfficeUiModel) {
        appointUiTaskIfMapNull { performSetMapPanningConstraints(uiModel) }
    }

    private fun appointUiTaskIfMapNull(function: () -> Any) {
        if (googleMap == null) {
            UiCommandInvoker.queueInvokement { function }
        } else {
            function()
        }
    }

    private fun performSetMapPanningConstraints(uiModel: OfficeUiModel) {
        val bounds = LatLngBounds.builder()
                .include(LatLng(uiModel.centerLatitude - uiModel.mapViewportConstraint, uiModel.centerLongitude - uiModel.mapViewportConstraint))
                .include(LatLng(uiModel.centerLatitude + uiModel.mapViewportConstraint, uiModel.centerLongitude + uiModel.mapViewportConstraint))
                .build()

        googleMap?.setLatLngBoundsForCameraTarget(bounds)
    }

    override fun focusMapOnOfficeLocation(uiModel: OfficeUiModel) {
        appointUiTaskIfMapNull { performFocusMapOnOfficeLocation(uiModel) }
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
        appointUiTaskIfMapNull { performDisplayOfficeRooms(officeUiModel) }
    }

    private fun performDisplayOfficeRooms(officeUiModel: OfficeUiModel) {
        officeUiModel.roomUiModels.forEach { roomUiModel ->
            Glide.with(activityWrapper.activity)
                    .`as`(PictureDrawable::class.java)
                    .load(roomUiModel.imageUrl)
                    .listener(object : RequestListener<PictureDrawable> {
                        override fun onLoadFailed(exception: GlideException?, ignored: Any?, target: Target<PictureDrawable>?, aBoolean: Boolean): Boolean {
                            exception?.printStackTrace()
                            return false
                        }

                        override fun onResourceReady(pictureDrawable: PictureDrawable?, ignored: Any?, target: Target<PictureDrawable>?, dataSource: DataSource?, aBoolean: Boolean): Boolean {
                            val bitmap = convertPictureDrawableToBitmap(pictureDrawable!!)
                            createAndDisplayMapOverlay(roomUiModel, officeUiModel, bitmap)
                            return false
                        }
                    })
                    .submit()
        }
    }

    private fun convertPictureDrawableToBitmap(pictureDrawable: PictureDrawable): Bitmap {
        val resultBitmap = Bitmap.createBitmap(pictureDrawable.intrinsicWidth, pictureDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resultBitmap)
        canvas.drawPicture(pictureDrawable.picture)
        return resultBitmap
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
            val overlay = (googleMap as GoogleMap).addGroundOverlay(overlayOptions)
//            overlay.tag = room.
            groundOverlayList.add(overlay)
        }
    }

    override fun prepareForRoomTransition() {
        with(activityWrapper.activity) {
            val resourceId = resources.getIdentifier("room_3", "drawable", this.packageName)
            val roomImageDrawable = resources.getDrawable(resourceId)

            with(roomImage) {
                setImageDrawable(roomImageDrawable)
                layoutParams.width = ((roomImageDrawable).intrinsicWidth * 2.2f).toInt()
                layoutParams.height = ((roomImageDrawable).intrinsicHeight * 2.2f).toInt()
            }

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

    override fun performRoomTransition() {
        with(activityWrapper.activity) {
            val roomScene = Scene.getSceneForLayout(rootLayout, R.layout.scene_room, this)
            roomScene.setEnterAction {
                val roomSceneImage = (roomScene.sceneRoot.findViewById(R.id.zoomedRoomImage) as ImageView)
                roomSceneImage.setImageDrawable(roomImage.drawable)
            }
            val sceneTransition = TransitionInflater.from(this).inflateTransition(R.transition.room_scene_enter_transition)
            TransitionManager.go(roomScene, sceneTransition)
        }
    }

    inner class CameraListenerAdapter(private val endAction: () -> Unit) : GoogleMap.CancelableCallback {
        override fun onFinish() {
            endAction()
        }

        override fun onCancel() = Unit
    }

    private fun GoogleMap.setup() {
        with(uiSettings) {
            isZoomControlsEnabled = false
            isCompassEnabled = false
            isMyLocationButtonEnabled = false
        }
        setMinZoomPreference(MIN_MAP_ZOOM)
        mapType = GoogleMap.MAP_TYPE_TERRAIN
        isBuildingsEnabled = true
    }
}

interface OfficeUiGateway : MvpView {
    fun onLocationPermissionGranted()
}

