package pl.droidsonroids.droidsmap.feature.office.mvp

import pl.droidsonroids.droidsmap.feature.room.mvp.RoomUiModel
import pl.droidsonroids.droidsmap.model.Coordinates

interface OfficeMvpView<in T> {
    fun setMapPanningConstraints(uiModel: T)
    fun focusMapOnOfficeLocation(uiModel: T)
    fun displayOfficeRooms(officeUiModel: OfficeUiModel, roomUiModels: Collection<RoomUiModel>)
    fun animateCameraToClickedRoom(coordinates: Coordinates)
}