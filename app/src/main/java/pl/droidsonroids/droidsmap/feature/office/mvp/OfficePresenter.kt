package pl.droidsonroids.droidsmap.feature.office.mvp

import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeFeatureBoundary

class OfficePresenter private constructor(private val view: OfficeMvpView) {

    private val officeFeatureBoundary = OfficeFeatureBoundary.create()

    fun onRequestOffice() = officeFeatureBoundary.requestOffice(object : OfficeFeatureBoundary.Gateway {
        override fun onOfficeEntityAvailable(entity: OfficeEntity) = updateUi(OfficeUiModel.from(entity))
    })

    private fun updateUi(uiModel: OfficeUiModel) {
        view.setMapPanningConstraints(uiModel)
        view.focusMapOnOfficeLocation(uiModel)
        view.displayOfficeRooms(uiModel)
    }

    companion object {
        fun create(view: OfficeMvpView) = OfficePresenter(view)
    }
}