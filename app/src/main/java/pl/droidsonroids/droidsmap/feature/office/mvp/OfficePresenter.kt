package pl.droidsonroids.droidsmap.feature.office.mvp

import pl.droidsonroids.droidsmap.base.DataObserverAdapter
import pl.droidsonroids.droidsmap.base.Presenter
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeFeatureBoundary
import pl.droidsonroids.droidsmap.model.Coordinates

class OfficePresenter private constructor(
        private val officeView: OfficeMvpView,
        private val officeFeatureBoundary: OfficeFeatureBoundary) : Presenter {

    fun onRequestOffice() = officeFeatureBoundary.requestOffice(OfficeDataObserver())

    fun onRoomClicked(coordinates: Coordinates) {
        officeView.animateCameraToClickedRoom(coordinates)
    }

    fun onMapCameraAnimationCompleted() {
        officeView.prepareForRoomTransition()
    }

    private fun showOfficeMap(uiModel: OfficeUiModel) {
        officeView.setMapPanningConstraints(uiModel)
        officeView.focusMapOnOfficeLocation(uiModel)
        officeView.displayOfficeRooms(uiModel)
    }

    companion object {
        fun create(view: OfficeMvpView, officeFeatureBoundary: OfficeFeatureBoundary)
                = OfficePresenter(view, officeFeatureBoundary)
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