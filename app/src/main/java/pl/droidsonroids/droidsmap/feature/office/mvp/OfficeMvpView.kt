package pl.droidsonroids.droidsmap.feature.office.mvp

interface OfficeMvpView<in T> {
    fun setMapPanningConstraints(uiModel: T)
    fun focusMapOnOfficeLocation(uiModel: T)
    fun displayOfficeRooms(uiModel: T)
    fun animateCameraToClickedRoom()
}