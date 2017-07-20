package pl.droidsonroids.droidsmap.feature.office.mvp

import pl.droidsonroids.droidsmap.base.DataObserverAdapter
import pl.droidsonroids.droidsmap.base.Presenter
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeFeatureBoundary
import pl.droidsonroids.droidsmap.model.Coordinates

class OfficePresenter private constructor(
        private val officeFeatureBoundary: OfficeFeatureBoundary) : Presenter<OfficeMvpView>() {

    override fun onFlowNavigatorRegistered() {
        view.initMap()
    }

    fun onRequestOffice() = officeFeatureBoundary.requestOffice(OfficeDataObserver())

    fun onRoomClicked(coordinates: Coordinates) {
        view.animateCameraToClickedRoom(coordinates)
    }

    fun onMapCameraAnimationCompleted() {
        view.prepareForRoomTransition()
        view.performRoomTransition()
    }

    private fun showOfficeMap(uiModel: OfficeUiModel) {
        view.setMapPanningConstraints(uiModel)
        view.focusMapOnOfficeLocation(uiModel)
        view.displayOfficeRooms(uiModel)
    }

    companion object {
        fun create(officeFeatureBoundary: OfficeFeatureBoundary)
                = OfficePresenter(officeFeatureBoundary)
    }

    inner class OfficeDataObserver : DataObserverAdapter<OfficeUiModel>() {
        override fun onNext(model: OfficeUiModel) {
            showOfficeMap(model)
        }

        override fun onError(e: Throwable) {
            TODO()
        }
    }
}