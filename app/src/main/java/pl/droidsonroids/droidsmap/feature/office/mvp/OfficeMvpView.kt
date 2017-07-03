package pl.droidsonroids.droidsmap.feature.office.mvp

interface OfficeMvpView {
    fun setMapPanningConstraints(uiModel: OfficeUiModel)
    fun focusMapOnOfficeLocation(uiModel: OfficeUiModel)
    fun displayOfficeRooms(uiModel: OfficeUiModel)
}