package pl.droidsonroids.droidsmap

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import pl.droidsonroids.droidsmap.feature.office.ui.OfficeUiFeatureView
import pl.droidsonroids.droidsmap.feature.room.ui.RoomUiFeatureView

class MapActivity : AppCompatActivity() {

    private lateinit var officeFeature: OfficeUiFeatureView
    private lateinit var roomFeature: RoomUiFeatureView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        officeFeature = OfficeUiFeatureView(this)
        officeFeature.requestOffice()

        roomFeature = RoomUiFeatureView(this)
    }

    override fun onBackPressed() {
        officeFeature.onBackButtonPressed()
        roomFeature.onBackButtonPressed()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        officeFeature.onRequestPermissionsResult(requestCode, grantResults)
    }
}