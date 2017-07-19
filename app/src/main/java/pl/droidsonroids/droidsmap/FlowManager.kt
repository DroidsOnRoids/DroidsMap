package pl.droidsonroids.droidsmap

import pl.droidsonroids.droidsmap.feature.office.ui.OfficeUiGateway
import pl.droidsonroids.droidsmap.feature.room.ui.RoomUiGateway

class FlowManager(
        private val officeFeatureGateway: OfficeUiGateway,
        private val roomFeatureGateway: RoomUiGateway,
        private val terminateCallback: TerminationCallback) : ForwardFlowChangeListener {

    var currentPerspective = Perspective.OFFICE

    init {
        officeFeatureGateway.registerFlowChangeCallback(this)
        roomFeatureGateway.registerFlowChangeCallback(this)
        officeFeatureGateway.requestOffice()
    }

    fun onBackButtonPressed() {
        if (currentPerspective == Perspective.ROOM) {
            currentPerspective = Perspective.OFFICE
            roomFeatureGateway.onPerspectiveChanged(false)
            officeFeatureGateway.onPerspectiveChanged(true)
        } else {
            officeFeatureGateway.onPerspectiveChanged(false)
            terminateCallback.onAppTerminate()
        }
    }

    override fun onPerspectiveChanged(newPerspective: Perspective) {
        currentPerspective = newPerspective
    }
}

interface ForwardFlowChangeListener {
    fun onPerspectiveChanged(newPerspective: Perspective)
}
