package pl.droidsonroids.droidsmap.feature.office.mvp

import pl.droidsonroids.droidsmap.base.DataObserverAdapter
import pl.droidsonroids.droidsmap.base.Presenter
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeFeatureBoundary
import pl.droidsonroids.droidsmap.model.Coordinates

class OfficePresenter private constructor(
        private val view: OfficeMvpView<OfficeUiModel>,
        private val officeFeatureBoundary: OfficeFeatureBoundary) : Presenter {

    fun onRequestOffice() = officeFeatureBoundary.requestOffice(OfficeDataObserver())

    fun onRoomClicked(coordinates: Coordinates) {
        officeFeatureBoundary.changeToRoomPerspective()
        view.animateCameraToClickedRoom(coordinates)
    }

    fun onMapCameraAnimationCompleted() {
        view.prepareForRoomTransition()
    }

    fun onBackButtonPressed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun showOfficeMap(uiModel: OfficeUiModel) {
        view.setMapPanningConstraints(uiModel)
        view.focusMapOnOfficeLocation(uiModel)
        view.displayOfficeRooms(uiModel)
    }

    companion object {
        fun create(view: OfficeMvpView<OfficeUiModel>, officeFeatureBoundary: OfficeFeatureBoundary)
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