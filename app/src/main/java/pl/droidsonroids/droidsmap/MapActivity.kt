package pl.droidsonroids.droidsmap

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import pl.droidsonroids.droidsmap.base.MapActivityWrapper
import pl.droidsonroids.droidsmap.feature.office.ui.OfficeUiFeatureView
import pl.droidsonroids.droidsmap.feature.room.ui.RoomUiFeatureView

class MapActivity : AppCompatActivity() {

    private lateinit var flowManager: FlowManager
    private lateinit var officeFeature: OfficeUiFeatureView
    private lateinit var roomFeature: RoomUiFeatureView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        officeFeature = OfficeUiFeatureView(MapActivityWrapper(this))
        roomFeature = RoomUiFeatureView(MapActivityWrapper(this))
        flowManager = FlowManager(officeFeature, roomFeature)
    }

    override fun onBackPressed() {
        flowManager.onBackNavigationButtonPressed()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        flowManager.onRequestPermissionsResult(requestCode, grantResults)
    }
}