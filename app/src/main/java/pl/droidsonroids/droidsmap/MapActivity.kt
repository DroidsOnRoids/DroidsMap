package pl.droidsonroids.droidsmap

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import pl.droidsonroids.droidsmap.feature.office.ui.OfficeUiFeatureView

class MapActivity : AppCompatActivity() {

    private lateinit var officeFeature: OfficeUiFeatureView

    private var shouldMoveBack = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        officeFeature = OfficeUiFeatureView(this)
        officeFeature.requestOffice()
    }

    override fun onBackPressed() {
        if (shouldMoveBack) {
            super.onBackPressed()
        } else {
            officeFeature.performOfficeTransition()
            shouldMoveBack = true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        officeFeature.onRequestPermissionsResult(requestCode, grantResults)
    }
}