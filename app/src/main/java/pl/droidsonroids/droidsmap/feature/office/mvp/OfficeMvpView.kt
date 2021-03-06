package pl.droidsonroids.droidsmap.feature.office.mvp

import pl.droidsonroids.droidsmap.base.MvpView
import pl.droidsonroids.droidsmap.model.Coordinates

interface OfficeMvpView : MvpView {
    fun initMap()
    fun setMapPanningConstraints(uiModel: OfficeUiModel)
    fun focusMapOnOfficeLocation(uiModel: OfficeUiModel)
    fun displayOfficeRooms(officeUiModel: OfficeUiModel)
    fun animateCameraToClickedRoom(coordinates: Coordinates)
    fun prepareForRoomTransition()
    fun performRoomTransition()
}