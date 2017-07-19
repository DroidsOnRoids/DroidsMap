package pl.droidsonroids.droidsmap

import pl.droidsonroids.droidsmap.feature.office.ui.OfficeUiGateway
import pl.droidsonroids.droidsmap.feature.room.ui.RoomUiGateway

class FlowManager(private val officeFeatureGateway: OfficeUiGateway, private val roomFeatureGateway: RoomUiGateway) {

    var currentPerspective = Perspective.OFFICE

    init {
        officeFeatureGateway.requestOffice()
    }

    fun onBackButtonPressed() {
        TODO()
    }
}
