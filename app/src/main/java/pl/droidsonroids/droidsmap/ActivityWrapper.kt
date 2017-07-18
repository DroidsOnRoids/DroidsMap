package pl.droidsonroids.droidsmap

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.support.annotation.TransitionRes
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.transition.Scene
import android.transition.Transition
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.scene_office_map.*
import kotlinx.android.synthetic.main.scene_room.*
import pl.droidsonroids.droidsmap.feature.office.ui.OfficeUiFeatureView
import pl.droidsonroids.droidsmap.feature.room.ui.RoomUiFeatureView

class ActivityWrapper(
        activity: AppCompatActivity? = null,
        officeFeature: OfficeUiFeatureView? = null,
        roomFeature: RoomUiFeatureView? = null) {

    lateinit private var activity: AppCompatActivity
    private val officeFeature: OfficeUiFeatureView = officeFeature ?: OfficeUiFeatureView(this)
    private val roomFeature: RoomUiFeatureView = roomFeature ?: RoomUiFeatureView(this)

    init {
        if (activity != null) {
            this.activity = activity
        }
    }

    val officeRoot: ViewGroup
        get() = activity.rootLayout

    val roomRoot: ViewGroup
        get() = activity.roomRootLayout

    val officeSceneLayout: View
        get() = activity.officeSceneLayout

    val roomImage: ImageView
        get() = activity.roomImage

    val resources: Resources
        get() = activity.resources

    val packageName: String
        get() = activity.packageName

    fun getMapFragmentWrapper(): GoogleMapFragmentWrapper {
        return GoogleMapFragmentWrapper(
                activity.supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment)
    }

    fun showToast(text: String, length: Int) {
        Toast.makeText(activity, text, length).show()
    }

    fun performTransition(@TransitionRes resource: Int, scene: Scene, transitionListener: Transition.TransitionListener) {
        val transition = TransitionInflater.from(activity).inflateTransition(resource)
        transition.addListener(transitionListener)
        TransitionManager.go(scene, transition)
    }

    fun notifyPerspectiveChanged(perspective: Perspective) {
        roomFeature.onPerspectiveChanged(perspective)
    }

    fun onBackPressed() {
        officeFeature.onBackButtonPressed()
        roomFeature.onBackButtonPressed()
    }

    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
        officeFeature.onRequestPermissionsResult(requestCode, grantResults)
    }

    fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    fun requestPermission(requestCode: Int, permission: String) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            TODO()
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
        }
    }

}