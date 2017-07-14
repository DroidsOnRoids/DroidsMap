package pl.droidsonroids.droidsmap.feature.office.mvp

import pl.droidsonroids.droidsmap.model.Coordinates

interface OfficeMvpView<in T> {
    fun setMapPanningConstraints(uiModel: T)
    fun focusMapOnOfficeLocation(uiModel: T)
    fun displayOfficeRooms(officeUiModel: OfficeUiModel)
    fun animateCameraToClickedRoom(coordinates: Coordinates)
    fun prepareForRoomTransition()
    fun performOfficeTransition()
}