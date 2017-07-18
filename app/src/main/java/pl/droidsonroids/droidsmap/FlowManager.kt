package pl.droidsonroids.droidsmap

import pl.droidsonroids.droidsmap.feature.office.ui.OfficeUiFeatureView
import pl.droidsonroids.droidsmap.feature.room.ui.RoomUiFeatureView

class FlowManager(private val officeFeature: OfficeUiFeatureView, private val roomFeature: RoomUiFeatureView) {

    init {
        this.officeFeature.requestOffice()
    }

    fun notifyPerspectiveChanged(perspective: Perspective) {
        roomFeature.onPerspectiveChanged(perspective)
    }

    fun onBackNavigationButtonPressed() {
        officeFeature.onBackButtonPressed()
        roomFeature.onBackButtonPressed()
    }

    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
        officeFeature.onRequestPermissionsResult(requestCode, grantResults)
    }
}
