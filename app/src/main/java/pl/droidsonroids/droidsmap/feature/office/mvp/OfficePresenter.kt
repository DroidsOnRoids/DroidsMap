package pl.droidsonroids.droidsmap.feature.office.mvp

import pl.droidsonroids.droidsmap.base.DataObserverAdapter
import pl.droidsonroids.droidsmap.base.Presenter
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeFeatureBoundary
import pl.droidsonroids.droidsmap.feature.room.mvp.RoomUiModel
import pl.droidsonroids.droidsmap.model.Coordinates

class OfficePresenter private constructor(
        private val officeView: OfficeMvpView<OfficeUiModel>,
        private val officeFeatureBoundary: OfficeFeatureBoundary) : Presenter {

    var officeUiModel: OfficeUiModel? = null
    var roomUiModels: Collection<RoomUiModel>? = null

    fun onRequestOffice() = officeFeatureBoundary.requestOffice(OfficeDataObserver())

    fun onRoomClicked(coordinates: Coordinates) {
        officeFeatureBoundary.changeToRoomPerspective()
        officeView.animateCameraToClickedRoom(coordinates)
    }

    fun onMapCameraAnimationCompleted() {
        officeView.prepareForRoomTransition()
    }

    fun onBackButtonPressed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun showOfficeMap(uiModel: OfficeUiModel) {
        officeUiModel = uiModel
        officeView.setMapPanningConstraints(uiModel)
        officeView.focusMapOnOfficeLocation(uiModel)
        officeView.displayOfficeRooms(uiModel, uiModel.roomUiModels)
    }

    private fun updateRoomsUi(rooms: Collection<RoomUiModel>) {
        roomUiModels = rooms
        updateRoomsUi()
    }

    private fun updateRoomsUi() {
        if (officeUiModel != null && roomUiModels != null) {

        }
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