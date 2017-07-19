package pl.droidsonroids.droidsmap

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import pl.droidsonroids.droidsmap.feature.office.ui.OfficeUiFeatureView
import pl.droidsonroids.droidsmap.feature.room.ui.RoomUiFeatureView

private const val LOCATION_REQUEST_CODE = 1

class MapActivity : AppCompatActivity() {

    private lateinit var flowManager: FlowManager
    private lateinit var officeFeature: OfficeUiFeatureView
    private lateinit var roomFeature: RoomUiFeatureView

    val terminateListener = object : Terminatable {
        override fun onTerminate() {
            this@MapActivity.finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        officeFeature = OfficeUiFeatureView(this)
        roomFeature = RoomUiFeatureView(this)
        flowManager = FlowManager(officeFeature, roomFeature, terminateListener)
    }

    override fun onBackPressed() {
        flowManager.onBackButtonPressed()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    officeFeature.onLocationPermissionGranted()
                } else {
                    Toast.makeText(this, "Grant the permission!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

interface Terminatable {
    fun onTerminate()
}