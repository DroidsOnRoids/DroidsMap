package pl.droidsonroids.droidsmap.feature.office.mvp

import pl.droidsonroids.droidsmap.base.DataObserverAdapter
import pl.droidsonroids.droidsmap.base.Presenter
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeFeatureBoundary
import pl.droidsonroids.droidsmap.feature.room.ui.RoomUiFeatureView
import pl.droidsonroids.droidsmap.model.Coordinates

open class OfficePresenter private constructor(
        private val officeFeatureBoundary: OfficeFeatureBoundary) : OfficePresenterContract() {

    override fun onViewAttached() = view.initMap()

    override fun onRequestOffice() = officeFeatureBoundary.requestOffice(OfficeDataObserver())

    override fun onRoomClicked(coordinates: Coordinates) = view.animateCameraToClickedRoom(coordinates)

    override fun onMapCameraAnimationCompleted() {
        view.prepareForRoomTransition()
        view.performRoomTransition()
        flowChangeNavigator?.changePerspective(RoomUiFeatureView::class)
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
        override fun onNext(model: OfficeUiModel) = showOfficeMap(model)

        override fun onError(e: Throwable) = TODO()
    }
}

abstract class OfficePresenterContract : Presenter<OfficeMvpView>(){
    abstract fun onRequestOffice()
    abstract fun onRoomClicked(coordinates: Coordinates)
    abstract fun onMapCameraAnimationCompleted()
}