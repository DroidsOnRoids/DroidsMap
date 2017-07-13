package pl.droidsonroids.droidsmap.feature.office.mvp

import pl.droidsonroids.droidsmap.base.DataObserverAdapter
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeFeatureBoundary
import pl.droidsonroids.droidsmap.feature.room.business_logic.RoomFeatureBoundary
import pl.droidsonroids.droidsmap.feature.room.mvp.RoomMvpView
import pl.droidsonroids.droidsmap.feature.room.mvp.RoomUiModel
import pl.droidsonroids.droidsmap.model.Coordinates

class OfficePresenter private constructor(
        private val officeView: OfficeMvpView<OfficeUiModel>,
        private val officeFeatureBoundary: OfficeFeatureBoundary) {

    fun onRequestOffice() = officeFeatureBoundary.requestOffice(OfficeDataObserver())

    fun onRequestRooms() = officeFeatureBoundary.requestRooms(RoomsDataObserver())

    fun onRoomClicked(coordinates: Coordinates) {
        officeFeatureBoundary.changeToRoomPerspective()
        officeView.animateCameraToClickedRoom(coordinates)
    }

    private fun updateUi(uiModel: OfficeUiModel) {
        officeView.setMapPanningConstraints(uiModel)
        officeView.focusMapOnOfficeLocation(uiModel)
    }

    private fun updateRoomsUi(rooms: Collection<RoomUiModel>) {
        val emptyOfficeUiModel = OfficeUiModel(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        officeView.displayOfficeRooms(emptyOfficeUiModel, rooms)
    }

    companion object {
        fun create(view: OfficeMvpView<OfficeUiModel>, officeFeatureBoundary: OfficeFeatureBoundary)
                = OfficePresenter(view, officeFeatureBoundary)
    }

    inner class OfficeDataObserver : DataObserverAdapter<OfficeUiModel>() {
        override fun onNext(model: OfficeUiModel) {
            updateUi(model)
        }

        override fun onError(e: Throwable) {
            TODO("NYI") // implement data fetch error
        }
    }

    inner class RoomsDataObserver : DataObserverAdapter<Collection<RoomUiModel>>() {
        override fun onNext(rooms: Collection<RoomUiModel>) {
            updateRoomsUi(rooms)
        }

        override fun onError(e: Throwable) {
            TODO("NYI") // implement data fetch error
        }
    }
}