package pl.droidsonroids.droidsmap

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import pl.droidsonroids.droidsmap.base.MapActivityWrapper
import pl.droidsonroids.droidsmap.feature.office.api.OfficeDataEndpoint
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeFeatureBoundary
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficePresenter
import pl.droidsonroids.droidsmap.feature.office.repository.OfficeRepository
import pl.droidsonroids.droidsmap.feature.office.ui.OfficeUiFeatureView
import pl.droidsonroids.droidsmap.feature.room.api.RoomDataEndpoint
import pl.droidsonroids.droidsmap.feature.room.api.RoomImagesEndpoint
import pl.droidsonroids.droidsmap.feature.room.ui.RoomUiFeatureView

private const val LOCATION_REQUEST_CODE = 1

class MapActivity : AppCompatActivity() {

    private lateinit var flowManager: FlowManager
    private lateinit var officeFeature: OfficeUiFeatureView
    private lateinit var roomFeature: RoomUiFeatureView

    val appTerminateCallback = object : TerminationCallback {
        override fun onAppTerminate() {
            this@MapActivity.finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val officeBoundary = OfficeFeatureBoundary.create(
                officeRepository = OfficeRepository(OfficeDataEndpoint.create(), RoomDataEndpoint.create(), RoomImagesEndpoint.create()))

        officeFeature = OfficeUiFeatureView(MapActivityWrapper(this), OfficePresenter.create(officeBoundary))
        officeFeature.initMap()
        roomFeature = RoomUiFeatureView(this)
        flowManager = FlowManager(officeFeature, roomFeature, appTerminateCallback)

        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            officeFeature.onLocationPermissionGranted()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.elementAtOrElse(0, { PackageManager.PERMISSION_DENIED }) == PackageManager.PERMISSION_GRANTED) {
                    officeFeature.onLocationPermissionGranted()
                } else {
                    Toast.makeText(this, "Grant the permission!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        flowManager.onBackButtonPressed()
    }
}

interface TerminationCallback {
    fun onAppTerminate()
}